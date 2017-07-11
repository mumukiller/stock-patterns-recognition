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
public interface GroupOfFourPatternDetector {


    default Optional<Pattern> detectGroupOfFourPattern(final List<? extends Candlestick> candlesticks){
        if (candlesticks.size() < 4){
            return Optional.empty();
        }

        return detectGroupOfFourPattern(candlesticks.get(0),
                                        candlesticks.get(1),
                                        candlesticks.get(2),
                                        candlesticks.get(3));
    }

    default Optional<Pattern> detectGroupOfFourPattern(final Candlestick candlestick1,
                                                       final Candlestick candlestick2,
                                                       final Candlestick candlestick3,
                                                       final Candlestick candlestick4){

        if(candlestick1.getTrendDirection() == TrendDirection.DOWN){
            return detectGroupOfFourDownTrendCandlestickPattern(candlestick1, candlestick2, candlestick3, candlestick4);
        }

        if(candlestick1.getTrendDirection() == TrendDirection.UPPER){
            return detectGroupOfFourUpTrendCandlestickPattern(candlestick1, candlestick2, candlestick3, candlestick4);
        }

        return Optional.empty();
    }

    default Optional<Pattern> detectGroupOfFourDownTrendCandlestickPattern(final Candlestick candlestick1,
                                                                           final Candlestick candlestick2,
                                                                           final Candlestick candlestick3,
                                                                           final Candlestick candlestick4){

        if(candlestick1.isBearish() &&
                candlestick2.isBearish() &&
                candlestick3.isBearish() &&
                candlestick4.isBearish() &&
                candlestick1.getBodyType() == BodyType.MARIBOZU_LONG &&
                candlestick2.getBodyType() == BodyType.MARIBOZU_LONG &&
                candlestick3.getBodyType() == BodyType.SHORT &&
                candlestick3.getOpen() < candlestick2.getClose() &&
                candlestick3.getHigh() > candlestick2.getClose() &&
                candlestick4.getOpen() > candlestick3.getHigh() &&
                candlestick4.getClose() < candlestick3.getLow()){
            return Optional.of(PatternDto.of(PatternType.CONCEALING_BABY_SWALLOW, candlestick1, candlestick4));
        }

        if(candlestick1.isBearish() &&
                candlestick2.isBearish() &&
                candlestick3.isBearish() &&
                candlestick4.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick2.getBodyType() == BodyType.LONG || candlestick2.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick3.getBodyType() == BodyType.LONG || candlestick3.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getClose() < candlestick1.getClose() &&
                candlestick3.getClose() < candlestick2.getClose() &&
                candlestick4.getClose() > candlestick1.getOpen() &&
                candlestick4.getOpen() <= candlestick3.getClose()){
            return Optional.of(PatternDto.of(PatternType.THREE_LINE_STRIKE_BEAR, candlestick1, candlestick4));
        }

        return Optional.empty();
    }

    default Optional<Pattern> detectGroupOfFourUpTrendCandlestickPattern(final Candlestick candlestick1,
                                                                         final Candlestick candlestick2,
                                                                         final Candlestick candlestick3,
                                                                         final Candlestick candlestick4){

        if(candlestick1.isBullish() &&
                candlestick2.isBullish() &&
                candlestick3.isBullish() &&
                candlestick4.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick2.getBodyType() == BodyType.LONG || candlestick2.getBodyType() == BodyType.MARIBOZU_LONG) &&
                (candlestick3.getBodyType() == BodyType.LONG || candlestick3.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getClose() > candlestick1.getClose() &&
                candlestick3.getClose() > candlestick2.getClose() &&
                candlestick4.getClose() < candlestick1.getOpen() &&
                candlestick4.getOpen() >= candlestick3.getClose()){
            return Optional.of(PatternDto.of(PatternType.THREE_LINE_STRIKE_BULL, candlestick1, candlestick4));
        }

        return Optional.empty();
    }


}
