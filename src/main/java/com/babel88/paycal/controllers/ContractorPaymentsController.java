package com.babel88.paycal.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.controllers.BaseController;
import com.babel88.paycal.api.logic.template.DefaultBaseLogicModel;
import com.babel88.paycal.config.factory.ModelFactory;
import com.babel88.paycal.logic.base.AbstractBaseLogicModel;
import com.babel88.paycal.logic.base.ContractorLogicModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class ContractorPaymentsController extends AbstractBaseController {

    private static final Logger log = LoggerFactory.getLogger(ContractorPaymentsController.class);

    private static BaseController instance = new ContractorPaymentsController();

    private DefaultPaymentModel defaultPaymentModel;

    private DefaultBaseLogicModel contractorLogicModel;

    public ContractorPaymentsController() {
        super();

        log.debug("Creating an instance of the ContractorPaymentsController");

        log.debug("Fetching an instance of the payments model from model factory");
        defaultPaymentModel = ModelFactory.getInstance().createPaymentModel();

        log.debug("Fetching an instance of the defaultBaseLogicModel from logic factory");
        //TODO contractorLogicModel = LogicFactory.getInstance().createContractorLogicModel();
    }

    public static BaseController getInstance() {

        log.debug("Returning singleton instance of the ContractorPaymentsController");
        return instance;
    }

    /**
     * This method return an instance of an Object implementing the DefaultPaymentModel interface
     *
     * @return DefaultPaymentModel object
     */
    @Override
    public DefaultPaymentModel getDefaultPaymentModelInstance() {

        log.debug("Returning a default payment model for calculations : {}.",defaultPaymentModel.toString());

        return defaultPaymentModel;
    }

    /**
     * This method returns an instance of an object that implements the DefaultBaseLogic interface
     *
     * @return DefaultBaseLogicModel object
     */
    @Override
    public DefaultBaseLogicModel getDefaultBaseLogicModelInstance() {

        log.debug("Returning a BaseLogicModel object :{}.",contractorLogicModel);

        //TODO complete contractorLogicModel INTERFACE

        return new AbstractBaseLogicModel() {
            @Override
            public BigDecimal getAmountBeforeTax(BigDecimal invoiceAmount) {

                log.debug("Calculating the amount before taxes using : {}.",invoiceAmount);
                return getInvoiceAmount()
                        .divide(
                                BigDecimal.ONE.add(paymentParameters.getWithholdingVatRate())
                        );
            }

            @Override
            public BigDecimal getTotalExpenses(BigDecimal invoiceAmount) {
                return null;
            }
        };
    }

    @Override
    public void setInvoiceAmount(BigDecimal invoiceAmount) {

        invoiceAmount = getInvoiceAmount();
    }
}
