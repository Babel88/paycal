package com.ghacupha.paycal.api.controllers;

import com.ghacupha.paycal.models.ResultsOutput;

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
     * @return this
     */
    ReportControllers forPayment(ResultsOutput resultsOutput);
}
