package com.babel88.paycal.models;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import static java.math.BigDecimal.ZERO;

public class PaymentModelMemento {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AtomicReference<BigDecimal> amountB4Vat;

    private final AtomicReference<BigDecimal> withHoldingVat;

    private final AtomicReference<BigDecimal> total;

    private final AtomicReference<BigDecimal> toPayee;

    private final AtomicReference<BigDecimal> withHoldingTax;
    private final AtomicReference<BigDecimal> toPrepay;

    public PaymentModelMemento(PaymentModel model) {

        log.debug("Creating the Payment model memento");

        this.amountB4Vat = new AtomicReference<>(model.getAmountB4Vat());
        this.withHoldingVat = new AtomicReference<>(model.getWithHoldingVat());
        this.total = new AtomicReference<>(model.getTotalExpense());
        this.toPayee = new AtomicReference<>(model.getToPayee());
        this.withHoldingTax = new AtomicReference<>(model.getWithholdingTax());
        this.toPrepay = new AtomicReference<>(model.getToPrepay());

        log.debug("Memento object created : {}.", this.toString());
    }

    public BigDecimal getAmountB4Vat() {
        return amountB4Vat.get();
    }

    public PaymentModelMemento setAmountB4Vat(BigDecimal amountB4Vat) {

        log.debug("Amount before vat set as : {}.", amountB4Vat);
        this.amountB4Vat.set(amountB4Vat);
        return this;
    }

    public BigDecimal getWithHoldingVat() {
        return withHoldingVat.get();
    }

    public PaymentModelMemento setWithHoldingVat(BigDecimal withHoldingVat) {

        log.debug("Withholding vat set as : {}.", withHoldingVat);
        this.withHoldingVat.set(withHoldingVat);
        return this;
    }

    public BigDecimal getTotal() {
        return total.get();
    }

    public PaymentModelMemento setTotal(BigDecimal total) {

        log.debug("Total amount payable set as :" + total);
        this.total.set(total);
        return this;
    }

    public BigDecimal getToPayee() {
        return toPayee.get();
    }

    public PaymentModelMemento setToPayee(BigDecimal toPayee) {

        log.debug("Amount payable to payee set as : " + toPayee);
        this.toPayee.set(toPayee);
        return this;
    }

    public BigDecimal getWithHoldingTax() {
        return withHoldingTax.get();
    }

    public PaymentModelMemento setWithHoldingTax(BigDecimal withHoldingTax) {

        log.debug("Withholding tax set as : ", withHoldingTax);
        this.withHoldingTax.set(withHoldingTax);
        return this;
    }

    public BigDecimal getToPrepay() {
        return toPrepay.get();
    }

    public void setToPrepay(BigDecimal toPrepay) {
        this.toPrepay.set(toPrepay);
    }

    public void init() {

        log.debug("Instantiating the PaymentModel local fields");
        this.setAmountB4Vat(ZERO);
        this.setToPayee(ZERO);
        this.setTotal(ZERO);
        this.setWithHoldingTax(ZERO);
        this.setWithHoldingVat(ZERO);
    }

    public void destroy() {

        log.debug("Resetting the PaymentModel object");
        this.setAmountB4Vat(ZERO);
        this.setToPayee(ZERO);
        this.setTotal(ZERO);
        this.setWithHoldingTax(ZERO);
        this.setWithHoldingVat(ZERO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentModelMemento that = (PaymentModelMemento) o;
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
        return Objects.hashCode(log, amountB4Vat, withHoldingVat, total, toPayee, withHoldingTax, toPrepay);
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
}
