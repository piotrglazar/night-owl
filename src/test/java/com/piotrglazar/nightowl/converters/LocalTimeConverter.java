package com.piotrglazar.nightowl.converters;

import junitparams.converters.ConversionFailedException;
import junitparams.converters.ParamConverter;

import java.time.LocalTime;

public class LocalTimeConverter implements ParamConverter<LocalTime> {
    @Override
    public LocalTime convert(final Object param, final String options) throws ConversionFailedException {
        return LocalTime.parse(param.toString());
    }
}
