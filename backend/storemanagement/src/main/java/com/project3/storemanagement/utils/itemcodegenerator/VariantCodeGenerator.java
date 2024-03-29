package com.project3.storemanagement.utils.itemcodegenerator;

import com.project3.storemanagement.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("variant-code-generator")
public class VariantCodeGenerator extends ItemCodeGenerator {
    @Autowired
    private VariantRepository variantRepository;

    @Override
    protected String getPrefix() {
        return "PBN";
    }

    @Override
    protected long countRecords() {
        return variantRepository.count();
    }
}
