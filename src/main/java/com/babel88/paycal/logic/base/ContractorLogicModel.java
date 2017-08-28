package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.logic.template.DefaultBaseLogicModel;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.LogicFactory;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.ONE;

public class ContractorLogicModel extends AbstractBaseLogicModel {

    private final static Logger log = LoggerFactory.getLogger(ContractorLogicModel.class);
    private static DefaultBaseLogicModel instance = new ContractorLogicModel().initialization();
    private final InvoiceDetails invoice;
    private final PaymentParameters paymentParameters;

    public ContractorLogicModel() {
        super();

        log.debug("Creating contractor logic model");

        log.debug("Fetching payment parameters from factory");
        paymentParameters = LogicFactory.getInstance().createPaymentParameters();

        invoice = GeneralFactory.getInstance().createInvoice();
    }



    @Contract(pure = true)
    public static DefaultBaseLogicModel getInstance() {
        return instance;
    }

    /**
     * This method return the amount of money charged in the invoice before taxes are considered
     *
     * @return amount before taxes
     */
    @Override
    public BigDecimal getAmountBeforeTax(BigDecimal invoiceAmount) {

        log.debug("Calculating the amount before taxes using : {}.",invoiceAmount);
        return invoiceAmount
                .divide(
                        ONE.add(paymentParameters.getWithholdingVatRate()
                        ), RoundingMode.HALF_EVEN
                )
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    /**
     * This method returns the total expenses for the payment in question
     *
     * @return
     */
    @Override
    public BigDecimal getTotalExpenses(BigDecimal invoiceAmount) {

        log.debug("Calculating the amount before taxes using : {}.",invoiceAmount);
        return invoiceAmount;
    }
}
