package com.project3.storemanagement.service.impl;

import com.project3.storemanagement.dto.PayOrderDto;
import com.project3.storemanagement.entities.Order;
import com.project3.storemanagement.entities.PaymentInvoice;
import com.project3.storemanagement.entities.Supplier;
import com.project3.storemanagement.entities.User;
import com.project3.storemanagement.service.OrderService;
import com.project3.storemanagement.service.PaymentInvoiceService;
import com.project3.storemanagement.service.SupplierService;
import com.project3.storemanagement.entities.*;
import com.project3.storemanagement.exception.BadNumberException;
import com.project3.storemanagement.exception.RecordNotFoundException;
import com.project3.storemanagement.repository.OrderRepository;
import com.project3.storemanagement.repository.PaymentInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentInvoiceServiceImpl implements PaymentInvoiceService {
    private final PaymentInvoiceRepository paymentInvoiceRepository;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    public PaymentInvoiceServiceImpl(PaymentInvoiceRepository paymentInvoiceRepository, OrderRepository orderRepository) {
        this.paymentInvoiceRepository = paymentInvoiceRepository;
    }

    @Override
    public List<PaymentInvoice> listAllPaymentInvoices() {
        return this.paymentInvoiceRepository.findAll();
    }

    @Override
    public PaymentInvoice getPaymentInvoiceById(long id) {
        if(id <= 0) {
            throw new BadNumberException("Id phải lớn hơn 0");
        }

        return paymentInvoiceRepository
            .findById(id)
            .orElseThrow(() -> new RecordNotFoundException("Không tìm thấy hóa đơn có id " + id));
    }

    @Override
    @Transactional
    public PaymentInvoice savePaymentInvoice(long invoiceCreatorId, long orderId, PayOrderDto payOrderDto) {
        User user = userService.getUserById(invoiceCreatorId);

        Order order = orderService.increasePaidAmount(orderId, payOrderDto.getAmount());
        Supplier supplier = supplierService.decreaseDebt(order.getSupplier().getId(), payOrderDto.getAmount());

        PaymentInvoice paymentInvoice = new PaymentInvoice(payOrderDto.getAmount(), order, user);

        return paymentInvoiceRepository.save(paymentInvoice);
    }

    @Override
    public List<PaymentInvoice> listAllPaymentInvoicesByOrder(long orderId) {
        return paymentInvoiceRepository.findAllByOrder_Id(orderId);
    }
}
