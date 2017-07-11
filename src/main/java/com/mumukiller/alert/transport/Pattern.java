package com.mumukiller.alert.transport;

import com.mumukiller.alert.recognition.pattern.PatternType;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Mumukiller on 05.07.2017.
 */
public interface Pattern {
    LocalTime getStartAtTime();

    LocalDate getStartAtDate();

    LocalTime getEndAtTime();

    LocalDate getEndAtDate();

    PatternType getPatternType();
}
