package com.mumukiller.alert.converter;

/**
 * Created by Mumukiller on 13.07.2017.
 */
public class UnexpectedColumnsCount extends RuntimeException {
    private final int actualCount;
    private final int expectedCount;

    public UnexpectedColumnsCount(final int actualCount, final int expectedCount) {
        this.actualCount = actualCount;
        this.expectedCount = expectedCount;
    }
}
