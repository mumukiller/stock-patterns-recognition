package com.mumukiller.alert.transport;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Mumukiller on 05.07.2017.
 */
public interface OhlcContainer extends Serializable {
    String getTool();
    String getCode();
    String getName();

    double getOpen();
    double getHigh();
    double getLow();
    double getClose();

    LocalDate getDate();
    LocalTime getTime();

    double getBodysize();

    boolean isBullish();
    default boolean isBearish() {
        return !isBullish();
    }
}
