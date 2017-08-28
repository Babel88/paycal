package com.babel88.paycal.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.controllers.BaseController;
import com.babel88.paycal.api.logic.template.DefaultBaseLogicModel;
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.config.factory.ModelFactory;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContractorPaymentsController extends AbstractBaseController {

    private static final Logger log = LoggerFactory.getLogger(ContractorPaymentsController.class);
    private static BaseController instance = new ContractorPaymentsController().initialization();
    private DefaultPaymentModel paymentModel;
    private DefaultBaseLogicModel contractorLogic;

    public ContractorPaymentsController() {
        super();

        log.debug("Creating an instance of the ContractorPaymentsController");

        log.debug("Fetching payment model instance from factory");

        paymentModel = ModelFactory.getInstance().createPaymentModel();
        contractorLogic = LogicFactory.getInstance().createContractorLogicModel();

    }

    @Contract(pure = true)
    public static BaseController getInstance() {
        return instance;
    }

    /**
     * This method returns an instance of an object that implements the DefaultBaseLogic interface
     *
     * @return DefaultBaseLogicModel object
     */
    @Override
    public DefaultBaseLogicModel getDefaultBaseLogicModelInstance() {

        return contractorLogic;
    }
}
