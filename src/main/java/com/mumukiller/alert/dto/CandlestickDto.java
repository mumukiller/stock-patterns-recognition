package com.mumukiller.alert.dto;

import com.mumukiller.alert.recognition.body.BodyType;
import com.mumukiller.alert.recognition.trend.TrendDirection;
import com.mumukiller.alert.transport.Candlestick;
import lombok.Getter;

import java.time.*;

/**
 * Created by Mumukiller on 24.06.2017.
 */
public class CandlestickDto extends SimpleOhlcContainer implements Candlestick {

    @Getter private final BodyType bodyType;
    @Getter private final TrendDirection trendDirection;

    public CandlestickDto(final String tool,
                          final String code,
                          final String name,
                          final Double open,
                          final Double high,
                          final Double low,
                          final Double close,
                          final LocalDate date,
                          final LocalTime time,
                          final BodyType bodyType,
                          final TrendDirection trendDirection) {
        super(tool, code, name, open, high, low, close, date, time);

        this.bodyType = bodyType;
        this.trendDirection = trendDirection;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s | %s", super.toString(), getBodyType(), getTrendDirection());
    }
}
