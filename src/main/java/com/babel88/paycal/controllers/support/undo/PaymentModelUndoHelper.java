package com.babel88.paycal.controllers.support.undo;

import com.babel88.paycal.config.factory.ModelFactory;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.PaymentModelCareTaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The application of this class will look like the example below with object name "undoHelper"
 * from the DI container.
 * The method executePrevious() is intercepted through aop which is assisted through
 * use of runtime annotations @UndoRedo, and then the method executes. If the user sets
 * true that he/she might want to redo previousExecution(), then the model is reset to
 * previous state through application of the memento design pattern and a simple list
 * container
 * Then the previous execution is rehearsed
 * <p>
 * Methods marked with @UndoRedo will be rehearseable, including the caller of the undoRedo
 * helper make it possible to redo what we have undone...
 * <p>
 * Example
 * <p>
 * //@UndoRedo
 * private void undoRedo() {
 * <p>
 * undo=userInput.equalsIgnoreCase("yes");
 * <p>
 * undoHelper.setUndo(undo).invoke(paymentModel);
 * <p>
 * if(undo){
 * previousExecution()
 * }
 * }
 */

public class PaymentModelUndoHelper {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private int calculationSteps = 0;
    private boolean undo = false;
    private boolean redo = false;
    private PaymentModelCareTaker paymentModelCareTaker;

    private static PaymentModelUndoHelper instance = new PaymentModelUndoHelper();

    public PaymentModelUndoHelper() {

        log.debug("PaymentModelUndoHelper created");
        paymentModelCareTaker = ModelFactory.getInstance().createPaymentModelCaretaker();
    }

    public static PaymentModelUndoHelper getInstance() {
        return instance;
    }

    public PaymentModel invoke(PaymentModel paymentModel) {
        log.debug("Saving state to memento object");
        paymentModelCareTaker.add(paymentModel.saveStateToMemento());
        calculationSteps++;
        if (undo) {
            paymentModel.getStateFromMemento(paymentModelCareTaker.get(--calculationSteps));
        } else if (redo) {
            paymentModel.getStateFromMemento(paymentModelCareTaker.get(calculationSteps++));
        }

        return paymentModel;
    }

    public PaymentModelUndoHelper setUndo(boolean undo) {
        this.undo = undo;
        return this;
    }
}
