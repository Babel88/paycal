package com.babel88.paycal.controllers.base;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.api.controllers.PaymentsControllerRunner;
import com.babel88.paycal.api.controllers.PrepaymentController;
import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.api.view.Visitor;
import com.babel88.paycal.controllers.delegate.PrepaymentsDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Controller for rental payments updates the payment model with assertions based on
 * the rentalPaymentLogic and sends the results to views
 *
 * Created by edwin.njeru on 29/08/2017.
 */
public class RentalPaymentsController implements DefaultControllers,PaymentsControllerRunner, Serializable {

    private static final Logger log = LoggerFactory.getLogger(RentalPaymentsController.class);

    // = new PrepaymentsDelegate(this); Defined as inner bean in the application context
    private PrepaymentsDelegate prepaymentsDelegate;
    private DefaultLogic rentalPaymentLogic;
    protected DefaultPaymentModel paymentModel;
    private InvoiceDetails invoiceDetails;
    private PrepaymentController prepaymentController;
    private Visitor modelViewerVisitor;
    private Visitor modelPrecisionVisitor;
    private Visitor reportingVisitor;

    // Hold values for the calculations
    protected BigDecimal invoiceAmount;
    @SuppressWarnings("all")
    private Boolean doAgain;

    @SuppressWarnings("all")
    public RentalPaymentsController() {

        log.debug("Creating a rental payments controller using constructor in PaymentsControllerRunner : {}",this);
    }

    @Override
    public void runCalculation() {

        if(invoiceDetails != null) {

            do {

                invoiceAmount = invoiceDetails.invoiceAmount();

                updateWithholdingVat();

                updateWithholdingTax();

                updateTotalExpense();

                updateToPayee();

                updateToPrepay();

                paymentModel.accept(modelPrecisionVisitor);

                paymentModel.accept(modelViewerVisitor);

                doAgain = invoiceDetails.doAgain();
            } while (doAgain);

            paymentModel.accept(reportingVisitor);

        } else {

            log.error("Invoice details object is null");
        }

    }

    @Override
    public void updateTotalExpense() {

        paymentModel.setTotalExpense(
                rentalPaymentLogic.calculateTotalExpense(invoiceAmount)
        );
    }

    @Override
    public void updateToPayee() {

       paymentModel.setToPayee(
                rentalPaymentLogic.calculateToPayee(invoiceAmount)
        );

    }

    @Override
    public void updateWithholdingTax() {

        paymentModel.setWithholdingTax(
                rentalPaymentLogic.calculateWithholdingTax(invoiceAmount)
        );
    }

    @Override
    public void updateWithholdingVat() {

        paymentModel.setWithHoldingVat(
                rentalPaymentLogic.calculateWithholdingVat(invoiceAmount)
        );

    }

    /**
     * Updates the amount to prepay in the payment model
     */
    @Override
    public void updateToPrepay() {

        prepaymentsDelegate.updateToPrepay();
    }

    @Override
    public DefaultPaymentModel getPaymentModel() {

        return paymentModel;
    }

    @SuppressWarnings("all")
    public RentalPaymentsController setRentalPaymentLogic(DefaultLogic rentalPaymentLogic) {
        this.rentalPaymentLogic = rentalPaymentLogic;
        return this;
    }

    @SuppressWarnings("all")
    public DefaultLogic getRentalPaymentLogic() {
        return rentalPaymentLogic;
    }

    public RentalPaymentsController setPaymentModel(DefaultPaymentModel paymentModel) {
        this.paymentModel = paymentModel;
        return this;
    }

    @SuppressWarnings("all")
    public InvoiceDetails getInvoiceDetails() {
        return invoiceDetails;
    }

    @SuppressWarnings("all")
    public RentalPaymentsController setInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
        return this;
    }

    @SuppressWarnings("all")
    public PrepaymentController getPrepaymentController() {
        return prepaymentController;
    }

    @SuppressWarnings("all")
    public RentalPaymentsController setPrepaymentController(PrepaymentController prepaymentController) {
        this.prepaymentController = prepaymentController;
        return this;
    }

    @SuppressWarnings("all")
    public PrepaymentsDelegate getPrepaymentsDelegate() {
        return prepaymentsDelegate;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public RentalPaymentsController setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
        return this;
    }

    public Boolean getDoAgain() {
        return doAgain;
    }

    public RentalPaymentsController setDoAgain(Boolean doAgain) {
        this.doAgain = doAgain;
        return this;
    }

    @SuppressWarnings("all")
    public Visitor getModelViewerVisitor() {
        return modelViewerVisitor;
    }

    @SuppressWarnings("all")
    public RentalPaymentsController setModelViewerVisitor(Visitor modelViewerVisitor) {
        this.modelViewerVisitor = modelViewerVisitor;
        return this;
    }

    @SuppressWarnings("all")
    public Visitor getModelPrecisionVisitor() {
        return modelPrecisionVisitor;
    }

    @SuppressWarnings("all")
    public RentalPaymentsController setModelPrecisionVisitor(Visitor modelPrecisionVisitor) {
        this.modelPrecisionVisitor = modelPrecisionVisitor;
        return this;
    }

    @SuppressWarnings("all")
    public Visitor getReportingVisitor() {
        return reportingVisitor;
    }

    @SuppressWarnings("all")
    public RentalPaymentsController setReportingVisitor(Visitor reportingVisitor) {
        this.reportingVisitor = reportingVisitor;
        return this;
    }

    public RentalPaymentsController setPrepaymentsDelegate(PrepaymentsDelegate prepaymentsDelegate) {
        this.prepaymentsDelegate = prepaymentsDelegate;
        return this;
    }
}
