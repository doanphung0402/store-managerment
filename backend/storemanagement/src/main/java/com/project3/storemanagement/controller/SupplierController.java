package com.project3.storemanagement.controller;

import com.project3.storemanagement.entities.Order;
import com.project3.storemanagement.entities.Supplier;
import com.project3.storemanagement.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/suppliers")
@CrossOrigin

public class SupplierController {
    @Autowired
    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/{id}")
    public Supplier findSupplierById(@PathVariable long id) {
        return supplierService.getSupplierById(id);
    }

    @GetMapping
    public Iterable<Supplier> findAllSuppliers() {
        return supplierService.listAllSuppliersByRecordStatus();
    }

    @PostMapping
    public Supplier createSupplier(@RequestBody @Valid Supplier supplier) {
        return supplierService.saveSupplier(supplier);
    }

    @PutMapping("/{id}")
    public Supplier updateSupplier(@PathVariable long id, @RequestBody @Valid Supplier supplier) {
        return supplierService.updateSupplier(id, supplier);
    }

    @DeleteMapping("/{id}")
    public String deleteSupplier(@PathVariable long id) {
        return supplierService.deleteSupplier(id);
    }

    @GetMapping("/{id}/orders")
    public List<Order> findAllOrdersOfSupplier(@PathVariable long id) {
        return supplierService.findAllSuppliedOrders(id);
    }
}
