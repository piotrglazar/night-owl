package com.piotrglazar.nightowl.converters;

import junitparams.converters.ConversionFailedException;
import junitparams.converters.ParamConverter;

import java.time.LocalDate;

public class LocalDateConverter implements ParamConverter<LocalDate> {
    @Override
    public LocalDate convert(final Object param, final String options) throws ConversionFailedException {
        return LocalDate.parse(param.toString());
    }
}
