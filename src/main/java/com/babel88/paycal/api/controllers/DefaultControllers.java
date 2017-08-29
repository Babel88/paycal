package com.babel88.paycal.api.controllers;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.ResultsViewer;
import com.babel88.paycal.config.factory.ControllerFactory;
import com.babel88.paycal.config.factory.GeneralFactory;
import com.babel88.paycal.config.factory.ModelFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;

/**
 * Created by edwin.njeru on 28/08/2017.
 */
public interface DefaultControllers {

    InvoiceDetails invoice = GeneralFactory.getInstance().createInvoice();
    ResultsViewer resultsViewer = ModelViewFactory.getInstance().createResultsViewer();
    DefaultPaymentModel paymentModel = ModelFactory.getInstance().createPaymentModel();
    ReportControllers reportController = ControllerFactory.getInstance().createReportController();


    void runCalculation();

    void updateTotalExpense();

    void updateToPayee();

    void updateWithholdingTax();

    void updateWithholdingVat();

    void updateToPrepay();

    DefaultPaymentModel getPaymentModel();
}
