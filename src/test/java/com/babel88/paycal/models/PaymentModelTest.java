package com.babel88.paycal.models;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.utils.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

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

    @Test
    @Override
    public void equalsAndHashcodeContract() throws Exception {

        EqualsVerifier.forClass(getBeanInstance().getClass())
                .usingGetClass()
                //.withPrefabValues(PaymentModel.getInstance())
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify();
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

    @Override
    public void getterAndSetterCorrectness() throws Exception {
        super.getterAndSetterCorrectness();
    }
}