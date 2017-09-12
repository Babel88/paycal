package com.babel88.paycal.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestUtilityFunctions {

    protected BigDecimal bd(Double amount) {

        return BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    protected BigDecimal bd(String value){

        return new BigDecimal("value").setScale(2, RoundingMode.HALF_EVEN);
    }

    protected Double pd(String value){

        return Double.parseDouble(value);
    }
}
