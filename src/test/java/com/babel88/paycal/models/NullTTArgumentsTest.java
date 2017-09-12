package com.babel88.paycal.models;

import com.babel88.paycal.utils.TestUtils;
import static org.junit.Assert.*;
import org.junit.Before;

public class NullTTArgumentsTest extends TestUtils<NullTTArguments> {

    private NullTTArguments nullTTArguments;


    @Before
    public void setUp() throws Exception {
        nullTTArguments = new NullTTArguments();
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public NullTTArguments getBeanInstance() {

        return nullTTArguments;
    }

}