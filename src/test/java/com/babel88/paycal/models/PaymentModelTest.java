package com.babel88.paycal.models;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.utils.TestUtils;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by edwin.njeru on 31/08/2017.
 */
public class PaymentModelTest extends TestUtils<PaymentModel> {

    private PaymentModel paymentModel;
    @Before
    public void setUp() throws Exception {

        paymentModel = new PaymentModel();
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public PaymentModel getBeanInstance() {
        return paymentModel;
    }

}