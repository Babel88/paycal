package com.ghacupha.paycal.models;

import com.ghacupha.paycal.api.DefaultPaymentModel;
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

    private static final PaymentModel instance = new PaymentModel();
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

        log.debug("Payment model created : {}.", this.toString());

    }

    public static PaymentModel getInstance() {
        return instance;
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
        return total.get();
    }

    @Override
    public PaymentModel setTotalExpense(BigDecimal total) {

        log.debug("Total amount payable set as :" + total);
        this.total.set(total);
        return this;
    }

    public BigDecimal getToPayee() {
        return toPayee.get();
    }

    @Override
    public PaymentModel setToPayee(BigDecimal toPayee) {

        log.debug("Amount payable to payee set as : " + toPayee);
        this.toPayee.set(toPayee);
        return this;
    }

    public BigDecimal getWithholdingTax() {
        return withHoldingTax.get();
    }

    public BigDecimal getWithholdingVat() {
        return withHoldingVat.get();
    }

    @Override
    public PaymentModel setWithholdingTax(BigDecimal withHoldingTax) {

        log.debug("Withholding tax set as : ", withHoldingTax);
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

    /**
     * Creates and returns a copy of this object.  The precise meaning
     * of "copy" may depend on the class of the object. The general
     * intent is that, for any object {@code x}, the expression:
     * <blockquote>
     * <pre>
     * x.clone() != x</pre></blockquote>
     * will be true, and that the expression:
     * <blockquote>
     * <pre>
     * x.clone().getClass() == x.getClass()</pre></blockquote>
     * will be {@code true}, but these are not absolute requirements.
     * While it is typically the case that:
     * <blockquote>
     * <pre>
     * x.clone().equals(x)</pre></blockquote>
     * will be {@code true}, this is not an absolute requirement.
     *
     * By convention, the returned object should be obtained by calling
     * {@code super.clone}.  If a class and all of its superclasses (except
     * {@code Object}) obey this convention, it will be the case that
     * {@code x.clone().getClass() == x.getClass()}.
     *
     * By convention, the object returned by this method should be independent
     * of this object (which is being cloned).  To achieve this independence,
     * it may be necessary to modify one or more fields of the object returned
     * by {@code super.clone} before returning it.  Typically, this means
     * copying any mutable objects that comprise the internal "deep structure"
     * of the object being cloned and replacing the references to these
     * objects with references to the copies.  If a class contains only
     * primitive fields or references to immutable objects, then it is usually
     * the case that no fields in the object returned by {@code super.clone}
     * need to be modified.
     *
     * The method {@code clone} for class {@code Object} performs a
     * specific cloning operation. First, if the class of this object does
     * not implement the interface {@code Cloneable}, then a
     * {@code CloneNotSupportedException} is thrown. Note that all arrays
     * are considered to implement the interface {@code Cloneable} and that
     * the return type of the {@code clone} method of an array type {@code T[]}
     * is {@code T[]} where T is any reference or primitive type.
     * Otherwise, this method creates a new instance of the class of this
     * object and initializes all its fields with exactly the contents of
     * the corresponding fields of this object, as if by assignment; the
     * contents of the fields are not themselves cloned. Thus, this method
     * performs a "shallow copy" of this object, not a "deep copy" operation.
     *
     * The class {@code Object} does not itself implement the interface
     * {@code Cloneable}, so calling the {@code clone} method on an object
     * whose class is {@code Object} will result in throwing an
     * exception at run time.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not
     *                                    support the {@code Cloneable} interface. Subclasses
     *                                    that override the {@code clone} method can also
     *                                    throw this exception to indicate that an instance cannot
     *                                    be cloned.
     * @see Cloneable
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Called by the garbage collector on an object when garbage collection
     * determines that there are no more references to the object.
     * A subclass overrides the {@code finalize} method to dispose of
     * system resources or to perform other cleanup.
     * <p>
     * The general contract of {@code finalize} is that it is invoked
     * if and when the Java&trade; virtual
     * machine has determined that there is no longer any
     * means by which this object can be accessed by any thread that has
     * not yet died, except as a result of an action taken by the
     * finalization of some other object or class which is ready to be
     * finalized. The {@code finalize} method may take any action, including
     * making this object available again to other threads; the usual purpose
     * of {@code finalize}, however, is to perform cleanup actions before
     * the object is irrevocably discarded. For example, the finalize method
     * for an object that represents an input/output connection might perform
     * explicit I/O transactions to break the connection before the object is
     * permanently discarded.
     * <p>
     * The {@code finalize} method of class {@code Object} performs no
     * special action; it simply returns normally. Subclasses of
     * {@code Object} may override this definition.
     * <p>
     * The Java programming language does not guarantee which thread will
     * invoke the {@code finalize} method for any given object. It is
     * guaranteed, however, that the thread that invokes finalize will not
     * be holding any user-visible synchronization locks when finalize is
     * invoked. If an uncaught exception is thrown by the finalize method,
     * the exception is ignored and finalization of that object terminates.
     * <p>
     * After the {@code finalize} method has been invoked for an object, no
     * further action is taken until the Java virtual machine has again
     * determined that there is no longer any means by which this object can
     * be accessed by any thread that has not yet died, including possible
     * actions by other objects or classes which are ready to be finalized,
     * at which point the object may be discarded.
     * <p>
     * The {@code finalize} method is never invoked more than once by a Java
     * virtual machine for any given object.
     * <p>
     * Any exception thrown by the {@code finalize} method causes
     * the finalization of this object to be halted, but is otherwise
     * ignored.
     *
     * @throws Throwable the {@code Exception} raised by this method
     * @jls 12.6 Finalization of Class Instances
     * @see WeakReference
     * @see PhantomReference
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
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
