package com.grocery_store.payment.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ApplicationUtils {
    public static double convertPriceToPounds(double value) {
        BigDecimal roundedValue = new BigDecimal(value).divide(BigDecimal.valueOf(100D),2,RoundingMode.HALF_UP);
        return roundedValue.doubleValue();
    }

    public static double round(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
