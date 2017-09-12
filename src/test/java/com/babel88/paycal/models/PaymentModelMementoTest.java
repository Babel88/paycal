package com.babel88.paycal.models;

import com.babel88.paycal.utils.TestUtils;
import static org.junit.Assert.*;
import org.junit.Before;

public class PaymentModelMementoTest extends TestUtils<PaymentModelMemento> {

    private PaymentModelMemento paymentModelMemento;

    @Before
    public void setUp() throws Exception {

        paymentModelMemento = new PaymentModelMemento(new PaymentModel());
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public PaymentModelMemento getBeanInstance() {

        return paymentModelMemento;
    }

    @Override
    public void getterAndSetterCorrectness() throws Exception {
        // To run this manually
    }
}