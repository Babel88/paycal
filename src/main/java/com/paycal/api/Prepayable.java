package com.paycal.api;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
public interface Prepayable {

    /**
     * CheckedPrepayment module: getPrepay
     * We are going to take take the total amount and apportion it
     * to the period for prepayment and hence get the amount to expense
     * <p>
     * The invoice details are to be injected into the constructor of the implementing object
     *
     * @param invoiceAmount
     * @return
     */
    //TODO to enhance prepayment algorithm
    double getPrepay(double invoiceAmount);
}
