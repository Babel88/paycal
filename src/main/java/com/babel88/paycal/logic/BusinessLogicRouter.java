package com.babel88.paycal.logic;

import com.babel88.paycal.api.ForeignPaymentDetails;
import com.babel88.paycal.api.Router;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.logic.TelegraphicTransfers;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by edwin.njeru on 18/07/2016.
 * Contains actual implementation of calculations based on info collected pertaining to
 * the Invoice.
 * Ideally this is the heart of the entre program
 * Ideally all of those methods are likely to crowd the class but will try to keep the methods to small sizes
 */
public class BusinessLogicRouter implements Router {

    private static Router instance = new BusinessLogicRouter();
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private PrepaymentController prepaymentController;
    private PaymentModelViewInterface paymentModelView;
    private TelegraphicTransfers foreignPayments;
    private ForeignPaymentDetails foreignPaymentDetails;
    private PartialTaxPaymentController partialTaxPaymentController;
    private DefaultControllers contractorPaymentController;
    private DefaultControllers withholdingTaxPaymentController;
    private DefaultControllers rentalPaymentsController;
    private DefaultControllers defaultTypicalPaymentsController;
    private DefaultControllers foreignPaymentsController;

    private BusinessLogicRouter() {

        log.debug("Creating an instance of the main business logic controller");

        foreignPayments = LogicFactory.createTelegraphicTransfers();

        log.debug("Fetching controller singleton from Controller Factory");

        prepaymentController = ControllerFactory.getPrepaymentController();

        partialTaxPaymentController = ControllerFactory.getPartialTaxPaymentController();

        log.debug("Fetching instances from the payment model view Factory");

        paymentModelView = ModelViewFactory.createPaymentModelView();

        log.debug("Fetching instance from the general factory");

        foreignPaymentDetails = GeneralFactory.createForeignPaymentDetails();

        log.debug("Fetching contractor payments controller from ControllerFactory");
        contractorPaymentController = ControllerFactory.getInstance().getContractorPaymentController();

        log.debug("Fetching withholdingTaxPaymentController from controller factory");
        withholdingTaxPaymentController = ControllerFactory.getInstance().getWithholdingTaxPaymentController();

        log.debug("Fetching rentalPaymentsController from Controller factory");
        rentalPaymentsController = ControllerFactory.getInstance().getRentalPaymentsController();

        log.debug("Fetching the defaultTypicalPaymentsController from the controller ");
        defaultTypicalPaymentsController = ControllerFactory.getInstance().getDefaultTypicalPaymentsController();

        log.debug("Fetching the foreignPaymentsController from {}",ControllerFactory.getInstance());
        foreignPaymentsController = ControllerFactory.getInstance().getForeignPaymentsController();
    }

    @Contract(pure = true)
    public static Router getInstance(){

        return instance;
    }

    @Override
    public void normal() {

        //typicalPaymentsController.runCalculation(invoiceAmount);

        defaultTypicalPaymentsController.runCalculation();

    }

    @Override
    public void vatGiven() {

        partialTaxPaymentController.runCalculation();

    }

    @Override
    public void contractor() {

        contractorPaymentController.runCalculation();
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

        foreignPaymentsController.runCalculation();

        /*

        boolean doAgain;

        do {
            BigDecimal vatRate = BigDecimal.valueOf(foreignPaymentDetails.vatRate()/100);

            BigDecimal withHoldingTax = BigDecimal.valueOf(foreignPaymentDetails.withHoldingTaxRate()/100);

            BigDecimal invoiceAmount = foreignPaymentDetails.invoiceAmount();

            Boolean exclusive = foreignPaymentDetails.exclusiveOfWithholdingTax();

            BigDecimal reverseInvoice =
                    foreignPayments.getReverseInvoice(invoiceAmount, withHoldingTax,exclusive);

            BigDecimal withHoldingVat =
                    foreignPayments.getReverseVat(reverseInvoice, vatRate);

            BigDecimal total =
                    foreignPayments.getTotalExpense(reverseInvoice,vatRate,exclusive,invoiceAmount,withHoldingVat);

            BigDecimal toPayee =
                    foreignPayments.getPaySupplier(total,withHoldingTax,withHoldingVat);


            BigDecimal toPrepay =
                    ((PrepaymentService) prepaymentController::getPrepayment).prepay(total);

            paymentModelView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
            // Results submitted for paymentModelView

            doAgain = foreignPaymentDetails.doAgain();
        } while (doAgain);
*/
    }

}



