package com.babel88.paycal.api.view;

import com.babel88.paycal.models.PaymentModel;

public interface Visitor {

    void visit(PaymentModel paymentModel);
}
