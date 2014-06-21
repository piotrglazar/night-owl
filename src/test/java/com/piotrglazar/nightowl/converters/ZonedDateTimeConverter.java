package com.piotrglazar.nightowl.converters;

import junitparams.converters.ConversionFailedException;
import junitparams.converters.ParamConverter;

import java.time.ZonedDateTime;

public class ZonedDateTimeConverter implements ParamConverter<ZonedDateTime> {
    @Override
    public ZonedDateTime convert(final Object param, final String options) throws ConversionFailedException {
        return ZonedDateTime.parse(param.toString());
    }
}
