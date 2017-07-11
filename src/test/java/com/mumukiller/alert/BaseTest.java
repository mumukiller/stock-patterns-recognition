package com.mumukiller.alert;

import com.mumukiller.alert.dto.CandlestickDto;
import com.mumukiller.alert.dto.StockDto;
import com.mumukiller.alert.recognition.body.BodyType;
import com.mumukiller.alert.recognition.trend.TrendDirection;
import com.mumukiller.alert.transport.Candlestick;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Mumukiller on 18.07.2017.
 */
public class BaseTest {
    private static final LocalDate ANY_LOCAL_DATE = LocalDate.of(1, 1, 1);
    private static final LocalTime ANY_LOCAL_TIME = LocalTime.of(1, 1);

    public StockDto buildStock(final double open, final double high, final double low, final double close) {
        return new StockDto("ANY", "ANY", "ANY", open, high, low, close, 1.0, 1.0,
                            ANY_LOCAL_DATE, ANY_LOCAL_TIME);
    }

    public Candlestick buildCandlestick(final double open, final double close, final double high, final double low,
                                         final BodyType bodyType, final TrendDirection direction){
        return new CandlestickDto("ANY", "ANY", "ANY", open, high, low, close, ANY_LOCAL_DATE, ANY_LOCAL_TIME, bodyType, direction);
    }
}
