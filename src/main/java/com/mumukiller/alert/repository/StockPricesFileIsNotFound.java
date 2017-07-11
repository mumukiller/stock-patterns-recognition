package com.mumukiller.alert.repository;

/**
 * Created by Mumukiller on 14.07.2017.
 */
public class StockPricesFileIsNotFound extends RuntimeException {
    private final String name;

    public StockPricesFileIsNotFound(final Throwable cause, final String name) {
        super(cause);
        this.name = name;
    }
}
