package com.mumukiller.alert.converter;

import com.mumukiller.alert.converter.CandlestickConverter;
import com.mumukiller.alert.converter.UnexpectedColumnsCount;
import com.mumukiller.alert.converter.UnexpectedDateTimeConversionException;
import com.mumukiller.alert.recognition.body.BodyType;
import com.mumukiller.alert.recognition.trend.TrendDirection;
import com.mumukiller.alert.transport.Candlestick;
import org.junit.Test;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mumukiller on 13.07.2017.
 */
public class CandlestickConverterTest {

    private static final CandlestickConverter DEFAULT_CONVERTER = new CandlestickConverter();

    @Test
    public void checkCustomConverter() {
        final CandlestickConverter converter = new CandlestickConverter(DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                                                                        DateTimeFormatter.ofPattern("HH:mm:ss"));

        final Candlestick candlestick =
                converter.convert("US33734X1928 [NA_ETF],60,2016-10-21,16:00:00,33.840000,33.960000,33.816000,33.927500,165000.000000");

        assertEquals(candlestick.getTool(), "US33734X1928 [NA_ETF]");
        assertEquals(candlestick.getCode(), "US33734X1928");
        assertEquals(candlestick.getName(), "unknown");
        assertEquals(candlestick.getDate(), LocalDate.parse("2016-10-21", converter.getDateFormatter()));
        assertEquals(candlestick.getTime(), LocalTime.parse("16:00:00", converter.getTimeFormatter()));
    }

    @Test(expected = UnexpectedDateTimeConversionException.class)
    public void checkExceptionOnInvalidDate() {
        DEFAULT_CONVERTER.convert("US33734X1928 [NA_ETF],60,2016-10-21,160000,33.840000,33.960000,33.816000,33.927500,165000.000000");
    }

    @Test(expected = UnexpectedDateTimeConversionException.class)
    public void checkExceptionOnInvalidTime() {
        DEFAULT_CONVERTER.convert("US33734X1928 [NA_ETF],60,20161021,16 00 00,33.840000,33.960000,33.816000,33.927500,165000.000000");
    }

    @Test(expected = UnexpectedColumnsCount.class)
    public void emptyLine() {
        DEFAULT_CONVERTER.convert("");
    }

    @Test
    public void validLine() {
        final Candlestick candlestick =
                DEFAULT_CONVERTER.convert("US33734X1928 [NA_ETF],60,20161021,160000,33.840000,33.960000,33.816000,33.927500,165000.000000");

        assertEquals(candlestick.getTool(), "US33734X1928 [NA_ETF]");
        assertEquals(candlestick.getCode(), "US33734X1928");
        assertEquals(candlestick.getName(), "unknown");
        assertEquals(candlestick.getDate(), LocalDate.parse("20161021", DEFAULT_CONVERTER.getDateFormatter()));
        assertEquals(candlestick.getTime(), LocalTime.parse("160000", DEFAULT_CONVERTER.getTimeFormatter()));
        assertEquals(candlestick.getOpen(), 33.840000, 0.0);
        assertEquals(candlestick.getHigh(), 33.960000, 0.0);
        assertEquals(candlestick.getLow(), 33.816000, 0.0);
        assertEquals(candlestick.getClose(), 33.927500, 0.0);
        assertEquals(candlestick.getBodyType(), BodyType.UNKNOWN);
        assertEquals(candlestick.getTrendDirection(), TrendDirection.LATERAL);
    }

    @Test
    public void validateConversionToExtendedCandlestick() {
        final Candlestick candlestick =
                DEFAULT_CONVERTER.convert("US33734X1928 [NA_ETF],60,20161021,160000,33.840000,33.960000,33.816000,33.927500,165000.000000");

        final Candlestick extended = DEFAULT_CONVERTER.convert(candlestick, BodyType.DOJI, TrendDirection.DOWN);
        assertEquals(candlestick.getTool(), "US33734X1928 [NA_ETF]");
        assertEquals(candlestick.getCode(), "US33734X1928");
        assertEquals(candlestick.getName(), "unknown");
        assertEquals(candlestick.getDate(), extended.getDate());
        assertEquals(candlestick.getTime(), extended.getTime());
        assertEquals(candlestick.getOpen(), extended.getOpen(), 0.0);
        assertEquals(candlestick.getHigh(), extended.getHigh(), 0.0);
        assertEquals(candlestick.getLow(), extended.getLow(), 0.0);
        assertEquals(candlestick.getClose(), extended.getClose(), 0.0);

        assertEquals(extended.getBodyType(), BodyType.DOJI);
        assertEquals(extended.getTrendDirection(), TrendDirection.DOWN);

        assertEquals(candlestick.getBodyType(), BodyType.UNKNOWN);
        assertEquals(candlestick.getTrendDirection(), TrendDirection.LATERAL);
    }

}
