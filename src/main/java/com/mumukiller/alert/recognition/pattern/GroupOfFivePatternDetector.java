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
public interface GroupOfFivePatternDetector {

    default Optional<Pattern> detectGroupOfFivePattern(final List<? extends Candlestick> candlesticks){
        if (candlesticks.size() < 5){
            return Optional.empty();
        }

        return detectGroupOfFivePattern(candlesticks.get(0),
                                        candlesticks.get(1),
                                        candlesticks.get(2),
                                        candlesticks.get(3),
                                        candlesticks.get(4));
    }

    default Optional<Pattern> detectGroupOfFivePattern(final Candlestick candlestick1,
                                                       final Candlestick candlestick2,
                                                       final Candlestick candlestick3,
                                                       final Candlestick candlestick4,
                                                       final Candlestick candlestick5){

        if(candlestick1.getTrendDirection() == TrendDirection.DOWN){
            return detectGroupOfFiveDownTrendCandlestickPattern(candlestick1, candlestick2, candlestick3, candlestick4, candlestick5);
        }

        if(candlestick1.getTrendDirection() == TrendDirection.UPPER){
            return detectGroupOfFiveUpTrendCandlestickPattern(candlestick1, candlestick2, candlestick3, candlestick4, candlestick5);
        }

        return Optional.empty();
    }

    default Optional<Pattern> detectGroupOfFiveDownTrendCandlestickPattern(final Candlestick candlestick1,
                                                                           final Candlestick candlestick2,
                                                                           final Candlestick candlestick3,
                                                                           final Candlestick candlestick4,
                                                                           final Candlestick candlestick5){

        if(candlestick1.isBearish() &&
                candlestick2.isBearish() &&
                candlestick3.isBearish() &&
                candlestick4.isBearish() &&
                candlestick5.isBullish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() == BodyType.SHORT &&
                candlestick3.getBodyType() == BodyType.SHORT &&
                candlestick4.getBodyType() == BodyType.SHORT &&
                (candlestick5.getBodyType() == BodyType.LONG || candlestick5.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getOpen() < candlestick1.getClose() &&
                candlestick5.getClose() < candlestick1.getClose() &&
                candlestick5.getClose() > candlestick2.getOpen()){
            return Optional.of(PatternDto.of(PatternType.BREAKAWAY_BULL, candlestick1, candlestick5));
        }

        return Optional.empty();
    }

    default Optional<Pattern> detectGroupOfFiveUpTrendCandlestickPattern(final Candlestick candlestick1,
                                                                         final Candlestick candlestick2,
                                                                         final Candlestick candlestick3,
                                                                         final Candlestick candlestick4,
                                                                         final Candlestick candlestick5){

        if(candlestick1.isBullish() &&
                candlestick2.isBullish() &&
                candlestick3.isBullish() &&
                candlestick4.isBullish() &&
                candlestick5.isBearish() &&
                (candlestick1.getBodyType() == BodyType.LONG || candlestick1.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getBodyType() == BodyType.SHORT &&
                candlestick3.getBodyType() == BodyType.SHORT &&
                candlestick4.getBodyType() == BodyType.SHORT &&
                (candlestick5.getBodyType() == BodyType.LONG || candlestick5.getBodyType() == BodyType.MARIBOZU_LONG) &&
                candlestick2.getOpen() > candlestick1.getClose() &&
                candlestick5.getClose() > candlestick1.getClose() &&
                candlestick5.getClose() < candlestick2.getOpen()){
            return Optional.of(PatternDto.of(PatternType.BREAKAWAY_BEAR, candlestick1, candlestick5));
        }

        return Optional.empty();
    }


}
