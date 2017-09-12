package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.logic.ExclusiveImportedServiceLogic;
import com.babel88.paycal.api.logic.InclusiveImportedServiceLogic;
import com.babel88.paycal.api.view.Visitor;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.logic.base.ExclusiveImportedServiceLogicImpl;
import com.babel88.paycal.logic.base.InclusiveImportedServiceLogicImpl;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.TTArguments;
import com.babel88.paycal.view.ModelViewerVisitor;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class ExclusiveTTControllerImplTest {

    private TTControllerImpl ttController;

    @Mock
    private InvoiceDetails invoiceDetails;

    // Stateless classes no need to mock
    private InclusiveImportedServiceLogic inclusiveImportedServiceLogic;
    private ExclusiveImportedServiceLogic exclusiveImportedServiceLogic;

    @Mock
    private Visitor modelViewerVisitor;

    @Mock
    private Visitor reportingVisitor;

    @Mock
    private TTArguments ttArguments;

    @Mock
    private PrepaymentsDelegate prepaymentsDelegate;

    @Mock
    private PrepaymentController prepaymentController;


    @Before
    public void setUp() throws Exception {

        inclusiveImportedServiceLogic  = new InclusiveImportedServiceLogicImpl();
        exclusiveImportedServiceLogic = new ExclusiveImportedServiceLogicImpl();

        when(invoiceDetails.invoiceAmount()).thenReturn(bd(45365.56));
        when(invoiceDetails.vatRate()).thenReturn(16.00);
        when(invoiceDetails.withHoldingVatRate()).thenReturn(16.00);
        when(invoiceDetails.withHoldingTaxRate()).thenReturn(20.00);
        when(invoiceDetails.doAgain()).thenReturn(false);
        when(invoiceDetails.exclusiveOfWithholdingTax()).thenReturn(true);

        when(ttArguments.getReverseVatRate()).thenReturn(bd(0.16));
        when(ttArguments.getWithholdingTaxRate()).thenReturn(bd(0.20));
        when(ttArguments.getInvoiceAmount()).thenReturn(bd(45365.56));
        when(ttArguments.getTaxExclusionPolicy()).thenReturn(true);

        ttController = new TTControllerImpl(invoiceDetails);

        ttController
                .setPaymentModel(new PaymentModel())
                .setExclusiveImportedServiceLogic(exclusiveImportedServiceLogic)
                .setInclusiveImportedServiceLogic(inclusiveImportedServiceLogic)
                .setModelViewerVisitor(modelViewerVisitor)
                .setReportingVisitor(reportingVisitor)
                .setPrepaymentController(prepaymentController)
                .setPrepaymentsDelegate(prepaymentsDelegate)
                .setTtArguments(ttArguments);
    }

    @Test
    public void runCalculationWithholdingTax() throws Exception {

        ttController.runCalculation();

        BigDecimal withholdingTax = ttController.getPaymentModel().getWithholdingTax();

        assertEquals(bd(11341.39),withholdingTax);
    }

    @Test
    public void runCalculationWithholdingVat() throws Exception {

            ttController.runCalculation();
            BigDecimal withholdingVat = ttController.getPaymentModel().getWithholdingVat();

            assertEquals(bd(9073.11),withholdingVat);
    }

    /*@Test
    public void runCalculationPrepayment() throws Exception {

        ttController.runCalculation();

        BigDecimal prepayment = ttController.getPaymentModel().getToPrepay();

        assertEquals(bd(22932.04),prepayment);
    }*/

    @Test
    public void runCalculationTotalExpenses() throws Exception {

        ttController.runCalculation();

        BigDecimal totalExpense = ttController.getPaymentModel().getTotalExpense();

        assertEquals(bd(65780.06),totalExpense);
    }

    @Test
    public void runCalculationToPayee() throws Exception {

        ttController.runCalculation();

        BigDecimal toPayee = ttController.getPaymentModel().getToPayee();

        assertEquals(bd(45365.56),toPayee);
    }

    protected BigDecimal bd(Double value){

        return BigDecimal.valueOf(value).setScale(2,HALF_EVEN);
    }

}