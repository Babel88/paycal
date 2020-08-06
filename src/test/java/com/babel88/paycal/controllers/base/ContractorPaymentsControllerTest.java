package com.babel88.paycal.controllers.base;

import com.babel88.paycal.PaycalApp;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.logic.base.ContractorLogic;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.view.FeedBackImpl;
import com.babel88.paycal.view.Invoice;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.babel88.paycal.models.AppConstants.SYSTEM_VAT_RATE;
import static com.babel88.paycal.models.AppConstants.SYSTEM_WITHHOLDING_VAT_RATE;
import static java.math.RoundingMode.*;
import static org.junit.Assert.*;

public class ContractorPaymentsControllerTest {

    private ContractorPaymentsController contractorController;

    private BigDecimal invoiceAmount = BigDecimal.valueOf(116000).setScale(2, HALF_EVEN);

    @Before
    public void setUp() throws Exception {

        contractorController = new ContractorPaymentsController();
        contractorController.setContractorLogic(new ContractorLogic(new PaymentParameters()));
        contractorController.setPaymentModel(new PaymentModel());

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
                BigDecimal.valueOf(invoiceAmount.divide(SYSTEM_VAT_RATE).multiply(SYSTEM_WITHHOLDING_VAT_RATE).doubleValue()).setScale(2, HALF_EVEN),
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

        contractorController.setPaymentModel(newPaymentModel);

        assertEquals(newPaymentModel,contractorController.getPaymentModel());
    }

}