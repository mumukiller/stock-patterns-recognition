package com.mumukiller.alert.service;

import lombok.Getter;

/**
 * Created by Mumukiller on 14.07.2017.
 */
public class StockIsNotFoundException extends RuntimeException {

    @Getter private final String code;

    public StockIsNotFoundException(final String code) {
        this.code = code;
    }
}
