package com.ghacupha.paycal.api.controllers;

import com.ghacupha.paycal.api.DefaultPaymentModel;
import com.ghacupha.paycal.models.TTArguments;

import java.io.Serializable;

public interface TTController extends PaymentsControllerRunner, Serializable {

    DefaultPaymentModel updateTotalExpense(TTArguments ttArguments);


    DefaultPaymentModel updateToPayee(TTArguments ttArguments);


    DefaultPaymentModel updateWithholdingTax(TTArguments ttArguments);


    DefaultPaymentModel updateWithholdingVat(TTArguments ttArguments);


    void updateToPrepay(TTArguments ttArguments);

    @Override
    default void runCalculation() {
    }

    ;

    void setTtArguments(TTArguments ttArguments);
}
