package com.babel88.paycal.api.controllers;

import com.babel88.paycal.models.TTArguments;
import org.jetbrains.annotations.NotNull;

@Deprecated
public interface TTController extends PaymentsControllerRunner, DefaultControllers {
    @Override
    void updateTotalExpense();

    @Override
    void updateToPayee();

    @Override
    void updateWithholdingTax();

    @Override
    void updateWithholdingVat();

    @Override
    void updateToPrepay();

    @Override
    default void runCalculation(){};

    void setTtArguments(TTArguments ttArguments);
}
