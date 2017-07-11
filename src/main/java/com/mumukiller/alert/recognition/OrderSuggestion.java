package com.mumukiller.alert.recognition;

import com.mumukiller.alert.recognition.pattern.PatternType;
import lombok.Getter;

import java.util.EnumSet;

/**
 * Created by Mumukiller on 14.07.2017.
 */
public enum OrderSuggestion {
    UNKNOWN(EnumSet.noneOf(PatternType.class)),
    NEUTRAL(EnumSet.of(PatternType.UNKNOWN)),

    BUY(EnumSet.of(PatternType.THREE_LINE_STRIKE_BULL,
                   PatternType.UPSIDE_TASUKI_GAP,
                   PatternType.UPSIDE_GAP_THREE_METHODS,
                   PatternType.KICKING_BULL,
                   PatternType.BREAKAWAY_BULL,
                   PatternType.CONCEALING_BABY_SWALLOW,
                   PatternType.UNIQUE_THREE_RIVER_BOTTOM,
                   PatternType.THREE_START_BULL,
                   PatternType.THREE_INSIDE_UP,
                   PatternType.THREE_OUTSIDE_UP,
                   PatternType.THREE_WHITE_SOLDIERS,
                   PatternType.THREE_STAR_IN_THE_SOUTH,
                   PatternType.MORNING_STAR,
                   PatternType.MORNING_DOJI_STAR,
                   PatternType.ABANDON_BABY_BULLISH,
                   PatternType.HOMING_PIGEON,
                   PatternType.MATCHING_LOW,
                   PatternType.MEETING_LINES_BULLISH,
                   PatternType.PIERCING_PATTERN,
                   PatternType.DOJI_STAR_BULLISH,
                   PatternType.HARAMI_BULLISH,
                   PatternType.HARAMI_CROSS_BULLISH,
                   PatternType.ENGULFING_BULLISH,
                   PatternType.BELT_HOLD_BULL)),

    SELL(EnumSet.of(PatternType.THREE_LINE_STRIKE_BEAR,
                    PatternType.DOWNSIDE_TASUKI_GAP,
                    PatternType.DOWNSIDE_GAP_THREE_METHODS,
                    PatternType.KICKING_BEAR,
                    PatternType.BREAKAWAY_BEAR,
                    PatternType.IDENTICAL_THREE_CROWS,
                    PatternType.THREE_START_BEAR,
                    PatternType.THREE_OUTSIDE_DOWN,
                    PatternType.THREE_BLACK_CROWS,
                    PatternType.DELIBERATION,
                    PatternType.TWO_CROWS,
                    PatternType.BEARISH_UPSIDE_GAP_TWO_CROWS,
                    PatternType.EVENING_STAR,
                    PatternType.EVENING_DOJI_STAR,
                    PatternType.ABANDON_BABY_BEARISH,
                    PatternType.MEETING_LINES_BEARISH,
                    PatternType.DARK_CLOUD_COVER,
                    PatternType.DOJI_STAR_BEARISH,
                    PatternType.HARAMI_BEARISH,
                    PatternType.HARAMI_CROSS_BEARISH,
                    PatternType.ENGULFING_BEARISH,
                    PatternType.BELT_HOLD_BEAR,
                    PatternType.BEARISH_ON_NECK_LINE,
                    PatternType.IN_NECK_LINE,
                    PatternType.THRUSTING_LINE));

    @Getter private final EnumSet<PatternType> patternTypes;

    OrderSuggestion(final EnumSet<PatternType> patternTypes) {
        this.patternTypes = patternTypes;
    }

    public boolean containsPatternType(final PatternType patternType){
        return getPatternTypes().contains(patternType);
    }

    public static OrderSuggestion suggestionBy(final PatternType patternType){
        if (BUY.containsPatternType(patternType)){
            return BUY;
        }

        if (SELL.containsPatternType(patternType)){
            return SELL;
        }

        return NEUTRAL;
    }
}
