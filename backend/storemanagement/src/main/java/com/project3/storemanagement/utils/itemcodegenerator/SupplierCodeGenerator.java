package com.project3.storemanagement.utils.itemcodegenerator;

import com.project3.storemanagement.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("supplier-code-generator")
public class SupplierCodeGenerator extends ItemCodeGenerator {
    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    protected String getPrefix() {
        return "NCC";
    }

    @Override
    protected long countRecords() {
        return supplierRepository.count();
    }
}
