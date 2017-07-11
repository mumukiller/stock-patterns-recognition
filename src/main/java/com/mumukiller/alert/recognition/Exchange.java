package com.mumukiller.alert.recognition;

import lombok.Getter;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

/**
 * Created by Mumukiller on 20.06.2017.
 */
public enum Exchange {
    NASDAQ(LocalTime.of(9, 30), LocalTime.of(16, 00), ZoneOffset.of("-04:00"));

    @Getter private final TradingHours tradingHours;
    @Getter private final ZoneOffset offset;

    Exchange(final LocalTime open, final LocalTime close, final ZoneOffset offset) {
        this.offset = offset;
        this.tradingHours = new TradingHours(OffsetTime.of(open, offset), OffsetTime.of(close, offset));
    }
}
