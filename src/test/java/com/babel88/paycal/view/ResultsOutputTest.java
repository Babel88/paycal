package com.babel88.paycal.view;

import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.test.AbstractJavaBeanTest;

import java.math.BigDecimal;

public class ResultsOutputTest extends AbstractJavaBeanTest<ResultsViewer>{

    /**
     * This method returns an instance of the bean being tested, after being created
     * by the class extending this
     *
     * @return instance of the bean under test
     */
    @Override
    public ResultsViewer getBeanInstance() {

        return new ResultsOutput(new PaymentModelViewInterface() {
            @Override
            public void displayResults(BigDecimal total, BigDecimal vatWithheld, BigDecimal withholdingTax, BigDecimal toPrepay, BigDecimal toPayee) {

            }
        });
    }
}