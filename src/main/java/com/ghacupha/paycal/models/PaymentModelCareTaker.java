package com.ghacupha.paycal.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PaymentModelCareTaker {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private List<PaymentModelMemento> paymentModelMementos;
    private static PaymentModelCareTaker instance = new PaymentModelCareTaker();

    public PaymentModelCareTaker() {

        paymentModelMementos =
                new ArrayList<>();

        log.debug("Payment model caretaker created by invokation of a new ArrayList \n" +
                "for the payment model memento : {}.", paymentModelMementos.toString());
    }

    public static PaymentModelCareTaker getInstance() {
        return instance;
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
