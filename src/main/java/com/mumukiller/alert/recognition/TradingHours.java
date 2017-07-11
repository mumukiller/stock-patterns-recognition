package com.mumukiller.alert.recognition;

import lombok.Getter;

import java.time.*;

/**
 * Created by Mumukiller on 20.06.2017.
 */
public class TradingHours {
    @Getter private final OffsetTime closeAt;
    @Getter private final OffsetTime openAt;

    public TradingHours(final OffsetTime openAt, final OffsetTime closeAt) {
        this.openAt = openAt;
        this.closeAt = closeAt;
    }
}
