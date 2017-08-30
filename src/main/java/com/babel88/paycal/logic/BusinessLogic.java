package com.babel88.paycal.logic;

import com.babel88.paycal.api.DefaultPrepayable;
import com.babel88.paycal.api.ForeignPaymentDetails;
import com.babel88.paycal.api.Logic;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PartialTaxPaymentController;
import com.babel88.paycal.api.controllers.TypicalPaymentsControllers;
import com.babel88.paycal.api.factory.Incarnatable;
import com.babel88.paycal.api.logic.Contractors;
import com.babel88.paycal.api.logic.PrepaymentService;
import com.babel88.paycal.api.logic.TelegraphicTransfers;
import com.babel88.paycal.api.logic.WithholdingTaxPayments;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import com.babel88.paycal.controllers.prepayments.PrepaymentController;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 18/07/2016.
 * Contains actual implementation of calculations based on info collected pertaining to
 * the Invoice.
 * Ideally this is the heart of the entre program
 * Ideally all of those methods are likely to crowd the class but will try to keep the methods to small sizes
 */
public class BusinessLogic implements Logic,Incarnatable {

    private static Logic instance = new BusinessLogic();
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public DefaultPrepayable defaultPrepayment;
    private Contractors contractor;
    private WithholdingTaxPayments withholdingTaxPayment;
    private PrepaymentController prepaymentController;
    private PaymentModelViewInterface paymentModelView;
    private TelegraphicTransfers foreignPayments;
    private ForeignPaymentDetails foreignPaymentDetails;
    private TypicalPaymentsControllers typicalPaymentsController;
    private PartialTaxPaymentController partialTaxPaymentController;
    private DefaultControllers contractorPaymentController;
    private DefaultControllers withholdingTaxPaymentController;
    private DefaultControllers rentalPaymentsController;
    private DefaultControllers defaultTypicalPaymentsController;

    private BusinessLogic() {

        log.debug("Creating an instance of the main business logic controller");

        withholdingTaxPayment = LogicFactory.getInstance().createWithholdingTaxPayments();

        contractor = LogicFactory.getInstance().createContractors();

        defaultPrepayment = LogicFactory.getInstance().createDefaultPrepayment();

        foreignPayments = LogicFactory.getInstance().createTelegraphicTransfers();

        log.debug("Fetching controller singleton from Controller Factory");

        prepaymentController = ControllerFactory.getInstance().createPrepaymentController();

        typicalPaymentsController = ControllerFactory.getInstance().createTypicalPaymentsController();

        partialTaxPaymentController = ControllerFactory.getInstance().createPartialTaxPaymentController();

        log.debug("Fetching instances from the payment model view Factory");

        paymentModelView = ModelViewFactory.getInstance().createPaymentModelView();

        log.debug("Fetching instance from the general factory");

        foreignPaymentDetails = GeneralFactory.getInstance().createForeignPaymentDetails();

        log.debug("Fetching contractor payments controller from ControllerFactory");
        contractorPaymentController = ControllerFactory.getInstance().createContractorPaymentController();

        log.debug("Fetching withholdingTaxPaymentController from controller factory");
        withholdingTaxPaymentController = ControllerFactory.getInstance().createWithholdingTaxPaymentController();

        log.debug("Fetching rentalPaymentsController from Controller factory");
        rentalPaymentsController = ControllerFactory.getInstance().createRentalPaymentsController();

        log.debug("Fetching the defaultTypicalPaymentsController from the controller ");
        defaultTypicalPaymentsController = ControllerFactory.getInstance().getDefaultTypicalPaymentsController();
    }

    @Contract(pure = true)
    public static Logic getInstance(){

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

    }

}




