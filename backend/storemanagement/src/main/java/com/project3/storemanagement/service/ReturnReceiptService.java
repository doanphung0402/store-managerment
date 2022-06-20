package com.project3.storemanagement.service;

import com.project3.storemanagement.entities.ReturnReceipt;
import com.project3.storemanagement.dto.ReturnReceiptDto;
import com.project3.storemanagement.dto.ReturnReceiptResponseDto;

import java.util.List;

public interface ReturnReceiptService {
    ReturnReceipt saveReturnReceipt(Long creatorId, long orderId, ReturnReceiptDto returnReceiptDto);

    List<ReturnReceiptResponseDto> listAllReturnReceiptsByOrder(long orderId);
}
