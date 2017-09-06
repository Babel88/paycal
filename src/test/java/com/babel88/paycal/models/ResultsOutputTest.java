package com.babel88.paycal.models;

import com.babel88.paycal.utils.TestUtils;
import com.babel88.paycal.view.DisplayImpl;
import org.junit.Before;

import static org.junit.Assert.*;

public class ResultsOutputTest extends TestUtils<ResultsOutput> {

    private ResultsOutput resultsOutput;
    @Before
    public void setUp() throws Exception {

        resultsOutput = new ResultsOutput(new DisplayImpl());
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