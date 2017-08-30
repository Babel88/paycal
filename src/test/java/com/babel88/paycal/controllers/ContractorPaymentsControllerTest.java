package com.babel88.paycal.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.ReportControllers;
import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.controllers.base.ContractorPaymentsController;
import com.babel88.paycal.controllers.prepayments.PrepaymentController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;
import static org.mockito.Mockito.when;

/**
 * Created by edwin.njeru on 28/08/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ContractorPaymentsControllerTest {

    private DefaultControllers controller;

    @Mock
    private DefaultPaymentModel paymentModel;

    private InvoiceDetails invoice;
    private BigDecimal invoiceAmount;

    @Mock
    private DefaultLogic defaultLogic;

    @Mock
    private PrepaymentController prepaymentController;

    @Mock
    private ResultsViewer viewResults;

    @Mock
    private ReportControllers reportsController;

    private Boolean doAgain;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(ContractorPaymentsControllerTest.class);

        when(prepaymentController.setPrepay(false));

        doAgain = false;
        invoice = createInvoice();
        invoiceAmount = this.invoice.invoiceAmount();

        controller = new ContractorPaymentsController();
    }

    private InvoiceDetails createInvoice() {
        invoice = new InvoiceDetails() {
            @Override
            public BigDecimal invoiceAmount() {

                return BigDecimal.valueOf(116000, 2);
            }

            @Override
            public double vatAmount() {
                return 0;
            }

            @Override
            public double withHoldingTaxAmount() {
                return 0;
            }

            @Override
            public double vatRate() {
                return 0;
            }

            @Override
            public double withHoldingTaxRate() {
                return 0;
            }

            @Override
            public double withHoldingVatRate() {
                return 0;
            }

            @Override
            public String payeeName() {
                return null;
            }

            @Override
            public String getInvoiceStartDate() {
                return null;
            }

            @Override
            public String getInvoiceRefDate() {
                return null;
            }

            @Override
            public String getInvoiceEndDate() {
                return null;
            }

            @Override
            public Boolean exclusiveOfWithholdingTax() {
                return null;
            }

            @Override
            public boolean doAgain() {
                return false;
            }
        };

        return invoice;
    }

    @Test
    public void runCalculation() throws Exception {

        controller.runCalculation();

        DefaultPaymentModel results = controller.getPaymentModel();

        DefaultPaymentModel paymentModel = new DefaultPaymentModel() {
            @Override
            public Object setWithHoldingVat(BigDecimal invoiceAmount) {
                return null;
            }

            @Override
            public Object setTotalExpense(BigDecimal totalExpense) {
                return BigDecimal.valueOf(116000).setScale(2, HALF_EVEN);
            }

            @Override
            public Object setWithholdingTax(BigDecimal withholdingTax) {
                return BigDecimal.valueOf(3000).setScale(2, HALF_EVEN);
            }

            @Override
            public Object setToPrepay(BigDecimal toPrepay) {
                return BigDecimal.valueOf(0).setScale(2, HALF_EVEN);
            }

            @Override
            public Object setToPayee(BigDecimal toPayee) {
                return BigDecimal.valueOf(107000).setScale(2, HALF_EVEN);
            }

            @Override
            public BigDecimal getTotalExpense() {
                return BigDecimal.valueOf(116000).setScale(2, HALF_EVEN);
            }
        };

        Assert.assertEquals(paymentModel, results);

    }

    @Test
    public void getPaymentModel() throws Exception {
    }

}