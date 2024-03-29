package com.project3.storemanagement.controller;

import com.project3.storemanagement.service.CheckSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check-sheets")
@CrossOrigin
public class CheckSheetController {
    private final CheckSheetService checkSheetService;

    @Autowired
    public CheckSheetController(CheckSheetService checkSheetService) {
        this.checkSheetService = checkSheetService;
    }
}
