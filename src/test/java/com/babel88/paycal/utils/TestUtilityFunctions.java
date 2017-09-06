package com.babel88.paycal.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestUtilityFunctions {

    protected BigDecimal setAccuracy(Double amount) {

        return BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
