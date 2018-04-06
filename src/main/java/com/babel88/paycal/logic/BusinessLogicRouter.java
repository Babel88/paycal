package com.babel88.paycal.logic;

import com.babel88.paycal.api.Router;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.TTController;


/**
 * Created by edwin.njeru on 18/07/2016.
 * Contains actual implementation of calculations based on info collected pertaining to
 * the Invoice.
 * Ideally this is the heart of the entre program
 * Ideally all of those methods are likely to crowd the class but will try to keep the methods to small sizes
 */
public class BusinessLogicRouter implements Router {

    private final PartialTaxPaymentController partialTaxPaymentController;
    private final DefaultControllers contractorPaymentsController;
    private final DefaultControllers withholdingTaxPaymentController;
    private final DefaultControllers rentalPaymentsController;
    private final DefaultControllers typicalPaymentsController;
    private final TTController ttController;

    public BusinessLogicRouter(PartialTaxPaymentController partialTaxPaymentController, DefaultControllers contractorPaymentsController, DefaultControllers withholdingTaxPaymentController, DefaultControllers rentalPaymentsController, DefaultControllers typicalPaymentsController, TTController ttController) {
        this.partialTaxPaymentController = partialTaxPaymentController;
        this.contractorPaymentsController = contractorPaymentsController;
        this.withholdingTaxPaymentController = withholdingTaxPaymentController;
        this.rentalPaymentsController = rentalPaymentsController;
        this.typicalPaymentsController = typicalPaymentsController;
        this.ttController = ttController;
    }

    @Override
    public void normal() {

        typicalPaymentsController.runCalculation();

    }

    @Override
    public void vatGiven() {

        partialTaxPaymentController.runCalculation();

    }

    @Override
    public void contractor() {

        contractorPaymentsController.runCalculation();
    }

    @Override
    public void taxToWithhold() {

        withholdingTaxPaymentController.runCalculation();

    }

    @Override
    public void rentalPayments() {

        rentalPaymentsController.runCalculation();
    }

    /**
     * <p>Calculates transaction items for invoices of the following criteria</p>
     * <p>a) The payee is chargeable for withholding tax for consultancy</p>
     * <p>b) The payee is not locally domiciled</p>
     * <p>c) The payee chargeable to VAT tax</p>
     * <p>d) The Invoice is not encumbered with duties or levies</p>
     */
    @Override
    public void telegraphicTransfer() {

        ttController.runCalculation();
    }

    /*public BusinessLogicRouter setPartialTaxPaymentController(PartialTaxPaymentController partialTaxPaymentController) {
        this.partialTaxPaymentController = partialTaxPaymentController;
        return this;
    }

    public BusinessLogicRouter setContractorPaymentsController(DefaultControllers contractorPaymentsController) {
        this.contractorPaymentsController = contractorPaymentsController;
        return this;
    }

    public BusinessLogicRouter setWithholdingTaxPaymentController(DefaultControllers withholdingTaxPaymentController) {
        this.withholdingTaxPaymentController = withholdingTaxPaymentController;
        return this;
    }

    public BusinessLogicRouter setRentalPaymentsController(DefaultControllers rentalPaymentsController) {
        this.rentalPaymentsController = rentalPaymentsController;
        return this;
    }

    public BusinessLogicRouter setTypicalPaymentsController(DefaultControllers typicalPaymentsController) {
        this.typicalPaymentsController = typicalPaymentsController;
        return this;
    }

    public BusinessLogicRouter setTtController(TTController ttController) {
        this.ttController = ttController;
        return this;
    }*/
}




