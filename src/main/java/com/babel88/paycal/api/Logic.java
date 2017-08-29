package com.babel88.paycal.api;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 09/08/2017.
 */
public interface Logic {
    void normal(BigDecimal InvoiceAmount);

    void vatGiven();

    void contractor();

    void taxToWithhold();
    @NotNull
    void telegraphicTransfer();

    void rentalPayments();
}
