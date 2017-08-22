package com.babel88.paycal.api.logic;

import com.babel88.paycal.api.PaycalProviders;

@FunctionalInterface
public interface PaymentTypeProviders extends PaycalProviders {

    /**
     * This method exposes an internal map of "live" objects for handling
     * business logic, which can be accessed using the name of the object
     * as a string
     *
     * @param paymentTypeString name of the business logic
     * @return business logic provider(of the type AbstractPaymentLogic")
     */
    Object getPaymentType(String paymentTypeString);
}
