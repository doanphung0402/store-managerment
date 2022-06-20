package com.project3.storemanagement.repository;

import com.project3.storemanagement.entities.PaymentInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentInvoiceRepository extends JpaRepository<PaymentInvoice, Long> {
    List<PaymentInvoice> findAllByOrder_Id(long orderId);
}