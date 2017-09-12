package com.babel88.paycal.models;

import com.babel88.paycal.utils.TestUtils;
import static org.junit.Assert.*;
import org.junit.Before;

public class PaymentModelCareTakerTest extends TestUtils<PaymentModelCareTaker> {

    private PaymentModelCareTaker paymentModelCareTaker;

    @Before
    public void setUp() throws Exception {

        paymentModelCareTaker = new PaymentModelCareTaker();
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public PaymentModelCareTaker getBeanInstance() {
        return paymentModelCareTaker;
    }

    @Override
    public void getterAndSetterCorrectness() throws Exception {
        super.getterAndSetterCorrectness();
    }
}