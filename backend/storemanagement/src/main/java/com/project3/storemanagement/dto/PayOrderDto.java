package com.project3.storemanagement.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PayOrderDto {
    @NotNull(message = "Tiền thanh toán không được null")
    @Min(value = 0, message = "Tiền thanh toán phải lớn hơn 0")
    Double amount;

    public double getAmount() {
        return amount;
    }
}
