package com.babel88.paycal.controllers.base;

import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.PrepaymentConfigurations;
import com.babel88.paycal.controllers.prepayments.PrepaymentControllerImpl;
import com.babel88.paycal.logic.SimplePrepayments;
import com.babel88.paycal.logic.base.ContractorLogic;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.RoundingMode.*;
import static org.junit.Assert.*;

public class ContractorPaymentsControllerTest {

    private ContractorPaymentsController contractorController;

    private BigDecimal invoiceAmount = BigDecimal.valueOf(116000).setScale(2, HALF_EVEN);

    @Before
    public void setUp() throws Exception {

        contractorController = new ContractorPaymentsController( new PaymentModel(),
                new Invoice(new FeedBackImpl()),
                new ContractorLogic(new PaymentParameters()),
                new PrepaymentControllerImpl(new SimplePrepayments(new Invoice(new FeedBackImpl()), new PrepaymentConfigurations()), new FeedBackImpl()),
                new ModelViewerVisitor(),
                new ModelPrecisionVisitor(),
                new ReportingVisitor(new FeedBackImpl()));

    }

    @Test
    public void updateTotalExpense() throws Exception {

        contractorController.setInvoiceAmount(invoiceAmount);

        contractorController.updateTotalExpense();

        assertEquals(invoiceAmount,contractorController.getPaymentModel().getTotalExpense());
    }

    @Test
    public void updateToPayee() throws Exception {

        contractorController.setInvoiceAmount(invoiceAmount);

        contractorController.updateToPayee();

        assertEquals(BigDecimal.valueOf(107000).setScale(2, HALF_EVEN),contractorController.getPaymentModel().getToPayee());
    }

    @Test
    public void updateWithholdingTax() throws Exception {

        contractorController.setInvoiceAmount(invoiceAmount);

        contractorController.updateWithholdingTax();

        assertEquals(BigDecimal.valueOf(3000).setScale(2, HALF_EVEN),contractorController.getPaymentModel().getWithholdingTax());
    }

    @Test
    public void updateWithholdingVat() throws Exception {

        contractorController.setInvoiceAmount(invoiceAmount);

        contractorController.updateWithholdingVat();

        assertEquals(
                BigDecimal.valueOf(6000).setScale(2, HALF_EVEN),
                contractorController.getPaymentModel().getWithholdingVat()
        );
    }

    @Test
    public void updateToPrepay() throws Exception {
    }

    @Test
    public void getPaymentModel() throws Exception {

        assertNotNull(contractorController.getPaymentModel());
    }

    @Test
    public void setPaymentModel() throws Exception {

        PaymentModel newPaymentModel = new PaymentModel();

        assertEquals(newPaymentModel,contractorController.getPaymentModel());
    }

}