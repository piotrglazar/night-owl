package com.piotrglazar.nightowl.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DoubleConverter {

    public double twoDecimalPlaces(double number) {
        return new BigDecimal(number).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double twoDecimalPlaces(String number) {
        return twoDecimalPlaces(Double.parseDouble(number));
    }
}
