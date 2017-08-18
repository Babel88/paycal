package com.babel88.paycal.logic;

import com.babel88.paycal.api.PrepaymentDetails;
import com.babel88.paycal.config.GeneralConfigurations;

import java.math.BigDecimal;

import static java.lang.System.out;

public class PrepayableTest {

    private AbstractPrepayment abstractPrepayment;

    public PrepayableTest() {

        this.abstractPrepayment =
                new AbstractPrepayment(new GeneralConfigurations())
                        .setPrepaymentDetails(new PrepaymentDetails() {
                            @Override
                            public String getInvoiceStartDate() {
                                return "01.01.2017";
                            }

                            @Override
                            public String getInvoiceRefDate() {
                                return "09.08.2017";
                            }

                            @Override
                            public String getInvoiceEndDate() {
                                return "31.12.2017";
                            }
                        });
    }

    private void doPrepayment(){

        BigDecimal prepayment =
                abstractPrepayment.calculatePrepayment(BigDecimal.valueOf(100000));

        out.println("Prepayment amount for 100,000 is : " + prepayment);
    }

    public static void main(String[] args){

        new PrepayableTest().doPrepayment();
    }
}
