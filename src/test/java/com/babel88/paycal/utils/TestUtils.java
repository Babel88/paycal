package com.babel88.paycal.utils;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;

/**
 * Created by edwin.njeru on 29/08/2017.
 */
public abstract class TestUtils<T extends Serializable> {

    private T myBean;

    @Before
    public void setUp() throws Exception {
        this.myBean = getBeanInstance();
    }

    @Test
    public void beanIsSerializable() throws Exception {

        final byte[] serializedMyBean = SerializationUtils.serialize(myBean);

        @SuppressWarnings("unchecked") final T deserializedMyBean =
                (T) SerializationUtils.deserialize(serializedMyBean);

        assertEquals(myBean, deserializedMyBean);
    }

    @Test
    public void equalsAndHashcodeContract() throws Exception {

        EqualsVerifier.forClass(getBeanInstance().getClass())
                .usingGetClass()
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void getterAndSetterCorrectness() throws Exception {

        final BeanTester beanTester = new BeanTester();

        beanTester.testBean(getBeanInstance().getClass());
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    public abstract T getBeanInstance();

    protected BigDecimal setAccuracy(Double amount) {

        return BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_EVEN);
    }


}
