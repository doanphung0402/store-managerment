package com.project3.storemanagement.controller;

import com.project3.storemanagement.entities.ImportReceipt;
import com.project3.storemanagement.service.ImportReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/import-receipts")
@CrossOrigin
public class ImportReceiptController {
    private final ImportReceiptService importReceiptService;

    @Autowired
    public ImportReceiptController(ImportReceiptService importReceiptService) {
        this.importReceiptService = importReceiptService;
    }

    @GetMapping("/{id}")
    public ImportReceipt findImportReceiptById(@PathVariable long id){
        return importReceiptService.getImportReceiptById(id);
    }

    @GetMapping
    public List<ImportReceipt> findAllImportReceipts(){
        return importReceiptService.listAllImportReceipts();
    }
}
