package com.ghacupha.paycal.controllers;

import com.ghacupha.paycal.api.controllers.ReportControllers;
import com.ghacupha.paycal.api.view.FeedBack;
import com.ghacupha.paycal.config.factory.GeneralFactory;
import com.ghacupha.paycal.config.factory.ModelViewFactory;
import com.ghacupha.paycal.models.ResultsOutput;
import com.ghacupha.paycal.view.reporting.PaymentAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportsController implements ReportControllers {

    private static ReportControllers instance = new ReportsController();
    private final Logger log = LoggerFactory.getLogger(ReportsController.class);
    private FeedBack feedBack;
    private PaymentAdvice paymentAdvice;
    private Boolean printReport;

    private ReportsController() {

        log.debug("Creating a reports controller object");

        log.debug("Fetching dependants from factory");

        feedBack = GeneralFactory.createFeedback();

        paymentAdvice = ModelViewFactory.createPaymentAdvice();
    }

    public static ReportControllers getInstance() {
        return instance;
    }

    // new feature, for printing reports
    @Override
    public ReportControllers printReport() {

        printReport = feedBack.printReport();

        return this;
    }


    @Override
    public ReportControllers forPayment(ResultsOutput resultsOutput) {

        paymentAdvice.setPrintAdvice(printReport).forPayment(resultsOutput);

        return this;
    }
}
