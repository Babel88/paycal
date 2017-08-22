package com.babel88.paycal.api;

import com.babel88.paycal.models.PaymentModel;

import java.io.Serializable;

public interface ResultsViewer<T> extends Serializable {
    /**
     * This method renders a view for the payment object passed as parameter
     *
     * @param paymentModel object containing results of compuations
     */
    T forPayment(PaymentModel paymentModel);
}
