package com.mumukiller.alert.converter;

/**
 * Created by Mumukiller on 13.07.2017.
 */
public class UnexpectedDateTimeConversionException extends RuntimeException {
    private final String stringToConvert;
    private final String format;

    public UnexpectedDateTimeConversionException(final String stringToConvert, final String format) {
        this.stringToConvert = stringToConvert;
        this.format = format;
    }
}
