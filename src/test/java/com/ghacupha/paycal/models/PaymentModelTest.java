package com.ghacupha.paycal.models;

import com.ghacupha.paycal.utils.TestUtils;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

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

}