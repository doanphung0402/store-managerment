package com.project3.storemanagement.service;

import com.project3.storemanagement.entities.ImportReceipt;
import com.project3.storemanagement.dto.ImportReceiptDto;
import com.project3.storemanagement.dto.ImportReceiptResponseDto;

import java.util.List;

public interface ImportReceiptService {
    List<ImportReceipt> listAllImportReceipts();

    ImportReceipt getImportReceiptById(long id);

    ImportReceipt saveImportReceipt(long creatorId, long orderId, ImportReceiptDto importReceiptDto);

    List<ImportReceiptResponseDto> listAllImportReceiptsByOrder(long orderId);
}
