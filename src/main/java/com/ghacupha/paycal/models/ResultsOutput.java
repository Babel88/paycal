package com.ghacupha.paycal.models;

import com.ghacupha.paycal.api.ResultsViewer;
import com.ghacupha.paycal.api.view.PaymentModelViewInterface;
import com.ghacupha.paycal.config.factory.ModelViewFactory;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ResultsOutput implements Serializable, ResultsViewer {

    private final static ResultsViewer instance =
            new ResultsOutput(ModelViewFactory.createPaymentModelView());
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final PaymentModelViewInterface view;
    private final AtomicReference<BigDecimal> total = new AtomicReference<BigDecimal>();
    private final AtomicReference<BigDecimal> vatWithheld = new AtomicReference<BigDecimal>();
    private final AtomicReference<BigDecimal> withholdingTax = new AtomicReference<BigDecimal>();
    private final AtomicReference<BigDecimal> toPrepay = new AtomicReference<BigDecimal>();
    private final AtomicReference<BigDecimal> toPayee = new AtomicReference<BigDecimal>();

    public ResultsOutput(PaymentModelViewInterface view) {

        this.view = view;

        log.debug("Creating empty outputFields...");

        total.set(new BigDecimal(BigInteger.ZERO));
        vatWithheld.set(new BigDecimal(BigInteger.ZERO));
        withholdingTax.set(new BigDecimal(BigInteger.ZERO));
        toPrepay.set(new BigDecimal(BigInteger.ZERO));
        toPayee.set(new BigDecimal(BigInteger.ZERO));

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

        view.displayResults(total.get(), vatWithheld.get(), withholdingTax.get(), toPrepay.get(), toPayee.get());

        return this;
    }

    private void createLocalPaymentFieldsFromModel(PaymentModel model) {

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

    public void resetOutput() {

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
                Objects.equal(total, that.total) &&
                Objects.equal(vatWithheld, that.vatWithheld) &&
                Objects.equal(withholdingTax, that.withholdingTax) &&
                Objects.equal(toPrepay, that.toPrepay) &&
                Objects.equal(toPayee, that.toPayee);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(log, total, vatWithheld, withholdingTax, toPrepay, toPayee);
    }
}
