package com.mumukiller.alert.converter;

import com.mumukiller.alert.dto.CandlestickDto;
import com.mumukiller.alert.recognition.body.BodyType;
import com.mumukiller.alert.recognition.trend.TrendDirection;
import com.mumukiller.alert.transport.Candlestick;
import com.mumukiller.alert.transport.OhlcContainer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Mumukiller on 11.07.2017.
 */
@Slf4j
@Service
public class CandlestickConverter {
    private static final int DEFAULT_COLUMNS_COUNT = 9;

    @Getter private final DateTimeFormatter dateFormatter;
    @Getter private final DateTimeFormatter timeFormatter;

    public CandlestickConverter(final DateTimeFormatter dateFormatter,
                                final DateTimeFormatter timeFormatter) {
        this.dateFormatter = dateFormatter;
        this.timeFormatter = timeFormatter;
    }

    public CandlestickConverter() {
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.timeFormatter = DateTimeFormatter.ofPattern("HHmmss");
    }

    public Candlestick convert(final OhlcContainer candlestick, final BodyType bodyType, final TrendDirection trendDirection) {
        return new CandlestickDto(candlestick.getTool(),
                                  candlestick.getCode(),
                                  candlestick.getName(),
                                  candlestick.getOpen(),
                                  candlestick.getHigh(),
                                  candlestick.getLow(),
                                  candlestick.getClose(),
                                  candlestick.getDate(),
                                  candlestick.getTime(),
                                  bodyType,
                                  trendDirection);
    }

    public Candlestick convert(final String line){
        final String[] terms = line.split(",");

        if (terms.length != DEFAULT_COLUMNS_COUNT){
            throw new UnexpectedColumnsCount(terms.length, DEFAULT_COLUMNS_COUNT);
        }

        final String tool = terms[0];
        final String code = tool.split(" ")[0];
        return new CandlestickDto(tool,
                                  code,
                                  "unknown",
                                  Double.valueOf(terms[4]),
                                  Double.valueOf(terms[5]),
                                  Double.valueOf(terms[6]),
                                  Double.valueOf(terms[7]),
                                  parseDateSafely(terms[2]),
                                  parseTimeSafely(terms[3]),
                                  BodyType.UNKNOWN,
                                  TrendDirection.LATERAL);
    }

    private LocalDate parseDateSafely(final String date){
        try{
            return LocalDate.parse(date, getDateFormatter());
        } catch (Exception e){
            log.error("Unable to parse LocalDate from string {} using formatter {}", date, getDateFormatter());
            throw new UnexpectedDateTimeConversionException(date, getDateFormatter().toString());
        }
    }

    private LocalTime parseTimeSafely(final String date){
        try{
            return LocalTime.parse(date, getTimeFormatter());
        } catch (Exception e){
            log.error("Unable to parse LocalTime from string {} using formatter {}", date, getTimeFormatter());
            throw new UnexpectedDateTimeConversionException(date, getTimeFormatter().toString());
        }
    }
}
