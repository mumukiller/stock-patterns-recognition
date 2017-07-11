package com.mumukiller.alert.service;

import com.mumukiller.alert.recognition.Exchange;
import com.mumukiller.alert.recognition.TradingHours;
import com.mumukiller.alert.recognition.TradingStatus;
import lombok.Getter;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

import static java.time.ZonedDateTime.now;

/**
 * Created by Mumukiller on 20.06.2017.
 */
public class TradingHoursServiceImpl implements TradingHoursService {

    @Getter private final Clock clock;

    public TradingHoursServiceImpl(final Clock clock) {
        this.clock = clock;
    }

    public TradingHoursServiceImpl() {
        this.clock = Clock.systemUTC();
    }

    @Override
    public TradingStatus statusOf(final Exchange stockExchange) {
        final OffsetDateTime now = now(getClock()).withZoneSameInstant(stockExchange.getOffset())
                .toOffsetDateTime();
        final OffsetTime offsetTime = now.toOffsetTime();

        final TradingHours tradingHours = stockExchange.getTradingHours();
        return (( offsetTime.isAfter(tradingHours.getOpenAt()) || offsetTime.isEqual(tradingHours.getOpenAt()))
                && offsetTime.isBefore(tradingHours.getCloseAt()))
                ? TradingStatus.OPENED
                : TradingStatus.CLOSED;
    }
}
