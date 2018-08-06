package com.babel88.paycal.api.view;

/**
 * Created by edwin.njeru on 10/08/2017.
 */
public interface FeedBack {
    void printIntro();

    void mainPrompt();

    void payeeName();

    void vatRate();

    void withHoldingTaxRate();

    void initialMenu();

    void invoiceAmount();

    void vatAmount();

    void withHoldingTaxAmount();

    void dateInfo(String date);

    void withHoldingVatRate();

    Boolean printReport();


}
