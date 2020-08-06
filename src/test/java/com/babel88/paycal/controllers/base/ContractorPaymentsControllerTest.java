package com.babel88.paycal.controllers.base;

import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.logic.base.ContractorLogic;
import com.babel88.paycal.models.PaymentModel;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.babel88.paycal.models.AppConstants.HUNDRED;
import static com.babel88.paycal.models.AppConstants.SYSTEM_VAT_RATE;
import static com.babel88.paycal.models.AppConstants.SYSTEM_WITHHOLDING_VAT_RATE;
import static com.babel88.paycal.utils.TestUtilityFunctions.bd;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ContractorPaymentsControllerTest {

    private ContractorPaymentsController contractorController;

    private BigDecimal invoiceAmount = BigDecimal.valueOf(114000).setScale(2, HALF_EVEN);

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

        assertEquals(bd(109000.00),contractorController.getPaymentModel().getToPayee());
    }

    @Test
    public void updateWithholdingTax() throws Exception {

        contractorController.setInvoiceAmount(invoiceAmount);

        contractorController.updateWithholdingTax();

        assertEquals(bd(3000.00),contractorController.getPaymentModel().getWithholdingTax());
    }

    @Test
    public void updateWithholdingVat() throws Exception {

        contractorController.setInvoiceAmount(invoiceAmount);

        contractorController.updateWithholdingVat();

        BigDecimal vatRate = SYSTEM_VAT_RATE.divide(HUNDRED,2, HALF_EVEN);
        BigDecimal withTaxRate = SYSTEM_WITHHOLDING_VAT_RATE.divide(HUNDRED,2, HALF_EVEN);

        assertEquals(bd(2000.00),contractorController.getPaymentModel().getWithholdingVat());
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