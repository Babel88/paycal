package com.babel88.paycal.models;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import static java.math.BigDecimal.ZERO;

public class PaymentModel implements Serializable, DefaultPaymentModel {

    private static PaymentModel instance = new PaymentModel();
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final AtomicReference<BigDecimal> amountB4Vat;
    private final AtomicReference<BigDecimal> withHoldingVat;
    private final AtomicReference<BigDecimal> total;
    private final AtomicReference<BigDecimal> toPayee;
    private final AtomicReference<BigDecimal> withHoldingTax;
    private final AtomicReference<BigDecimal> toPrepay;

    public PaymentModel() {

        log.debug("Creating empty fields");
        withHoldingVat = new AtomicReference<BigDecimal>();
        total = new AtomicReference<BigDecimal>();
        toPayee = new AtomicReference<BigDecimal>();
        withHoldingTax = new AtomicReference<BigDecimal>();
        amountB4Vat = new AtomicReference<BigDecimal>();
        toPrepay = new AtomicReference<BigDecimal>();

        log.debug("Payment model created : {}.",this.toString());

    }

    public static PaymentModel getInstance() {
        return instance;
    }

    public BigDecimal getAmountB4Vat() {
        return amountB4Vat.get();
    }

    public PaymentModel setAmountB4Vat(BigDecimal amountB4Vat) {

        log.debug("Amount before vat set as : {}.",amountB4Vat);
        this.amountB4Vat.set(amountB4Vat);
        return this;
    }

    public BigDecimal getWithHoldingVat() {
        return withHoldingVat.get();
    }

    @Override
    public PaymentModel setWithHoldingVat(BigDecimal withHoldingVat) {

        log.debug("Withholding vat set as : {}.",withHoldingVat);
        this.withHoldingVat.set(withHoldingVat);
        return this;
    }

    public BigDecimal getTotal() {
        return total.get();
    }

    @Override
    public PaymentModel setTotalExpense(BigDecimal total) {

        log.debug("Total amount payable set as :"+total);
        this.total.set(total);
        return this;
    }

    public BigDecimal getToPayee() {
        return toPayee.get();
    }

    @Override
    public PaymentModel setToPayee(BigDecimal toPayee) {

        log.debug("Amount payable to payee set as : "+toPayee);
        this.toPayee.set(toPayee);
        return this;
    }

    public BigDecimal getWithHoldingTax() {
        return withHoldingTax.get();
    }

    @Override
    public PaymentModel setWithholdingTax(BigDecimal withHoldingTax) {

        log.debug("Withholding tax set as : ",withHoldingTax);
        this.withHoldingTax.set(withHoldingTax);
        return this;
    }

    public BigDecimal getToPrepay() {
        return toPrepay.get();
    }

    @Override
    public PaymentModel setToPrepay(BigDecimal toPrepay) {
        this.toPrepay.set(toPrepay);
        return this;
    }

    public void init(){

        log.debug("Instantiating the PaymentModel local fields");
        this.setAmountB4Vat(ZERO);
        this.setToPayee(ZERO);
        this.setTotalExpense(ZERO);
        this.setWithholdingTax(ZERO);
        this.setWithHoldingVat(ZERO);
    }

    public void destroy(){

        log.debug("Resetting the PaymentModel object");
        this.setAmountB4Vat(ZERO);
        this.setToPayee(ZERO);
        this.setTotalExpense(ZERO);
        this.setWithholdingTax(ZERO);
        this.setWithHoldingVat(ZERO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentModel that = (PaymentModel) o;
        return Objects.equal(log, that.log) &&
                Objects.equal(amountB4Vat, that.amountB4Vat) &&
                Objects.equal(withHoldingVat, that.withHoldingVat) &&
                Objects.equal(total, that.total) &&
                Objects.equal(toPayee, that.toPayee) &&
                Objects.equal(withHoldingTax, that.withHoldingTax) &&
                Objects.equal(toPrepay, that.toPrepay);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(log, amountB4Vat, withHoldingVat, total,
                toPayee, withHoldingTax, toPrepay);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("amountB4Vat", amountB4Vat)
                .add("withHoldingVat", withHoldingVat)
                .add("total", total)
                .add("toPayee", toPayee)
                .add("withHoldingTax", withHoldingTax)
                .add("toPrepay", toPrepay)
                .toString();
    }

    public PaymentModelMemento saveStateToMemento() {

        log.debug("Saving a new state to payment model memento");
        return new PaymentModelMemento(this);
    }

    public void getStateFromMemento(PaymentModelMemento memento) {

        this.setAmountB4Vat(memento.getAmountB4Vat());
        this.setWithholdingTax(memento.getWithHoldingTax());
        this.setWithHoldingVat(memento.getWithHoldingVat());
        this.setToPayee(memento.getToPayee());
        this.setToPrepay(memento.getToPrepay());
        this.setTotalExpense(memento.getTotal());

        log.debug("Payment model successfully restored to former state : {}.", this.toString());
    }
}
