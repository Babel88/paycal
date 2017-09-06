package com.babel88.paycal.models;

import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ResultsOutput implements Serializable, ResultsViewer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private PaymentModelViewInterface display;

    private final AtomicReference<BigDecimal> total = new AtomicReference<BigDecimal>();
    private final AtomicReference<BigDecimal> vatWithheld = new AtomicReference<BigDecimal>();
    private final AtomicReference<BigDecimal> withholdingTax = new AtomicReference<BigDecimal>();
    private final AtomicReference<BigDecimal> toPrepay = new AtomicReference<BigDecimal>();
    private final AtomicReference<BigDecimal> toPayee = new AtomicReference<BigDecimal>();

    public ResultsOutput(PaymentModelViewInterface display) {

        this.display = display;

        log.debug("Creating empty outputFields for : {}",this);

        total.set(new BigDecimal(BigInteger.ZERO));
        vatWithheld.set(new BigDecimal(BigInteger.ZERO));
        withholdingTax.set(new BigDecimal(BigInteger.ZERO));
        toPrepay.set(new BigDecimal(BigInteger.ZERO));
        toPayee.set(new BigDecimal(BigInteger.ZERO));

    }

    /**
     * This method renders a paymentModelView for the payment object passed as parameter
     *
     * @param paymentModel object containing results of compuations
     */
    @Override
    public ResultsOutput forPayment(PaymentModel paymentModel) {

        createLocalPaymentFieldsFromModel(paymentModel);

        display.displayResults(total.get(), vatWithheld.get(), withholdingTax.get(), toPrepay.get(), toPayee.get());

        return this;
    }

    private void createLocalPaymentFieldsFromModel(PaymentModel model){

        this.setTotal(model.getTotalExpense());
        this.setVatWithheld(model.getWithHoldingVat());
        this.setWithholdingTax(model.getWithholdingTax());
        this.setToPrepay(model.getToPrepay());
        this.setToPayee(model.getToPayee());
    }

    @SuppressWarnings(value = "never used")
    private ResultsViewer setTotal(BigDecimal total) {
        this.total.set(total);

        log.debug("Total field set as {}.: ", total);
        return this;
    }

    @SuppressWarnings(value = "never used")
    private ResultsViewer setVatWithheld(BigDecimal vatWithheld) {
        this.vatWithheld.set(vatWithheld);

        log.debug("vatWithheld field set as {}.: ", vatWithheld);
        return this;
    }

    private ResultsViewer setWithholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax.set(withholdingTax);

        log.debug("withholdingTax field set as {}.: ", withholdingTax);
        return this;
    }

    private ResultsViewer setToPrepay(BigDecimal toPrepay) {
        this.toPrepay.set(toPrepay);

        log.debug("toPrepay field set as : {}.", toPrepay);
        return this;
    }

    private ResultsViewer setToPayee(BigDecimal toPayee) {
        this.toPayee.set(toPayee);

        log.debug("toPayee field set as : {}.", toPayee);
        return this;
    }

    public BigDecimal getTotal() {
        return total.get();
    }

    public BigDecimal getVatWithheld() {
        return vatWithheld.get();
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax.get();
    }

    public BigDecimal getToPrepay() {
        return toPrepay.get();
    }

    public BigDecimal getToPayee() {
        return toPayee.get();
    }

    public void resetOutput(){

        total.set(BigDecimal.ZERO);
        vatWithheld.set(BigDecimal.ZERO);
        withholdingTax.set(BigDecimal.ZERO);
        toPrepay.set(BigDecimal.ZERO);
        toPayee.set(BigDecimal.ZERO);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultsOutput that = (ResultsOutput) o;
        return Objects.equal(log, that.log) &&
                Objects.equal(display, that.display) &&
                Objects.equal(getTotal(), that.getTotal()) &&
                Objects.equal(getVatWithheld(), that.getVatWithheld()) &&
                Objects.equal(getWithholdingTax(), that.getWithholdingTax()) &&
                Objects.equal(getToPrepay(), that.getToPrepay()) &&
                Objects.equal(getToPayee(), that.getToPayee());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(log, display, getTotal(), getVatWithheld(), getWithholdingTax(),
                getToPrepay(), getToPayee());
    }
}
