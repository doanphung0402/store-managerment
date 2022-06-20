package com.project3.storemanagement.controller;

import com.project3.storemanagement.entities.PaymentInvoice;
import com.project3.storemanagement.service.PaymentInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-invoices")
@CrossOrigin
public class PaymentInvoiceController {
    private final PaymentInvoiceService paymentInvoiceService;

    @Autowired
    public PaymentInvoiceController(PaymentInvoiceService paymentInvoiceService) {
        this.paymentInvoiceService = paymentInvoiceService;
    }

    @GetMapping("/{id}")
    public PaymentInvoice findPaymentInvoiceById(@PathVariable long id){
        return paymentInvoiceService.getPaymentInvoiceById(id);
    }

    @GetMapping
    public List<PaymentInvoice> findAllPaymentInvoices(){
        return paymentInvoiceService.listAllPaymentInvoices();
    }
}
