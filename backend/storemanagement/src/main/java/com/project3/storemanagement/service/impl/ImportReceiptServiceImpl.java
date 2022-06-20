package com.project3.storemanagement.service.impl;

import com.project3.storemanagement.dto.ImportReceiptDto;
import com.project3.storemanagement.dto.ImportReceiptResponseDto;
import com.project3.storemanagement.dto.LineItemDto;
import com.project3.storemanagement.entities.*;
import com.project3.storemanagement.service.ImportReceiptService;
import com.project3.storemanagement.service.OrderService;
import com.project3.storemanagement.service.VariantService;
import com.project3.storemanagement.entities.*;
import com.project3.storemanagement.exception.BadNumberException;
import com.project3.storemanagement.exception.RecordNotFoundException;
import com.project3.storemanagement.repository.ImportReceiptRepository;
import com.project3.storemanagement.repository.VariantsImportReceiptRepository;
import com.project3.storemanagement.repository.VariantsOrderRepository;
import com.project3.storemanagement.repository.VariantsReturnReceiptRepository;
import com.project3.storemanagement.utils.itemcodegenerator.ItemCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ImportReceiptServiceImpl implements ImportReceiptService {
    private final ImportReceiptRepository importReceiptRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private VariantsOrderRepository variantsOrderRepository;

    @Autowired
    private VariantsImportReceiptRepository variantsImportReceiptRepository;

    @Autowired
    private VariantsReturnReceiptRepository variantsReturnReceiptRepository;

    @Autowired
    private VariantService variantService;

    @Autowired
    @Qualifier("import-receipt-code-generator")
    private ItemCodeGenerator itemCodeGenerator;

    @Autowired
    public ImportReceiptServiceImpl(ImportReceiptRepository importReceiptRepository) {
        this.importReceiptRepository = importReceiptRepository;
    }

    @Override
    public List<ImportReceipt> listAllImportReceipts() {
        return importReceiptRepository.findAll();
    }

    @Override
    public ImportReceipt getImportReceiptById(long id) {
        if(id <= 0) {
            throw new BadNumberException("Id của đơn nhập kho phải lớn hơn 0");
        }

        return importReceiptRepository
            .findById(id)
            .orElseThrow(() -> new RecordNotFoundException("Không tìm thấy đơn nhập kho"));
    }

    @Override
    @Transactional
    public ImportReceipt saveImportReceipt(long creatorId, long orderId, ImportReceiptDto importReceiptDto) {
        User user = userService.getUserById(creatorId);
        Order order = orderService.getOrderById(orderId);

        if(!order.getStatus().equals(OrderStatus.PROCESSING.getStatus())) {
            throw new IllegalStateException("Đơn hàng đã bị hủy hoặc đã hoàn thành");
        }

        String code = itemCodeGenerator.generate();
        ImportReceipt importReceipt = importReceiptRepository.save(new ImportReceipt(code, order, user));
        AtomicReference<ImportedStatus> importedStatus = new AtomicReference<>(ImportedStatus.IMPORTED);

        importReceiptDto.getLineItems().forEach(lineItem -> {
            Variant variant = variantService.getVariantById(lineItem.getVariantId());
            /*
            Step 1: Check if imported variant is supplied in the order
                    If not, throw error
             */
            if(variantsOrderRepository.isVariantSuppliedInOrder(lineItem.getVariantId(), orderId) == 0) {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Sản phẩm " + variant.getVariantName() + " chưa được cung cấp"
                );
            }

            /*
              Step 2: Check total already imported quantity for this order.
              If total imported quantity + new imported quantity > supplied quantity, throw error
              Else, save new record to variant_import_receipts
             */
            long totalAlreadyImportedQuantity = variantsImportReceiptRepository
                .totalImportedQuantityOfVariantInOrder(lineItem.getVariantId(), orderId);
            long totalSuppliedQuantity = variantsOrderRepository
                .totalSuppliedQuantityOfVariantInOrder(lineItem.getVariantId(), orderId);
            if(totalAlreadyImportedQuantity + lineItem.getQuantity() > totalSuppliedQuantity) {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Tổng lượng nhập kho của sản phẩm " + variant.getVariantName() + " không được vượt quá số lượng được cung cấp"
                );
            }
            else if(totalAlreadyImportedQuantity + lineItem.getQuantity() < totalSuppliedQuantity) {
                importedStatus.set(ImportedStatus.PARTIAL_IMPORTED);
            }

            /*
            Step 3: Save this imported variant to the database
             */
            VariantsImportReceipt variantsImportReceipt = variantsImportReceiptRepository.save(
                new VariantsImportReceipt(
                    variant,
                    importReceipt,
                    lineItem.getQuantity()
                )
            );

            /*
            Step 4: Update inventoryQuantity and sellableQuantity of the variant
             */
            variant.setInventoryQuantity(variant.getInventoryQuantity() + lineItem.getQuantity());
            variant.setSellableQuantity(variant.getSellableQuantity() + lineItem.getQuantity());
        });

        /*
        Step 5: Update imported status of order: Order.importedStatus
         */
        order.setImportedStatus(importedStatus.get());
        if(order.getTransactionStatus().equals(TransactionStatus.PAID.getStatus()) &&
            order.getImportedStatus().equals(ImportedStatus.IMPORTED.getStatus())) {
            order.setStatus(OrderStatus.COMPLETE);
        }

        return importReceipt;
    }

    @Override
    public List<ImportReceiptResponseDto> listAllImportReceiptsByOrder(long orderId) {
        List<ImportReceiptResponseDto> response = new ArrayList<>();
        /*
        Step 1: Find all import receipts
         */
        List<ImportReceipt> importReceipts = importReceiptRepository.findAllByOrder_Id(orderId);

        /*
        Step 2: For each import receipt, find all line items,
        and build response based on those line items
         */
        importReceipts.forEach(importReceipt -> {
            List<LineItemDto> lineItems = new ArrayList<>();

            /*
            Step 2.1: Find all variants in this importReceipt
             */
            List<VariantsImportReceipt> variantsImportReceipts = variantsImportReceiptRepository
                .findAllByImportReceipt_Id(importReceipt.getId());

            /*
            Step 2.2: For each variant, find the supplied price,
            build lineItem, and add to list of line items
             */
            variantsImportReceipts.forEach(variantsImportReceipt -> {
                Variant variant = variantsImportReceipt.getVariant();
                long variantId = variant.getId();
                String variantName = variant.getVariantName();

                /*
                Find supplied price of this variant based on variantId and orderId
                 */
                double suppliedPrice = variantsOrderRepository
                    .findByVariant_IdAndOrder_Id(variantId, orderId)
                    .orElseThrow(() -> new RecordNotFoundException("Đơn hàng " + orderId + " không có sản phẩm " + variantId))
                    .getPrice();

                /*
                Build a lineItem
                 */
                LineItemDto lineItem = new LineItemDto(
                    variantsImportReceipt.getId().getVariantId(),
                    variantName,
                    suppliedPrice,
                    variantsImportReceipt.getQuantity()
                );
                lineItems.add(lineItem);
            });

            /*
            Build an object of ImportReceiptResponseDto
             */
            ImportReceiptResponseDto responseDto = new ImportReceiptResponseDto(
                importReceipt.getCode(), importReceipt.getCreatedAt(),
                importReceipt.getCreatedBy().getUsername(), lineItems
            );

            /*
            Build the response
             */
            response.add(responseDto);
        });

        return response;
    }
}
