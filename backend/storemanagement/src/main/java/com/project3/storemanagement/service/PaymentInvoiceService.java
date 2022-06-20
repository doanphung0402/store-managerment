package com.project3.storemanagement.service;

import com.project3.storemanagement.entities.PaymentInvoice;
import com.project3.storemanagement.dto.PayOrderDto;

import java.util.List;

public interface PaymentInvoiceService {
    PaymentInvoice getPaymentInvoiceById(long id);

    List<PaymentInvoice> listAllPaymentInvoices();

    PaymentInvoice savePaymentInvoice(long invoiceCreatorId, long orderId, PayOrderDto payOrderDto);

    List<PaymentInvoice> listAllPaymentInvoicesByOrder(long orderId);
}
