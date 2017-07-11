package com.mumukiller.alert.dto;

import com.mumukiller.alert.recognition.pattern.PatternType;
import com.mumukiller.alert.transport.Candlestick;
import com.mumukiller.alert.transport.Pattern;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Mumukiller on 05.07.2017.
 */
public class PatternDto implements Pattern {
    @Getter private final LocalTime startAtTime;
    @Getter private final LocalDate startAtDate;
    @Getter private final LocalTime endAtTime;
    @Getter private final LocalDate endAtDate;
    @Getter private final PatternType patternType;

    public PatternDto(final PatternType patternType,
                      final LocalDate startAtDate,
                      final LocalTime startAtTime,
                      final LocalDate endAtDate,
                      final LocalTime endAtTime) {
        this.startAtTime = startAtTime;
        this.startAtDate = startAtDate;
        this.endAtTime = endAtTime;
        this.endAtDate = endAtDate;
        this.patternType = patternType;
    }

    public static Pattern of(final PatternType pattern,
                                final Candlestick startCandlestick,
                                final Candlestick endCandlestick) {
        return new PatternDto(pattern,
                              startCandlestick.getDate(),
                              startCandlestick.getTime(),
                              endCandlestick.getDate(),
                              endCandlestick.getTime());
    }

    public static Pattern unknown() {
        return new PatternDto(PatternType.UNKNOWN,
                              LocalDate.MIN,
                              LocalTime.MIN,
                              LocalDate.MIN,
                              LocalTime.MIN);
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", patternType, String.format("[ %s %s , %s %s ]", startAtDate, startAtTime, endAtDate, endAtTime));
    }
}
