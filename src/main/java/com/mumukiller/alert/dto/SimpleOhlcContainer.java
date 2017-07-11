package com.mumukiller.alert.dto;

import com.mumukiller.alert.transport.OhlcContainer;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;


/**
 * Created by Mumukiller on 05.07.2017.
 */
public class SimpleOhlcContainer implements OhlcContainer {
    @Getter private final String tool;
    @Getter private final String code;
    @Getter private final String name;

    @Getter private final double open;
    @Getter private final double high;
    @Getter private final double low;
    @Getter private final double close;

    @Getter private final double bodysize;
    @Getter private final boolean isBullish;

    @Getter private final LocalDate date;
    @Getter private final LocalTime time;

    public SimpleOhlcContainer(final String tool,
                               final String code,
                               final String name,
                               final double open,
                               final double high,
                               final double low,
                               final double close,
                               final LocalDate date,
                               final LocalTime time) {
        if (high < low){
            throw new IllegalArgumentException("Hi price should be more or equals low price");
        }

        this.tool = tool;
        this.code = code;
        this.name = name;

        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;

        this.bodysize = Math.abs(getOpen() - getClose());
        this.isBullish = getOpen() < getClose();

        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("OHLC [%s, %s, %s, %s]", open, high, low, close);
    }
}
