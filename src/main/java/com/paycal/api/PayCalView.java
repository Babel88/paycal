package com.paycal.api;

/**
 * Created by edwin.njeru on 09/08/2017.
 */
public interface PayCalView {
    void displayResults(Double total,
                        Double vatWithheld,
                        Double withholdingTax,
                        Double toPrepay,
                        Double toPayee);
}
