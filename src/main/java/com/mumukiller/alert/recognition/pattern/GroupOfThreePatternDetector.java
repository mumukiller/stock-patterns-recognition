package com.mumukiller.alert.recognition.pattern;

import com.mumukiller.alert.dto.PatternDto;
import com.mumukiller.alert.transport.Candlestick;
import com.mumukiller.alert.transport.Pattern;
import com.mumukiller.alert.recognition.body.BodyType;
import com.mumukiller.alert.recognition.trend.TrendDirection;

import java.util.List;
import java.util.Optional;

/**
 * Created by Mumukiller on 17.06.2017.
 */
public interface GroupOfThreePatternDetector {

    default Optional<Pattern> detectGroupOfThreePattern(final List<? extends Candlestick> candlesticks){
        if (candlesticks.size() < 3){
            return Optional.empty();
        }

        return detectGroupOfThreePattern(candlesticks.get(0),
                                         candlesticks.get(1),
                                         candlesticks.get(2));
    }

    default Optional<Pattern> detectGroupOfThreePattern(final Candlestick candlestick1,
                                                        final Candlestick candlestick2,
                                                        final Candlestick candlestick3){

        if(candlestick1.getTrendDirection() == TrendDirection.DOWN){
            return detectTripleDownTrendCandlestickPattern(candlestick1, candlestick2, candlestick3);
        }

        if(candlestick1.getTrendDirection() == TrendDirection.UPPER){
            return detectTripleUpTrendCandlestickPattern(candlestick1, candlestick2, candlestick3);
        }

        return Optional.empty();
    }

    default Optional<Pattern> detectTripleDownTrendCandlestickPattern(final Candlestick candlestick1,
                                                                      final Candlestick candlestick2,
                                                                      final Candlestick candlestick3){

        if(candlestick3.getTrendDirection() == TrendDirection.DOWN &&
                candlestick1.isBearish() &&
                candlestick3.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() == BodyType.DOJI &&
                (candlestick3.getBodyType() == BodyType.LONG || candlestick3.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick3.getClose() < candlestick1.getOpen() &&
                candlestick3.getClose() > candlestick1.getClose() &&
                candlestick1.getLow() > candlestick2.getHigh() &&
                candlestick3.getLow() > candlestick2.getHigh()){
            return Optional.of(PatternDto.of(PatternType.ABANDON_BABY_BULLISH, candlestick1, candlestick3));
        }

        if(candlestick3.getTrendDirection() == TrendDirection.DOWN &&
                candlestick1.isBearish() &&
                candlestick3.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() == BodyType.SHORT &&
                (candlestick3.getBodyType() == BodyType.LONG || candlestick3.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick3.getClose() > candlestick1.getClose() &&
                candlestick3.getClose() < candlestick1.getOpen() &&
                candlestick2.getOpen() <= candlestick1.getClose()){
            return Optional.of(PatternDto.of(PatternType.MORNING_STAR, candlestick1, candlestick3));
        }

        if(candlestick3.getTrendDirection() == TrendDirection.DOWN &&
                candlestick1.isBearish() &&
                candlestick3.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() == BodyType.DOJI &&
                (candlestick3.getBodyType() == BodyType.LONG || candlestick3.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick3.getClose() > candlestick1.getClose() &&
                candlestick3.getClose() < candlestick1.getOpen() &&
                candlestick2.getOpen() <= candlestick1.getClose()){
            return Optional.of(PatternDto.of(PatternType.MORNING_DOJI_STAR, candlestick1, candlestick3));
        }

        if(candlestick1.isBullish() &&
                candlestick2.isBullish() &&
                candlestick3.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick2.getBodyType() == BodyType.LONG || candlestick2.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick3.getBodyType() == BodyType.LONG || candlestick3.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick1.getClose() > candlestick2.getClose() &&
                candlestick2.getClose() > candlestick3.getClose()){
            return Optional.of(PatternDto.of(PatternType.THREE_WHITE_SOLDIERS, candlestick1, candlestick3));
        }

        if(candlestick1.isBearish() &&
                candlestick2.isBearish() &&
                candlestick3.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick2.getBodyType() == BodyType.LONG || candlestick2.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getOpen() < candlestick1.getClose() &&
                candlestick3.getOpen() < candlestick2.getOpen() &&
                candlestick3.getOpen() > candlestick2.getClose() &&
                candlestick3.getClose() > candlestick1.getClose()){
            return Optional.of(PatternDto.of(PatternType.DOWNSIDE_GAP_THREE_METHODS, candlestick1, candlestick3));
        }

        if(candlestick1.isBearish() &&
                candlestick2.isBearish() &&
                candlestick3.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick3.getBodyType() == BodyType.SHORT || candlestick3.getBodyType() == BodyType.MARIBOZU) &&
                candlestick1.getBodysize() > candlestick2.getBodysize() &&
                candlestick1.getLow() < candlestick2.getLow() &&
                candlestick3.getLow() > candlestick2.getLow() &&
                candlestick3.getHigh() < candlestick2.getHigh() &&
                candlestick2.getClose() < candlestick2.getOpen() &&
                candlestick2.getClose() < candlestick3.getOpen()){
            return Optional.of(PatternDto.of(PatternType.THREE_STAR_IN_THE_SOUTH, candlestick1, candlestick3));
        }

        if(candlestick1.isBearish() &&
                candlestick2.isBearish() &&
                candlestick3.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() == BodyType.SHORT &&
                candlestick2.getOpen() < candlestick1.getOpen() &&
                candlestick2.getClose() > candlestick1.getClose() &&
                candlestick2.getLow() < candlestick1.getLow() &&
                candlestick3.getClose() < candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.UNIQUE_THREE_RIVER_BOTTOM, candlestick1, candlestick3));
        }

        if(candlestick1.isBearish() &&
                candlestick2.isBullish() &&
                candlestick3.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick1.getBodysize() > candlestick2.getBodysize() &&
                candlestick1.getClose() < candlestick2.getOpen() &&
                candlestick1.getClose() < candlestick2.getClose() &&
                candlestick1.getOpen() > candlestick2.getClose() &&
                candlestick3.getClose() > candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.THREE_INSIDE_UP, candlestick1, candlestick3));
        }

        if(candlestick1.isBearish() &&
                candlestick2.isBullish() &&
                candlestick3.isBullish() &&
                candlestick2.getBodysize() > candlestick1.getBodysize() &&
                candlestick3.getClose() > candlestick2.getClose() &&
                candlestick2.getClose() >= candlestick2.getOpen() &&
                candlestick1.getOpen() < candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.THREE_OUTSIDE_UP, candlestick1, candlestick3));
        }

        if(candlestick1.getBodyType() == BodyType.DOJI &&
                candlestick2.getBodyType() == BodyType.DOJI &&
                candlestick3.getBodyType() == BodyType.DOJI &&
                candlestick2.getOpen() != candlestick1.getClose() &&
                candlestick2.getClose() != candlestick3.getOpen()){
            return Optional.of(PatternDto.of(PatternType.THREE_START_BULL, candlestick1, candlestick3));
        }

        if(candlestick1.isBearish() &&
                candlestick2.isBearish() &&
                candlestick3.isBullish() &&
                (candlestick1.getBodyType() != BodyType.DOJI) &&
                (candlestick2.getBodyType() != BodyType.DOJI) &&
                candlestick2.getOpen() < candlestick1.getOpen() &&
                candlestick3.getOpen() < candlestick2.getOpen() &&
                candlestick3.getOpen() > candlestick2.getClose() &&
                candlestick3.getClose() > candlestick2.getOpen() &&
                candlestick3.getClose() < candlestick1.getClose()){
            return Optional.of(PatternDto.of(PatternType.DOWNSIDE_TASUKI_GAP, candlestick1, candlestick3));
        }

        return Optional.empty();
    }

    default Optional<Pattern> detectTripleUpTrendCandlestickPattern(final Candlestick candlestick1, final Candlestick candlestick2,
                                                   final Candlestick candlestick3){

        if(candlestick3.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.isBullish() &&
                candlestick3.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() == BodyType.DOJI &&
                (candlestick3.getBodyType() == BodyType.LONG || candlestick3.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick3.getClose() > candlestick1.getOpen() &&
                candlestick3.getClose() < candlestick1.getClose() &&
                candlestick1.getHigh() < candlestick2.getLow() &&
                candlestick3.getHigh() < candlestick2.getLow()){
            return Optional.of(PatternDto.of(PatternType.ABANDON_BABY_BEARISH, candlestick1, candlestick3));
        }

        if(candlestick3.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.isBullish() &&
                candlestick3.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() == BodyType.SHORT &&
                (candlestick3.getBodyType() == BodyType.LONG || candlestick3.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick3.getClose() < candlestick1.getClose() &&
                candlestick3.getClose() > candlestick1.getOpen() &&
                candlestick2.getOpen() >= candlestick1.getClose()){
            return Optional.of(PatternDto.of(PatternType.EVENING_STAR, candlestick1, candlestick3));
        }

        if(candlestick3.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.isBullish() &&
                candlestick3.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() == BodyType.DOJI &&
                (candlestick3.getBodyType() == BodyType.LONG || candlestick3.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick3.getClose() < candlestick1.getClose() &&
                candlestick3.getClose() > candlestick1.getOpen() &&
                candlestick2.getOpen() >= candlestick1.getClose()){
            return Optional.of(PatternDto.of(PatternType.EVENING_DOJI_STAR, candlestick1, candlestick3));
        }

        if(candlestick2.getTrendDirection() == TrendDirection.UPPER &&
                candlestick3.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.isBullish() &&
                candlestick2.isBearish() &&
                candlestick3.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick3.getBodyType() == BodyType.LONG || candlestick3.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick1.getClose() < candlestick2.getClose() &&
                candlestick3.getOpen() > candlestick2.getClose() &&
                candlestick3.getClose() < candlestick1.getClose()){
            return Optional.of(PatternDto.of(PatternType.TWO_CROWS, candlestick1, candlestick3));
        }

        if(candlestick2.getTrendDirection() == TrendDirection.UPPER &&
                candlestick3.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.isBullish() &&
                candlestick2.isBearish() &&
                candlestick3.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick1.getClose()< candlestick2.getClose() &&
                candlestick1.getClose() < candlestick3.getClose() &&
                candlestick2.getOpen() < candlestick3.getOpen() &&
                candlestick2.getClose() > candlestick3.getClose()){
            return Optional.of(PatternDto.of(PatternType.BEARISH_UPSIDE_GAP_TWO_CROWS, candlestick1, candlestick3));
        }

        if(candlestick1.isBearish() &&
                candlestick2.isBearish() &&
                candlestick3.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick2.getBodyType() == BodyType.LONG || candlestick2.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick3.getBodyType() == BodyType.LONG || candlestick3.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick1.getClose() < candlestick2.getClose() &&
                candlestick2.getClose() < candlestick3.getClose()){
            return Optional.of(PatternDto.of(PatternType.THREE_BLACK_CROWS, candlestick1, candlestick3));
        }

        if(candlestick1.isBearish() &&
                candlestick2.isBearish() &&
                candlestick3.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick2.getBodyType() == BodyType.LONG || candlestick2.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick3.getBodyType() == BodyType.LONG || candlestick3.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick1.getClose() >= candlestick2.getOpen() &&
                candlestick2.getClose() >= candlestick3.getOpen()){
            return Optional.of(PatternDto.of(PatternType.IDENTICAL_THREE_CROWS, candlestick1, candlestick3));
        }

        if(candlestick1.isBullish() &&
                candlestick2.isBullish() &&
                candlestick3.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick2.getBodyType() == BodyType.LONG || candlestick2.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getOpen() > candlestick1.getClose() &&
                candlestick3.getOpen() > candlestick2.getOpen() &&
                candlestick3.getOpen() < candlestick2.getClose() &&
                candlestick3.getClose() < candlestick1.getClose()){
            return Optional.of(PatternDto.of(PatternType.UPSIDE_GAP_THREE_METHODS, candlestick1, candlestick3));
        }

        if(candlestick1.isBullish() &&
                candlestick2.isBullish() &&
                candlestick3.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick2.getBodyType() == BodyType.LONG || candlestick2.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick3.getBodyType() == BodyType.SPINING_TOP || candlestick3.getBodyType() == BodyType.SHORT) &&
                candlestick1.getClose() > candlestick2.getOpen() &&
                candlestick2.getClose() <= candlestick3.getOpen()){
            return Optional.of(PatternDto.of(PatternType.DELIBERATION, candlestick1, candlestick3));
        }

        if(candlestick1.isBullish() &&
                candlestick2.isBearish() &&
                candlestick3.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick1.getBodysize() > candlestick2.getBodysize() &&
                candlestick3.getClose() < candlestick2.getClose() &&
                candlestick1.getClose() > candlestick2.getOpen() &&
                candlestick1.getClose() > candlestick2.getClose() &&
                candlestick1.getOpen() < candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.THREE_INSIDE_DOWN, candlestick1, candlestick3));
        }

        if(candlestick1.isBullish() &&
                candlestick2.isBearish() &&
                candlestick3.isBearish() &&
                candlestick1.getBodysize() < candlestick2.getBodysize() &&
                candlestick3.getClose() < candlestick2.getClose() &&
                candlestick1.getClose() < candlestick2.getOpen() &&
                candlestick1.getOpen() > candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.THREE_OUTSIDE_DOWN, candlestick1, candlestick3));
        }

        if(candlestick1.getBodyType() == BodyType.DOJI &&
                candlestick2.getBodyType() == BodyType.DOJI &&
                candlestick3.getBodyType() == BodyType.DOJI &&
                candlestick2.getOpen() != candlestick1.getClose() &&
                candlestick2.getClose() != candlestick3.getOpen()){
            return Optional.of(PatternDto.of(PatternType.THREE_START_BEAR, candlestick1, candlestick3));
        }

        if(candlestick1.isBullish() &&
                candlestick2.isBullish() &&
                candlestick3.isBearish() &&
                (candlestick1.getBodyType() != BodyType.DOJI) &&
                (candlestick2.getBodyType() != BodyType.DOJI) &&
                candlestick2.getOpen() > candlestick1.getOpen() &&
                candlestick3.getOpen() > candlestick2.getOpen() &&
                candlestick3.getOpen() < candlestick2.getClose() &&
                candlestick3.getClose() < candlestick2.getOpen() &&
                candlestick3.getClose() > candlestick1.getClose()){
            return Optional.of(PatternDto.of(PatternType.UPSIDE_TASUKI_GAP, candlestick1, candlestick3));
        }


        return Optional.empty();
    }


}
