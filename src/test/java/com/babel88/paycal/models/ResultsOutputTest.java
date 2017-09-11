package com.babel88.paycal.models;

import com.babel88.paycal.utils.TestUtils;
import org.junit.Before;

public class ResultsOutputTest extends TestUtils<ResultsOutput> {

    private ResultsOutput resultsOutput;
    @Before
    public void setUp() throws Exception {

        resultsOutput = new ResultsOutput();
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public ResultsOutput getBeanInstance() {
        return resultsOutput;
    }

}