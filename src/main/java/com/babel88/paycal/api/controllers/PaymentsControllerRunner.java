package com.babel88.paycal.api.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.models.TTArguments;

/**
 * In the beginning the  controllers were hard to group into a common interface since the operations
 * were too varied.
 * However as development continued, one breakthrough was having one single method than ran all single
 * method all the while updating the payment model. This method would then pass the model into the view
 * and the view would then print a table of calculated numbers.
 * Because the prepayment concept has turned out to be general to all types of payments, rather than
 * specific type of payment, it was necessary to implement the prepayment service in each controller,
 * which led to hideous amounts of code duplications and opportunities for errors.
 * The goals of the application design have changed to correct unforeseen anti-patterns, and a number of
 * classes have had to be deprecated. This however would mean that we need a rewrite of most of the controller
 * package, which is a massive feat on its own.
 * One attempt though has accomplished what has taken months of planning, and its the emergence of the prepayment
 * delegate. It was refactored from the PaymentsControllerRunner (which is one of the child classes of this interface)
 * as a delegate to perform the prepayment service, and also to adjust the total expenses to accommodate the
 * prepayment amount.
 * The delegate performed wonderfully and passed all (non-automated) tests. But it's limitation is that it's
 * delegator could only be the PaymentsControllerRunnerImpl. I sought to include other controllers without
 * success and almost resoughted to rewriting the whole controller package.
 * Another option was to perform reflection inside
 * the delegate and created objects whose pointer would be compatible to the PaymentsControllerRunner.
 * This would then result in failure of one of the design goals which is to maintain speedy performance of
 * calculations.
 * But an opportunity presents itself in using this interface, in that the existing controllers will not have to be rewritten
 * however allowing them all to be classified under the same class.
 * All future controllers will be quick to implement, since they will only need to implement the DefaultControllers
 * interface, and extend the PaymentsControllerRunner class immediately inheriting the benefit of the PrepaymentDelegate.
 * However existing classes do not need to be rewritten since they already work, and they have been tested too.
 * They only need to implement this interface, and they will be able to delegate the prepayment service to the
 * prepayment delegate.
 * That is the reason for the seemingly needless existence of this curious interface
 * <p>
 * Created by edwin.njeru on 30/08/2017.
 */
public interface PaymentsControllerRunner {

    /**
     * Pulls all methods for updating the payment model into a reversible loop
     * and delegates the prepayment service implementation to the prepayment
     * delegate
     */
    void runCalculation();

    /**
     * Retuns the DefaultPaymentModel currently in the delegator's class
     *
     * @return payment model
     */
    DefaultPaymentModel<Object> getPaymentModel();

    /**
     * Returns the PrepaymentController object currently in the delegator's class
     *
     * @return Prepayment controller object
     */
    PrepaymentController getPrepaymentController();

    TTArguments getTtArguments();
}
