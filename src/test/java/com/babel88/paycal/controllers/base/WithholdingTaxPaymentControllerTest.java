package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.controllers.prepayments.PrepaymentControllerImpl;
import com.babel88.paycal.logic.base.DefaultTypicalWithholdingTaxPayment;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.utils.TestUtils;
import com.babel88.paycal.view.ModelPrecisionVisitor;
import com.babel88.paycal.view.ModelViewerDelegate;
import com.babel88.paycal.view.ModelViewerVisitor;
import com.babel88.paycal.view.PaymentReportDelegate;
import com.babel88.paycal.view.ReportingVisitor;
import com.babel88.paycal.view.reporting.PaymentAdvice;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.Assert.*;

public class WithholdingTaxPaymentControllerTest extends TestUtils<WithholdingTaxPaymentController> {

    private WithholdingTaxPaymentController withholdingTaxPaymentController;


    @Before
    public void setUp() throws Exception {

        withholdingTaxPaymentController = new WithholdingTaxPaymentController();
        InvoiceDetails invoiceDetails = new InvoiceDetails() {
            @Override
            public BigDecimal invoiceAmount() {

                return bd(45365.56);
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

        PrepaymentController prepaymentController =
                new PrepaymentController() {
                    @Nonnull
                    @Override
                    public @NotNull BigDecimal getPrepayment(@Nonnull BigDecimal totalExpense) {

                        return bd(22932.04);
                    }

                    @Nonnull
                    @Override
                    public @NotNull Object setExpenseAmount(BigDecimal expenseAmount) {
                        return new PrepaymentControllerImpl();
                    }
                };

        ReportingVisitor reportingVisitor =
                new ReportingVisitor();



        withholdingTaxPaymentController.setDoAgain(false)
                .setInvoiceAmount(bd(45365.56))
                .setInvoiceDetails(invoiceDetails)
                .setModelPrecisionVisitor(new ModelPrecisionVisitor())
                .setPaymentModel(new PaymentModel())
                .setPrepaymentController(prepaymentController)
                .setModelViewerVisitor(new ModelViewerVisitor())
                ;

        PaymentAdvice paymentAdvice = new PaymentAdvice();
        paymentAdvice.setPrintAdvice(false);

        reportingVisitor
                .setPaymentModel(withholdingTaxPaymentController.getPaymentModel())
                .setPaymentAdvice(paymentAdvice)
                ;
        PaymentReportDelegate paymentReportDelegate
                = new PaymentReportDelegate(reportingVisitor);

        paymentReportDelegate.setFeedBack(
                new FeedBack() {
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
                        return false;
                    }
                }
        );

        reportingVisitor.setPaymentReportDelegate(paymentReportDelegate);

        DefaultTypicalWithholdingTaxPayment defaultTypicalWithholdingTaxPayment =
                new DefaultTypicalWithholdingTaxPayment();

        defaultTypicalWithholdingTaxPayment.setPaymentParameters(new PaymentParameters());

        PrepaymentsDelegate prepaymentsDelegate =
                new PrepaymentsDelegate(withholdingTaxPaymentController);

        ModelViewerVisitor modelViewerVisitor =
                new ModelViewerVisitor();

        modelViewerVisitor
                .setPaymentModel((PaymentModel) withholdingTaxPaymentController.getPaymentModel())
                ;
        ModelViewerDelegate modelViewerDelegate =
                new ModelViewerDelegate(modelViewerVisitor);

        modelViewerVisitor
                .setModelViewerDelegate(modelViewerDelegate);

        withholdingTaxPaymentController
                .setReportingVisitor(reportingVisitor)
                .setWithholdingTaxPayments(defaultTypicalWithholdingTaxPayment)
                .setPrepaymentsDelegate(prepaymentsDelegate)
                .setModelViewerVisitor(modelViewerVisitor)
        ;
    }

    @Test
    public void runCalculation() throws Exception {

        withholdingTaxPaymentController.runCalculation();

        BigDecimal withholdingTax = withholdingTaxPaymentController.getPaymentModel().getWithholdingTax();
        BigDecimal withholdingVat = withholdingTaxPaymentController.getPaymentModel().getWithholdingVat();
        BigDecimal prepayment = withholdingTaxPaymentController.getPaymentModel().getToPrepay();
        BigDecimal totalExpense = withholdingTaxPaymentController.getPaymentModel().getTotalExpense();
        BigDecimal toPayee = withholdingTaxPaymentController.getPaymentModel().getToPayee();

        assertEquals(bd(1956.00),withholdingTax);
        assertEquals(bd(2347.00),withholdingVat);
        assertEquals(bd(22932.04),prepayment);
        assertEquals(bd(22433.51),totalExpense);
        assertEquals(bd(41062.55),toPayee);
    }

    @Test
    public void updateTotalExpense() throws Exception {

        withholdingTaxPaymentController.updateTotalExpense();

        BigDecimal totalExpense = withholdingTaxPaymentController.getPaymentModel().getTotalExpense();

        assertEquals(bd(45365.56),totalExpense);
    }

    @Test
    public void updateToPayee() throws Exception {

        withholdingTaxPaymentController.updateToPayee();

        BigDecimal totalExpense = withholdingTaxPaymentController.getPaymentModel().getToPayee();

        assertEquals(bd(41063.66),totalExpense);
    }

    @Test
    public void updateWithholdingTax() throws Exception {

        withholdingTaxPaymentController.updateWithholdingTax();

        BigDecimal withholdingTax = withholdingTaxPaymentController.getPaymentModel().getWithholdingTax();

        assertEquals(bd(1955.41),withholdingTax);
    }

    @Test
    public void updateWithholdingVat() throws Exception {

        withholdingTaxPaymentController.updateWithholdingVat();

        BigDecimal withholdingVat = withholdingTaxPaymentController.getPaymentModel().getWithholdingVat();

        assertEquals(bd(2346.49),withholdingVat);
    }

    @Test
    public void getPaymentModel() throws Exception {

        assertNotNull(withholdingTaxPaymentController.getPaymentModel());
    }

    protected BigDecimal bd(Double value){

        return BigDecimal.valueOf(value).setScale(2,HALF_EVEN);
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public WithholdingTaxPaymentController getBeanInstance() {

        return withholdingTaxPaymentController;
    }
}