package com.piotrglazar.nightowl.converters;

import com.google.common.base.Preconditions;
import com.piotrglazar.nightowl.model.StarInfo;
import junitparams.converters.ConversionFailedException;
import junitparams.converters.ParamConverter;

import java.time.LocalTime;

public class StarInfoConverter implements ParamConverter<StarInfo> {
    @Override
    public StarInfo convert(final Object param, final String options) throws ConversionFailedException {
        final String stringParam = param.toString();
        final String[] tokens = stringParam.split(";");
        Preconditions.checkState(tokens.length == 2, "Expected 2 tokens, got %s in %s", tokens.length, tokens);
        return new StarInfo(LocalTime.parse(tokens[0]), Double.valueOf(tokens[1]), "spectral type");
    }
}
