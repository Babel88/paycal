package com.babel88.paycal.view;

import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.test.AbstractJavaBeanTest;

public class ResultsOutputTest extends AbstractJavaBeanTest<ResultsViewer>{

    /**
     * This method returns an instance of the bean being tested, after being created
     * by the class extending this
     *
     * @return instance of the bean under test
     */
    @Override
    public ResultsViewer getBeanInstance() {

        return new ResultsOutput();
    }
}