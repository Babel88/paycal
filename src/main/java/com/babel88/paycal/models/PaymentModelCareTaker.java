package com.babel88.paycal.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaymentModelCareTaker implements Serializable {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private List<PaymentModelMemento> paymentModelMementos;

    public PaymentModelCareTaker() {

        paymentModelMementos =
                new ArrayList<>();

        log.debug("Payment model caretaker created by invokation of a new ArrayList \n" +
                "for the payment model memento : {}.", paymentModelMementos.toString());
    }


    public void add(PaymentModelMemento state) {

        log.debug("Adding new payment model state to memento");

        paymentModelMementos.add(state);

        log.debug("New payment model state saved at step: {}.",
                paymentModelMementos.size());
    }

    public PaymentModelMemento get(int index) {

        log.debug("Retrieving payment model state at step : {}.", index);
        return paymentModelMementos.get(index);
    }
}
