package com.babel88.paycal.controllers;

import com.babel88.paycal.api.logic.TypicalPayments;
import com.babel88.paycal.logic.GeneralPayments;
import com.babel88.paycal.models.PaymentModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;
import static org.mockito.Mockito.when;

public class TypicalPaymentsControllerTest {

    private BigDecimal invoiceAmount;

    private TypicalPaymentsController controller;

    private TypicalPayments typicalPayments;

    private PaymentModel paymentModel;

    @Mock
    private PrepaymentController prepaymentController;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this.getClass());

        invoiceAmount = BigDecimal.valueOf(116000).setScale(2, HALF_EVEN);

        controller = new TypicalPaymentsController();

        paymentModel = new PaymentModel();

        //prepaymentController = new PrepaymentController(invoiceAmount);

        typicalPayments = new TypicalPayments() {
            @Override
            public BigDecimal getInvoiceAmount() {

                return createBigDecimal(116000.00);
            }

            @Override
            public GeneralPayments setInvoiceAmount(BigDecimal invoiceAmount) {
                return this;
            }

            @Override
            public BigDecimal calculateWithholdingVat(BigDecimal invoiceAmount) {
                return createBigDecimal(6000.00);
            }

            @Override
            public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount) {
                return createBigDecimal(116000.00);
            }

            @Override
            public BigDecimal calculatePayableToVendor(BigDecimal invoiceAmount) {
                return createBigDecimal(110000.00);
            }

            @Override
            public BigDecimal calculateAmountBeforeTax(BigDecimal invoiceAmount) {
                return createBigDecimal(100000.00);
            }
        };
    }

    @Test
    public void runCalculation() throws Exception {

        controller.runCalculation(invoiceAmount);

        //BigDecimal expenseAmount = controller.getPaymentModel().getTotal();

        //assertEquals(createBigDecimal(116000.00),expenseAmount);
    }

    private BigDecimal createBigDecimal(Double amount){

        return BigDecimal.valueOf(amount).setScale(2, HALF_EVEN);
    }

    private void setPrepaymentControllerBehaviour(){

        //when(prepaymentController.setPrepay()).thenReturn("yes");
        when(prepaymentController.getPrepayment(invoiceAmount)).thenReturn(BigDecimal.ZERO);
    }

}