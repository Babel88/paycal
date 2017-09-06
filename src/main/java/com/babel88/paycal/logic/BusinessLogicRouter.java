package com.babel88.paycal.logic;

import com.babel88.paycal.api.ForeignPaymentDetails;
import com.babel88.paycal.api.Router;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.controllers.TTController;
import com.babel88.paycal.api.logic.TelegraphicTransfers;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by edwin.njeru on 18/07/2016.
 * Contains actual implementation of calculations based on info collected pertaining to
 * the Invoice.
 * Ideally this is the heart of the entre program
 * Ideally all of those methods are likely to crowd the class but will try to keep the methods to small sizes
 */
public class BusinessLogicRouter implements Router {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private PrepaymentController prepaymentController;

    private PaymentModelViewInterface display;

    private TelegraphicTransfers telegraphicTransfers;

    private ForeignPaymentDetails foreignPaymentDetails;

    private PartialTaxPaymentController partialTaxPaymentController;

    private DefaultControllers contractorPaymentsController;

    private DefaultControllers withholdingTaxPaymentController;

    private DefaultControllers rentalPaymentsController;

    private DefaultControllers typicalPaymentsController;

    private TTController ttController;

    public BusinessLogicRouter() {

        log.debug("Creating an instance of the main business logic controller : {}",this);
    }

    @Override
    public void normal() {

        //typicalPaymentsController.runCalculation(invoiceAmount);

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

    public BusinessLogicRouter setPrepaymentController(PrepaymentController prepaymentController) {
        this.prepaymentController = prepaymentController;
        return this;
    }

    public BusinessLogicRouter setDisplay(PaymentModelViewInterface display) {
        this.display = display;
        return this;
    }

    public BusinessLogicRouter setTelegraphicTransfers(TelegraphicTransfers telegraphicTransfers) {
        this.telegraphicTransfers = telegraphicTransfers;
        return this;
    }

    public BusinessLogicRouter setForeignPaymentDetails(ForeignPaymentDetails foreignPaymentDetails) {
        this.foreignPaymentDetails = foreignPaymentDetails;
        return this;
    }

    public BusinessLogicRouter setPartialTaxPaymentController(PartialTaxPaymentController partialTaxPaymentController) {
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
    }
}




