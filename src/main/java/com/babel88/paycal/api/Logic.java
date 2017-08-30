package com.babel88.paycal.api;

import org.jetbrains.annotations.NotNull;

/**
 * Created by edwin.njeru on 09/08/2017.
 */
public interface Logic {
    void normal();

    void vatGiven();

    void contractor();

    void taxToWithhold();
    @NotNull
    void telegraphicTransfer();

    void rentalPayments();
}
