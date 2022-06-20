package com.project3.storemanagement.utils.itemcodegenerator;

import com.project3.storemanagement.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("order-code-generator")
public class OrderCodeGenerator extends ItemCodeGenerator {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    protected String getPrefix() {
        return "SON";
    }

    @Override
    protected long countRecords() {
        return orderRepository.count();
    }
}
