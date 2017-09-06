package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.PartialTaxDetails;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.logic.PartialTaxPaymentLogic;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.controllers.prepayments.PrepaymentControllerImpl;
import com.babel88.paycal.logic.base.DefaultPartialTaxPaymentLogic;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.ResultsOutput;
import com.babel88.paycal.view.DisplayImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.RoundingMode.*;
import static org.junit.Assert.*;

public class DefaultPartialTaxPaymentControllerTest {

    private DefaultPartialTaxPaymentController partialTaxPaymentController;
    private PartialTaxDetails partialTaxDetails;
    private PaymentModel paymentModel;
    private PartialTaxPaymentLogic partialTaxPaymentLogic;
    private PrepaymentController prepaymentController;
    private FeedBack feedBack;

    @Before
    public void setUp() throws Exception {

        partialTaxDetails = new PartialTaxDetails() {
            @Override
            public BigDecimal invoiceAmount() {

                return BigDecimal.valueOf(116000).setScale(2, HALF_EVEN);
            }

            @Override
            public double vatAmount() {
                return 16000.00;
            }

            /**
             * Queries the user whether they would like to repeat the computation routine
             *
             * @return whether or not to repeat
             */
            @Override
            public boolean doAgain() {
                return false;
            }
        };

        feedBack = new FeedBack() {
            @Override
            public void printIntro() {

            }

            @Override
            public void mainPrompt() {

            }

            @Override
            public void payeeName() {

            }

            @Override
            public void vatRate() {

            }

            @Override
            public void withHoldingTaxRate() {

            }

            @Override
            public void initialMenu() {

            }

            @Override
            public void invoiceAmount() {

            }

            @Override
            public void vatAmount() {

            }

            @Override
            public void withHoldingTaxAmount() {

            }

            @Override
            public void dateInfo(String date) {

            }

            @Override
            public void withHoldingVatRate() {

            }

            @Override
            public Boolean printReport() {
                return null;
            }
        };

        paymentModel = new PaymentModel();

        partialTaxPaymentLogic = new DefaultPartialTaxPaymentLogic()
        .setPaymentParameters(new PaymentParameters());

        prepaymentController = new PrepaymentControllerImpl()
        .setExpenseAmount(BigDecimal.valueOf(116000).setScale(2,HALF_EVEN))
        .setPrepaymentAmount(BigDecimal.valueOf(23000).setScale(2,HALF_EVEN));

        partialTaxPaymentController = new DefaultPartialTaxPaymentController();
        partialTaxPaymentController.setPartialTaxDetails(partialTaxDetails)
        .setPaymentModel(paymentModel)
        .setPartialTaxPaymentLogic(partialTaxPaymentLogic)
        .setPrepaymentController(prepaymentController)
        .setResultsViewer(new ResultsOutput(new DisplayImpl()));
    }

    @Test
    public void runCalculation() throws Exception {

        partialTaxPaymentController.runCalculation();

    }
    @Test
    public void withholdingVatIsCorrect() throws Exception {

        partialTaxPaymentController.runCalculation();

        assertEquals(BigDecimal.valueOf(6000).setScale(2,HALF_EVEN),
                partialTaxPaymentController.getPaymentModel().getWithholdingVat()
        );

    }

    @Test
    public void withholdingTaxIsZero() throws Exception {

        partialTaxPaymentController.runCalculation();

        assertEquals(BigDecimal.valueOf(0).setScale(2,HALF_EVEN),
                partialTaxPaymentController.getPaymentModel().getWithholdingTax()
        );

    }

    @Test
    public void amountPayableToPayeeIsCorrect() throws Exception {

        partialTaxPaymentController.runCalculation();

        assertEquals(BigDecimal.valueOf(110000).setScale(2,HALF_EVEN),
                partialTaxPaymentController.getPaymentModel().getToPayee()
        );

    }

    @Test
    public void totalExpensesIsCorrect() throws Exception {

        partialTaxPaymentController.runCalculation();

        assertEquals(BigDecimal.valueOf(116000).setScale(2,HALF_EVEN),
                partialTaxPaymentController.getPaymentModel().getTotalExpense()
        );

    }

    @Test
    public void updateWithholdingVat() throws Exception {

        DefaultPartialTaxPaymentLogic logic=
                (DefaultPartialTaxPaymentLogic) partialTaxPaymentController
                .getPartialTaxPaymentLogic();
        logic.setPaymentParameters(new PaymentParameters());

        partialTaxPaymentController
                .setPartialTaxPaymentLogic(logic)
                .setVatAmount(BigDecimal.valueOf(16000))
                .updateWithholdingVat();

        assertEquals(BigDecimal.valueOf(6000).setScale(2,HALF_EVEN),
                partialTaxPaymentController.getPaymentModel().getWithholdingVat()
        );
    }

    @Test
    public void updateTotalExpense() throws Exception {
        DefaultPartialTaxPaymentLogic logic=
                (DefaultPartialTaxPaymentLogic) partialTaxPaymentController
                        .getPartialTaxPaymentLogic();
        logic.setPaymentParameters(new PaymentParameters());

        partialTaxPaymentController
                .setPartialTaxPaymentLogic(logic)
                .setInvoiceAmount(BigDecimal.valueOf(116000))
                .setVatAmount(BigDecimal.valueOf(16000))
                .updateTotalExpense();

        assertEquals(BigDecimal.valueOf(116000).setScale(2,HALF_EVEN),
                partialTaxPaymentController.getPaymentModel().getTotalExpense()
        );

    }

    @Test
    public void updateToPayee() throws Exception {

        partialTaxPaymentController
                .setInvoiceAmount(BigDecimal.valueOf(116000))
                .setVatAmount(BigDecimal.valueOf(16000))
                .updateTotalExpense();

        assertEquals(BigDecimal.valueOf(110000).setScale(2,HALF_EVEN),
                partialTaxPaymentController.getPaymentModel().getToPayee()
        );
    }

    @Test
    public void updateWithholdingTax() throws Exception {
        DefaultPartialTaxPaymentLogic logic=
                (DefaultPartialTaxPaymentLogic) partialTaxPaymentController
                        .getPartialTaxPaymentLogic();
        logic.setPaymentParameters(new PaymentParameters());

        partialTaxPaymentController
                .setPartialTaxPaymentLogic(logic)
                .setInvoiceAmount(BigDecimal.valueOf(116000))
                .setVatAmount(BigDecimal.valueOf(16000))
                .updateWithholdingTax();

        assertEquals(BigDecimal.valueOf(0).setScale(2,HALF_EVEN),
                partialTaxPaymentController.getPaymentModel().getWithholdingTax()
        );

    }

    @Test
    public void getPaymentModel() throws Exception {

        assertNotNull(partialTaxPaymentController.getPaymentModel());
    }

    @Test
    public void getPrepaymentController() throws Exception {

        assertNotNull(partialTaxPaymentController.getPrepaymentController());
    }

    @Test
    public void getTtArguments() throws Exception {

        assertNull(partialTaxPaymentController.getTtArguments());
    }

}