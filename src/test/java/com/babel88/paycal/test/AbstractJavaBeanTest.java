package com.babel88.paycal.test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;

public abstract class AbstractJavaBeanTest<T extends Serializable> {

    // Bean under test
    private T myBean;

    @Before
    public void setUp() throws Exception {

        this.myBean = getBeanInstance();
    }

    /**
     * This method returns an instance of the bean being tested, after being created
     * by the class extending this
     *
     * @return instance of the bean under test
     */
    public abstract T getBeanInstance();

    @Test
    public void beanIsSerializable() throws Exception {

        final byte[] serializedMyBean = SerializationUtils.serialize(myBean);

        @SuppressWarnings("unchecked")
        final T deserializedMyBean = (T) SerializationUtils.deserialize(serializedMyBean);

        assertEquals(myBean,deserializedMyBean);
    }

    @Test
    public void equalsAndHashcodeContract() throws Exception {

        EqualsVerifier.forClass(getBeanInstance().getClass())
                      .usingGetClass()
                      .suppress(Warning.STRICT_INHERITANCE,
                              Warning.NONFINAL_FIELDS,
                              Warning.REFERENCE_EQUALITY)
                      .verify();
    }

    @Test
    public void getterAndSetterCorrectness() throws Exception {

        final BeanTester beanTester = new BeanTester();

        beanTester.testBean(getBeanInstance().getClass());
    }
}
