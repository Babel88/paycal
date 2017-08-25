package com.babel88.paycal.view;

import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.config.factory.ModelViewFactory;
import com.babel88.paycal.models.PaymentModel;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

@ComponentScan
public class ResultsOutput implements Serializable, ResultsViewer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AtomicReference<BigDecimal> total;
    private final AtomicReference<BigDecimal> vatWithheld;
    private final AtomicReference<BigDecimal> withholdingTax;
    private final AtomicReference<BigDecimal> toPrepay;
    private final AtomicReference<BigDecimal> toPayee;

    //TODO add paymentModelView logic
    private PaymentModelViewInterface view;
    private static ResultsViewer instance =
            new ResultsOutput(ModelViewFactory.getInstance().createPaymentModelView());

    @Autowired
    public ResultsOutput(PaymentModelViewInterface view){

        this.view = view;

        log.debug("Creating empty outputFields...");

        total = new AtomicReference<BigDecimal>();
        vatWithheld = new AtomicReference<BigDecimal>();
        withholdingTax = new AtomicReference<BigDecimal>();
        toPrepay = new AtomicReference<BigDecimal>();
        toPayee = new AtomicReference<BigDecimal>();

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

        view.displayResults(total.get(),vatWithheld.get(),withholdingTax.get(),toPrepay.get(),toPayee.get());

        return this;
    }

    private void createLocalPaymentFieldsFromModel(PaymentModel model){

        this.setTotal(model.getTotal());
        this.setVatWithheld(model.getWithHoldingVat());
        this.setWithholdingTax(model.getWithHoldingTax());
        this.setToPrepay(model.getToPrepay());
        this.setToPayee(model.getToPayee());
    }

    public ResultsViewer setTotal(BigDecimal total) {
        this.total.set(total);

        log.debug("Total field set as : ",total);
        return this;
    }

    public ResultsViewer setVatWithheld(BigDecimal vatWithheld) {
        this.vatWithheld.set(vatWithheld);

        log.debug("vatWithheld field set as : ",vatWithheld);
        return this;
    }

    public ResultsViewer setWithholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax.set(withholdingTax);

        log.debug("withholdingTax field set as : ",withholdingTax);
        return this;
    }

    public ResultsViewer setToPrepay(BigDecimal toPrepay) {
        this.toPrepay.set(toPrepay);

        log.debug("toPrepay field set as : ",toPrepay);
        return this;
    }

    public ResultsViewer setToPayee(BigDecimal toPayee) {
        this.toPayee.set(toPayee);

        log.debug("toPayee field set as : ",toPayee);
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
                .add("total", total.get())
                .add("vatWithheld", vatWithheld.get())
                .add("withholdingTax", withholdingTax.get())
                .add("toPrepay", toPrepay.get())
                .add("toPayee", toPayee.get())
                .toString();
    }

    public void resetOutput(){

        total.set(BigDecimal.ZERO);
        vatWithheld.set(BigDecimal.ZERO);
        withholdingTax.set(BigDecimal.ZERO);
        toPrepay.set(BigDecimal.ZERO);
        toPayee.set(BigDecimal.ZERO);
    }
}
