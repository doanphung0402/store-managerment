package com.project3.storemanagement.utils.itemcodegenerator;

import com.project3.storemanagement.entities.ReturnReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "return-receipt-code-generator")
public class ReturnReceiptCodeGenerator extends ItemCodeGenerator {
    @Autowired
    private ReturnReceiptRepository returnReceiptRepository;

    @Override
    protected String getPrefix() {
        return "REN";
    }

    @Override
    protected long countRecords() {
        return returnReceiptRepository.count();
    }
}
