package com.babel88.paycal.test;

import com.babel88.paycal.config.PaymentParametersTest;
import com.babel88.paycal.controllers.TypicalPaymentsControllerTest;
import com.babel88.paycal.logic.*;
import com.babel88.paycal.view.ResultsOutputTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PaymentParametersTest.class,
        TypicalPaymentsControllerTest.class,
        AbstractPrepaymentTest.class,
        ContractorPaymentsTest.class,
        ForeignPaymentsTest.class,
        PrepayableTest.class,
        TypicalPaymentTest.class,
        TypicalWithholdingTaxPaymentTest.class,
        ResultsOutputTest.class
})
public class PaycalTestSuite {
}
