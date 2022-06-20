package com.project3.storemanagement.service;

import com.project3.storemanagement.entities.Order;
import com.project3.storemanagement.entities.Supplier;

import java.util.List;

public interface SupplierService {

    Iterable<Supplier> listAllSuppliers();

    Iterable<Supplier> listAllSuppliersByRecordStatus();

    Supplier getSupplierById(Long id);

    Supplier saveSupplier(Supplier supplier);

    Supplier updateSupplier(long id, Supplier supplier);

    String deleteSupplier(Long id);

    Supplier decreaseDebt(long supplierId, double offset);

    Supplier increaseDebt(long supplierId, double offset);

    List<Order> findAllSuppliedOrders(long supplierId);
}
