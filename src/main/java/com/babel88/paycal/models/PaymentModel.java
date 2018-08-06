package com.babel88.paycal.models;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.view.Visitor;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import static java.math.BigDecimal.ZERO;

public class PaymentModel implements Serializable, DefaultPaymentModel {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final AtomicReference<BigDecimal> amountB4Vat;
    private final AtomicReference<BigDecimal> withHoldingVat;
    private final AtomicReference<BigDecimal> total;
    private final AtomicReference<BigDecimal> toPayee;
    private final AtomicReference<BigDecimal> withHoldingTax;
    private final AtomicReference<BigDecimal> toPrepay;

    public PaymentModel() {

        log.debug("Creating payment model : {}", this);
        withHoldingVat = new AtomicReference<BigDecimal>();
        total = new AtomicReference<BigDecimal>();
        toPayee = new AtomicReference<BigDecimal>();
        withHoldingTax = new AtomicReference<BigDecimal>();
        amountB4Vat = new AtomicReference<BigDecimal>();
        toPrepay = new AtomicReference<BigDecimal>();

        log.debug("Payment model created : {}.", this.toString());

    }

    public PaymentModel(PaymentModel copy) {

        log.debug("Creating payment model : {} using the copy constructor with {} \n" +
                "as argument", this, copy);
        withHoldingVat = new AtomicReference<BigDecimal>(copy.getWithholdingVat());
        total = new AtomicReference<BigDecimal>(copy.getTotalExpense());
        toPayee = new AtomicReference<BigDecimal>(copy.getToPayee());
        withHoldingTax = new AtomicReference<BigDecimal>(copy.getWithholdingTax());
        amountB4Vat = new AtomicReference<BigDecimal>(copy.getAmountB4Vat());
        toPrepay = new AtomicReference<BigDecimal>(copy.getToPrepay());

        log.debug("Payment model created : {}.", this.toString());
    }

    public BigDecimal getAmountB4Vat() {
        return amountB4Vat.get();
    }

    public PaymentModel setAmountB4Vat(BigDecimal amountB4Vat) {

        log.debug("Amount before vat set as : {}.", amountB4Vat);
        this.amountB4Vat.set(amountB4Vat);
        return this;
    }

    public BigDecimal getWithHoldingVat() {

        log.debug("Returning withholding vat KES {}", withHoldingVat.get());
        return withHoldingVat.get();
    }

    @Override
    public PaymentModel setWithHoldingVat(BigDecimal withHoldingVat) {

        log.debug("Withholding vat set as : {}.", withHoldingVat);
        this.withHoldingVat.set(withHoldingVat);
        return this;
    }

    @Override
    public BigDecimal getTotalExpense() {

        log.debug("Returning a total expenses amount of KES {}", total.get());
        return total.get();
    }

    @Override
    public PaymentModel setTotalExpense(BigDecimal total) {

        log.debug("Total amount payable set as :" + total);
        this.total.set(total);
        return this;
    }

    public BigDecimal getToPayee() {

        log.debug("Returning amount payable to payee KES : {}", toPayee.get());
        return toPayee.get();
    }

    @Override
    public PaymentModel setToPayee(BigDecimal toPayee) {

        log.debug("Amount payable to payee set as : " + toPayee);
        this.toPayee.set(toPayee);
        return this;
    }

    public BigDecimal getWithholdingTax() {

        log.debug("Returning the withholding tax amount KES : {}", withHoldingTax.get());
        return withHoldingTax.get();
    }

    @Override
    public PaymentModel setWithholdingTax(BigDecimal withHoldingTax) {

        log.debug("Withholding tax set as : ", withHoldingTax);
        this.withHoldingTax.set(withHoldingTax);
        return this;
    }

    public BigDecimal getWithholdingVat() {

        log.debug("Returning the withholding vat amount KES : {}", withHoldingVat.get());
        return withHoldingVat.get();
    }

    public BigDecimal getToPrepay() {

        log.debug("Returning amount of {} as prepayment", toPrepay.get());
        return toPrepay.get();
    }

    @Override
    public PaymentModel setToPrepay(BigDecimal toPrepay) {

        log.debug("Amount to prepay set as {}", toPrepay);
        this.toPrepay.set(toPrepay);
        return this;
    }

    public void init() {

        log.debug("Instantiating the PaymentModel local fields");
        this.setAmountB4Vat(ZERO);
        this.setToPayee(ZERO);
        this.setTotalExpense(ZERO);
        this.setWithholdingTax(ZERO);
        this.setWithHoldingVat(ZERO);
    }

    public void destroy() {

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

    /**
     * Accepts visiting actors to implement additional funcitons
     *
     * @param visitor object to implement additional functions
     */
    public void accept(Visitor visitor) {

        visitor.visit(this);
    }

    /**
     * This method reviews the debits and credits to make sure that the balances are always
     * correct and balanced. Imbalances are adjusted against the total expenses
     * .
     *
     * @param reviewed model against which the current model is compared
     */
    @Override
    public DefaultPaymentModel reviewLedgerBalances(DefaultPaymentModel reviewed) {

        switch (this.toPayee.get().compareTo(reviewed.getToPayee())) {
            case 0:
                // do nothing
                break;
            case 1:
                this.total.get().subtract(this.toPayee.get().subtract(reviewed.getToPayee()));
                break;
            case -1:
                this.total.get().add(reviewed.getToPayee().subtract(this.toPayee.get()));
                break;
        }


        /*this.total.get().add(this.toPayee.get().subtract(reviewed.getToPayee()));
        this.total.get().add(this.withHoldingTax.get().subtract(reviewed.getWithholdingTax()));
        this.total.get().add(this.withHoldingVat.get().subtract(reviewed.getWithholdingVat()));*/
        return reviewed;
    }


}
