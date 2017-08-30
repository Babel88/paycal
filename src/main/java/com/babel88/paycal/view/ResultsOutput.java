package com.babel88.paycal.view;

import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.config.factory.ModelViewFactory;
import com.babel88.paycal.models.PaymentModel;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class ResultsOutput implements Serializable, ResultsViewer {

    private static ResultsViewer instance =
            new ResultsOutput(ModelViewFactory.createPaymentModelView());
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final PaymentModelViewInterface view;
    private BigDecimal total;
    private BigDecimal vatWithheld;
    private BigDecimal withholdingTax;
    private BigDecimal toPrepay;
    private BigDecimal toPayee;

    public ResultsOutput(PaymentModelViewInterface view){

        this.view = view;

        log.debug("Creating empty outputFields...");

        total = new BigDecimal(BigInteger.ZERO);
        vatWithheld = new BigDecimal(BigInteger.ZERO);
        withholdingTax = new BigDecimal(BigInteger.ZERO);
        toPrepay = new BigDecimal(BigInteger.ZERO);
        toPayee = new BigDecimal(BigInteger.ZERO);

    }

    public static ResultsViewer getInstance() {
        return instance;
    }

    /**
     * This method renders a paymentModelView for the payment object passed as parameter
     *
     * @param paymentModel object containing results of compuations
     */
    @Override
    public ResultsOutput forPayment(PaymentModel paymentModel) {

        createLocalPaymentFieldsFromModel(paymentModel);

        view.displayResults(total, vatWithheld, withholdingTax, toPrepay, toPayee);

        return this;
    }

    private void createLocalPaymentFieldsFromModel(PaymentModel model){

        this.setTotal(model.getTotalExpense());
        this.setVatWithheld(model.getWithHoldingVat());
        this.setWithholdingTax(model.getWithHoldingTax());
        this.setToPrepay(model.getToPrepay());
        this.setToPayee(model.getToPayee());
    }

    @SuppressWarnings(value = "never used")
    private ResultsViewer setTotal(BigDecimal total) {
        this.total = total;

        log.debug("Total field set as {}.: ", total);
        return this;
    }

    @SuppressWarnings(value = "never used")
    private ResultsViewer setVatWithheld(BigDecimal vatWithheld) {
        this.vatWithheld = vatWithheld;

        log.debug("vatWithheld field set as {}.: ", vatWithheld);
        return this;
    }

    private ResultsViewer setWithholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax = withholdingTax;

        log.debug("withholdingTax field set as {}.: ", withholdingTax);
        return this;
    }

    private ResultsViewer setToPrepay(BigDecimal toPrepay) {
        this.toPrepay = toPrepay;

        log.debug("toPrepay field set as : {}.", toPrepay);
        return this;
    }

    private ResultsViewer setToPayee(BigDecimal toPayee) {
        this.toPayee = toPayee;

        log.debug("toPayee field set as : {}.", toPayee);
        return this;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getVatWithheld() {
        return vatWithheld;
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax;
    }

    public BigDecimal getToPrepay() {
        return toPrepay;
    }

    public BigDecimal getToPayee() {
        return toPayee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultsOutput that = (ResultsOutput) o;
        return Objects.equal(log, that.log) &&
                Objects.equal(total, that.total) &&
                Objects.equal(vatWithheld, that.vatWithheld) &&
                Objects.equal(withholdingTax, that.withholdingTax) &&
                Objects.equal(toPrepay, that.toPrepay) &&
                Objects.equal(toPayee, that.toPayee) &&
                Objects.equal(view, that.view);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(log, total, vatWithheld, withholdingTax, toPrepay, toPayee, view);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("total", total)
                .add("vatWithheld", vatWithheld)
                .add("withholdingTax", withholdingTax)
                .add("toPrepay", toPrepay)
                .add("toPayee", toPayee)
                .toString();
    }

    public void resetOutput(){

        total = BigDecimal.ZERO;
        vatWithheld = BigDecimal.ZERO;
        withholdingTax = BigDecimal.ZERO;
        toPrepay = BigDecimal.ZERO;
        toPayee = BigDecimal.ZERO;
    }
}
