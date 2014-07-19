package com.piotrglazar.nightowl.converters;

import com.piotrglazar.nightowl.coordinates.Latitude;
import junitparams.converters.ConversionFailedException;
import junitparams.converters.ParamConverter;

public class LatitudeConverter implements ParamConverter<Latitude> {
    @Override
    public Latitude convert(final Object param, final String options) throws ConversionFailedException {
        return new Latitude(Double.valueOf(param.toString()));
    }
}
