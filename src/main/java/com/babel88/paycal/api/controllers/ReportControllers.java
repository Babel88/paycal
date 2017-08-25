package com.babel88.paycal.api.controllers;

import com.babel88.paycal.view.ResultsOutput;

public interface ReportControllers {

    /**
     * Calls user to set whether or not to have a report printed
     *
     * @return this
     */
    ReportControllers printReport();

    /**
     * Prints the report if the internal boolean flag is set to true
     *
     * @param resultsOutput containing the results on the paymentModelView
     *
     * @return this
     */
    ReportControllers forPayment(ResultsOutput resultsOutput);
}
