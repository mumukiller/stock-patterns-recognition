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
public interface GroupOfTwoPatternDetector {

    default Optional<Pattern> detectGroupOfTwoPattern(final List<? extends Candlestick> candlesticks){
        if (candlesticks.size() < 2){
            return Optional.empty();
        }

        return detectGroupOfTwoPattern(candlesticks.get(0), candlesticks.get(1));
    }

    default Optional<Pattern> detectGroupOfTwoPattern(final Candlestick candlestick1, final Candlestick candlestick2){

        if(candlestick1.getTrendDirection() == TrendDirection.UPPER &&
                candlestick2.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.getBodyType() == BodyType.HAMMER){
            return Optional.of(PatternDto.of(PatternType.HANGING_MAN, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.UPPER &&
                candlestick2.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.getBodyType() == BodyType.INVERTED_HUMMER){
            return Optional.of(PatternDto.of(PatternType.SHOOTING_STAR, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.DOWN &&
                candlestick2.getTrendDirection() == TrendDirection.DOWN &&
                candlestick1.isBearish() &&
                candlestick2.isBullish() &&
                candlestick1.getBodysize() < candlestick2.getBodysize() &&
                candlestick1.getClose() > candlestick2.getOpen() &&
                candlestick1.getOpen() < candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.ENGULFING_BULLISH, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.UPPER &&
                candlestick2.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.isBullish() &&
                candlestick2.isBearish() &&
                candlestick1.getBodysize() < candlestick2.getBodysize() &&
                candlestick1.getClose() < candlestick2.getOpen() &&
                candlestick1.getOpen() > candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.ENGULFING_BEARISH, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.DOWN &&
                candlestick1.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() ==  BodyType.DOJI &&
                candlestick1.getClose() < candlestick2.getOpen() &&
                candlestick1.getClose() < candlestick2.getClose() &&
                candlestick1.getOpen() > candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.HARAMI_CROSS_BULLISH, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() ==  BodyType.DOJI &&
                candlestick1.getClose() > candlestick2.getOpen() &&
                candlestick1.getClose() > candlestick2.getClose() &&
                candlestick1.getOpen() < candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.HARAMI_CROSS_BEARISH, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.DOWN &&
                candlestick1.isBearish() &&
                candlestick2.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() !=  BodyType.DOJI &&
                candlestick1.getBodysize() > candlestick2.getBodysize() &&
                candlestick1.getClose() < candlestick2.getOpen() &&
                candlestick1.getClose() < candlestick2.getClose() &&
                candlestick1.getOpen() > candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.HARAMI_BULLISH, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.isBullish() &&
                candlestick2.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() !=  BodyType.DOJI &&
                candlestick1.getBodysize() > candlestick2.getBodysize() &&
                candlestick1.getClose() > candlestick2.getOpen() &&
                candlestick1.getClose() > candlestick2.getClose() &&
                candlestick1.getOpen() < candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.HARAMI_BEARISH, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.DOWN &&
                candlestick1.isBearish() &&
                candlestick2.isBullish() &&
                candlestick2.getBodyType() == BodyType.MARIBOZU_LONG &&
                candlestick1.getBodysize() < candlestick2.getBodysize() &&
                candlestick1.getClose() > candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.BELT_HOLD_BULL, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.isBullish() &&
                candlestick2.isBearish() &&
                candlestick2.getBodyType() == BodyType.MARIBOZU_LONG &&
                candlestick1.getBodysize() < candlestick2.getBodysize() &&
                candlestick1.getClose() < candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.BELT_HOLD_BEAR, candlestick1, candlestick2));
        }

        if(candlestick1.isBearish() &&
                candlestick2.isBullish() &&
                candlestick1.getBodyType() == BodyType.MARIBOZU_LONG &&
                candlestick2.getBodyType() == BodyType.MARIBOZU_LONG &&
                candlestick1.getOpen() < candlestick2.getOpen()){
            return Optional.of(PatternDto.of(PatternType.KICKING_BULL, candlestick1, candlestick2));
        }

        if(candlestick1.isBullish() &&
                candlestick2.isBearish() &&
                candlestick1.getBodyType() == BodyType.MARIBOZU_LONG &&
                candlestick2.getBodyType() == BodyType.MARIBOZU_LONG &&
                candlestick1.getOpen() > candlestick2.getOpen()){
            return Optional.of(PatternDto.of(PatternType.KICKING_BEAR, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.DOWN &&
                candlestick2.getTrendDirection() == TrendDirection.DOWN &&
                candlestick1.isBearish() &&
                candlestick2.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick1.getClose() < candlestick2.getClose() &&
                candlestick1.getOpen() > candlestick2.getOpen()){
            return Optional.of(PatternDto.of(PatternType.HOMING_PIGEON, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.DOWN &&
                candlestick2.getTrendDirection() == TrendDirection.DOWN &&
                candlestick1.isBearish() &&
                candlestick2.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick1.getClose() == candlestick2.getClose() &&
                candlestick1.getBodysize() > candlestick2.getBodysize()){
            return Optional.of(PatternDto.of(PatternType.MATCHING_LOW, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.DOWN &&
                candlestick2.getTrendDirection() == TrendDirection.DOWN &&
                candlestick1.isBearish() &&
                candlestick2.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick2.getBodyType() == BodyType.LONG || candlestick2.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick1.getClose() == candlestick2.getClose() &&
                candlestick1.getBodysize() < candlestick2.getBodysize() &&
                candlestick1.getLow() > candlestick2.getOpen()){
            return Optional.of(PatternDto.of(PatternType.MEETING_LINES_BULLISH, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.UPPER &&
                candlestick2.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.isBullish() &&
                candlestick2.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick2.getBodyType() == BodyType.LONG || candlestick2.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick1.getClose() == candlestick2.getClose() &&
                candlestick1.getBodysize() < candlestick2.getBodysize() &&
                candlestick1.getHigh() < candlestick2.getOpen()){
            return Optional.of(PatternDto.of(PatternType.MEETING_LINES_BEARISH, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.DOWN &&
                candlestick2.getTrendDirection() == TrendDirection.DOWN &&
                candlestick1.isBearish() &&
                candlestick2.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick2.getBodyType() == BodyType.LONG || candlestick2.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getClose() > (candlestick1.getClose() + candlestick1.getOpen()) / 2 &&
                candlestick2.getOpen() < candlestick1.getLow() &&
                candlestick2.getClose() <= candlestick1.getOpen()){
            return Optional.of(PatternDto.of(PatternType.PIERCING_PATTERN, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.UPPER &&
                candlestick2.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.isBullish() &&
                candlestick2.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick2.getBodyType() == BodyType.LONG || candlestick2.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getClose() < (candlestick1.getClose() + candlestick1.getOpen()) / 2 &&
                candlestick1.getHigh() < candlestick2.getOpen() &&
                candlestick2.getClose() >= candlestick1.getOpen()){
            return Optional.of(PatternDto.of(PatternType.DARK_CLOUD_COVER, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.DOWN &&
                candlestick1.isBearish() &&
                ((candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                        candlestick2.getBodyType() ==  BodyType.DOJI) &&
                candlestick1.getClose() > candlestick2.getOpen() &&
                candlestick1.getClose() > candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.DOJI_STAR_BULLISH, candlestick1, candlestick2));
        }

        if(candlestick1.getTrendDirection() == TrendDirection.UPPER &&
                candlestick1.isBullish() &&
                ((candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                        candlestick2.getBodyType() ==  BodyType.DOJI) &&
                candlestick1.getClose() < candlestick2.getOpen() &&
                candlestick1.getClose() < candlestick2.getClose()){
            return Optional.of(PatternDto.of(PatternType.DOJI_STAR_BEARISH, candlestick1, candlestick2));
        }


        return Optional.empty();
    }

}
