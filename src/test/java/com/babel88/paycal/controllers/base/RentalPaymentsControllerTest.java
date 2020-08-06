package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.api.view.Visitor;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import com.babel88.paycal.controllers.prepayments.PrepaymentControllerImpl;
import com.babel88.paycal.logic.base.RentalPaymentLogic;
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
import java.math.RoundingMode;

import static java.math.RoundingMode.*;
import static org.junit.Assert.*;

public class RentalPaymentsControllerTest extends TestUtils<RentalPaymentsController> {

    private RentalPaymentsController rentalPaymentsController;
    private PrepaymentController prepaymentController;
    private RentalPaymentLogic rentalPaymentLogic;
    private PaymentModel paymentModel;
    private InvoiceDetails invoiceDetails;
    private ModelViewerVisitor modelViewerVisitor;
    private Visitor modelPrecisionVisitor;
    private ReportingVisitor reportingVisitor;
    private PaymentAdvice paymentAdvice;
    private FeedBack feedBack;
    private PaymentReportDelegate paymentReportDelegate;
    private PrepaymentsDelegate prepaymentsDelegate;
    private ModelViewerDelegate modelViewerDelegate;
    @Before
    public void setUp() throws Exception {

        prepaymentController = new PrepaymentController() {
            @Nonnull
            @Override
            public @NotNull BigDecimal getPrepayment(@Nonnull BigDecimal totalExpense) {
                return BigDecimal.valueOf(22932.04);
            }

            @Nonnull
            @Override
            public @NotNull Object setExpenseAmount(BigDecimal expenseAmount) {
                return new PrepaymentControllerImpl();
            }
        };

        invoiceDetails = new InvoiceDetails() {
            @Override
            public BigDecimal invoiceAmount() {

                return bd(45365.56);
            }

            @Override
            public double vatAmount() {
                return 0.00;
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

            // To cancel report printing during test
            @Override
            public Boolean printReport() {
                return false;
            }
        };

        rentalPaymentLogic = new RentalPaymentLogic(new PaymentParameters());
        paymentModel = new PaymentModel();
        modelPrecisionVisitor = new ModelPrecisionVisitor();
        modelViewerVisitor = new ModelViewerVisitor();
        reportingVisitor = new ReportingVisitor();
        paymentAdvice = new PaymentAdvice();
        paymentReportDelegate = new PaymentReportDelegate(reportingVisitor);

        paymentReportDelegate.setFeedBack(feedBack);
        reportingVisitor.setPaymentReportDelegate(paymentReportDelegate);

        modelViewerDelegate = new ModelViewerDelegate(modelViewerVisitor);

        modelViewerVisitor.setModelViewerDelegate(modelViewerDelegate);

        rentalPaymentsController = new RentalPaymentsController()
                .setPaymentModel(paymentModel)
                .setInvoiceDetails(invoiceDetails)
                .setInvoiceAmount(BigDecimal.valueOf(45365.56))
                .setModelPrecisionVisitor(modelPrecisionVisitor)
                .setModelViewerVisitor(modelViewerVisitor)
                .setReportingVisitor(reportingVisitor)
                .setPrepaymentController(prepaymentController)
                .setRentalPaymentLogic(rentalPaymentLogic)
        ;

        prepaymentsDelegate = new PrepaymentsDelegate(rentalPaymentsController);

        rentalPaymentsController.setPrepaymentsDelegate(prepaymentsDelegate);

    }

    @Test
    public void runCalculation() throws Exception {

        rentalPaymentsController.runCalculation();

        BigDecimal withholdingTax = rentalPaymentsController.getPaymentModel().getWithholdingTax();
        BigDecimal withholdingVat = rentalPaymentsController.getPaymentModel().getWithholdingVat();
        BigDecimal prepayment = rentalPaymentsController.getPaymentModel().getToPrepay();
        BigDecimal totalExpense = rentalPaymentsController.getPaymentModel().getTotalExpense();
        BigDecimal toPayee = rentalPaymentsController.getPaymentModel().getToPayee();

        assertEquals(bd(3980.00),withholdingTax);
        assertEquals(bd(796.00),withholdingVat);
        assertEquals(bd(22932.04),prepayment);
        assertEquals(bd(22433.51),totalExpense);
        assertEquals(bd(40589.55),toPayee);


    }

    @Test
    public void updateTotalExpense() throws Exception {

        rentalPaymentsController.updateTotalExpense();

        BigDecimal totalExpense = rentalPaymentsController.getPaymentModel().getTotalExpense();

        assertEquals(bd(45365.56),totalExpense);
    }

    @Test
    public void updateToPayee() throws Exception {

        rentalPaymentsController.updateToPayee();

        BigDecimal totalExpense = rentalPaymentsController.getPaymentModel().getToPayee();

        assertEquals(bd(40590.23),totalExpense);
    }

    @Test
    public void updateWithholdingTax() throws Exception {

        rentalPaymentsController.updateWithholdingTax();

        BigDecimal withholdingTax = rentalPaymentsController.getPaymentModel().getWithholdingTax();

        assertEquals(bd(3979.44),withholdingTax);
    }

    @Test
    public void updateWithholdingVat() throws Exception {

        rentalPaymentsController.updateWithholdingVat();

        BigDecimal withholdingVat = rentalPaymentsController.getPaymentModel().getWithholdingVat();

        assertEquals(bd(795.89),withholdingVat);
    }

    @Test
    public void getPaymentModel() throws Exception {

        assertNotNull(rentalPaymentsController.getPaymentModel());
    }

    @Test
    public void setRentalPaymentLogic() throws Exception {

        RentalPaymentLogic logic = new RentalPaymentLogic(new PaymentParameters());
        rentalPaymentsController.setRentalPaymentLogic(logic);

        assertEquals(logic,rentalPaymentsController.getRentalPaymentLogic());
    }

    public static BigDecimal bd(Double value){

        return BigDecimal.valueOf(value).setScale(2,HALF_EVEN);
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public RentalPaymentsController getBeanInstance() {

        return rentalPaymentsController;
    }
}