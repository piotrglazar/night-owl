package com.piotrglazar.nightowl.converters;

import com.piotrglazar.nightowl.coordinates.Longitude;
import junitparams.converters.ConversionFailedException;
import junitparams.converters.ParamConverter;

public class LongitudeConverter implements ParamConverter<Longitude> {
    @Override
    public Longitude convert(final Object param, final String options) throws ConversionFailedException {
        return new Longitude(Double.parseDouble(param.toString()));
    }
}
