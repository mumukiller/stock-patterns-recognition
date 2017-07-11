package com.mumukiller.alert.service;

import com.mumukiller.alert.recognition.Exchange;
import com.mumukiller.alert.recognition.TradingStatus;
import org.junit.Test;

import java.time.*;

import static java.time.ZonedDateTime.now;
import static org.junit.Assert.assertEquals;

/**
 * Created by Mumukiller on 21.06.2017.
 */
public class TradingHoursServiceTest {

    private static final ZoneId UTC = ZoneId.of("UTC");

    @Test
    public void defaultClock() {
        final TradingHoursService aware = new TradingHoursServiceImpl();

        final OffsetDateTime offsetDateTime = now(Exchange.NASDAQ.getOffset()).toOffsetDateTime();
        final OffsetTime offsetTime = offsetDateTime.toOffsetTime();
        final TradingStatus status =
                offsetTime.isAfter(Exchange.NASDAQ.getTradingHours().getOpenAt()) &&
                offsetTime.isBefore(Exchange.NASDAQ.getTradingHours().getCloseAt()) ?
                TradingStatus.OPENED :
                TradingStatus.CLOSED;

        assertEquals(status, aware.statusOf(Exchange.NASDAQ));
    }

    @Test
    public void midnight() {
        final TradingHoursService
                aware = new TradingHoursServiceImpl(Clock.fixed(Instant.parse("2017-01-01T00:00:00.00Z"), UTC));

        assertEquals(TradingStatus.CLOSED, aware.statusOf(Exchange.NASDAQ));
    }

    @Test
    public void at_13_29() {
        final TradingHoursService
                aware = new TradingHoursServiceImpl(Clock.fixed(Instant.parse("2017-01-01T13:29:00.00Z"), UTC));

        assertEquals(TradingStatus.CLOSED, aware.statusOf(Exchange.NASDAQ));
    }

    @Test
    public void at_13_30() {
        final TradingHoursService
                aware = new TradingHoursServiceImpl(Clock.fixed(Instant.parse("2017-01-01T13:30:00.00Z"), UTC));

        assertEquals(TradingStatus.OPENED, aware.statusOf(Exchange.NASDAQ));
    }

    @Test
    public void at_00_00() {
        final TradingHoursService
                aware = new TradingHoursServiceImpl(Clock.fixed(Instant.parse("2017-01-01T00:00:00.00Z"), UTC));

        assertEquals(TradingStatus.CLOSED, aware.statusOf(Exchange.NASDAQ));
    }

    @Test
    public void midday() {
        final TradingHoursService
                aware = new TradingHoursServiceImpl(Clock.fixed(Instant.parse("2017-01-01T12:00:00.00Z"), UTC));

        assertEquals(TradingStatus.CLOSED, aware.statusOf(Exchange.NASDAQ));
    }
}
