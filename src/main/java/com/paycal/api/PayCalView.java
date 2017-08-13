package com.paycal.api;

import java.math.BigDecimal;

/**
 * Created by edwin.njeru on 09/08/2017.
 */
public interface PayCalView {
    void displayResults(BigDecimal total,
                        BigDecimal vatWithheld,
                        BigDecimal withholdingTax,
                        BigDecimal toPrepay,
                        BigDecimal toPayee);
}
