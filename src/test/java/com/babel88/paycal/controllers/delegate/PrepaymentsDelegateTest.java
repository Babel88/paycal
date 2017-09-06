package com.babel88.paycal.controllers.delegate;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.controllers.prepayments.PrepaymentControllerImpl;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.TTArguments;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.*;

public class PrepaymentsDelegateTest {

    private PrepaymentsDelegate prepaymentsDelegate;

    private PrepaymentController prepaymentController;

    private BigDecimal totalExpnses = BigDecimal.valueOf(100000).setScale(2,RoundingMode.HALF_EVEN);

    ApplicationContext context = new ClassPathXmlApplicationContext("Beans-test.xml");

    @Before
    public void setUp() throws Exception {

        prepaymentController = (PrepaymentController) context.getBean("prepaymentController");

        prepaymentsDelegate = new PrepaymentsDelegate(new PaymentsControllerRunner() {
            @Override
            public void runCalculation() {
                //
            }

            @Override
            public DefaultPaymentModel getPaymentModel() {
                return new PaymentModel()
                        .setTotalExpense(
                                BigDecimal.valueOf(100000).setScale(2, RoundingMode.HALF_EVEN)
                        );
            }

            @Override
            public PrepaymentController getPrepaymentController() {
                PrepaymentController prepaymentController = (PrepaymentController) new PrepaymentController() {
                    @Nonnull
                    @Override
                    public @NotNull BigDecimal getPrepayment(@Nonnull BigDecimal totalExpense) {
                        return new PrepaymentControllerImpl().getPrepayment(totalExpense);
                    }

                    @Nonnull
                    @Override
                    public @NotNull Object setExpenseAmount(BigDecimal expenseAmount) {

                        this.setExpenseAmount(expenseAmount);
                        return this;
                    }
                }
                .setExpenseAmount(totalExpnses);

                return  prepaymentController;
            }

            @Override
            public TTArguments getTtArguments() {
                return new TTArguments();
            }
        });
    }

    @Test
    public void prepaymentDelegateNotNull() throws Exception {

        assertNotNull(prepaymentsDelegate);
    }

    @Test
    public void prepaymentControllerNotNull() throws Exception {

        assertNotNull(prepaymentController);
    }

    @Test
    public void prepaymentControllerWorks() throws Exception{

        PrepaymentControllerImpl prepaymentController =
                new PrepaymentControllerImpl();

        prepaymentController.setExpenseAmount(totalExpnses)
                .setPrepay(true)
                .setFeedBack(new FeedBack() {
                    @Override
                    public void printIntro() {
                        //
                    }

                    @Override
                    public void mainPrompt() {
                        //
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
                });

        //BigDecimal prepay = prepaymentController.getPrepayment(totalExpnses);
    }
}