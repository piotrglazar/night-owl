package com.piotrglazar.nightowl.converters;

import junitparams.converters.ConversionFailedException;
import junitparams.converters.ParamConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalDateTimeConverter implements ParamConverter<LocalDateTime> {
    @Override
    public LocalDateTime convert(final Object param, final String options) throws ConversionFailedException {
        final LocalDate date = LocalDate.parse(param.toString());
        return LocalDateTime.of(date, LocalTime.now());
    }
}
