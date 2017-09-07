package com.ghacupha.paycal.api;

import com.ghacupha.paycal.models.PaymentModel;

import java.io.Serializable;

public interface ResultsViewer<T> extends Serializable {
    /**
     * This method renders a paymentModelView for the payment object passed as parameter
     *
     * @param paymentModel object containing results of compuations
     */
    T forPayment(PaymentModel paymentModel);
}
