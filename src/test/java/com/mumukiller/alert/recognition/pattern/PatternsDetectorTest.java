package com.mumukiller.alert.recognition.pattern;

import com.mumukiller.alert.BaseTest;
import com.mumukiller.alert.recognition.body.BodyType;
import com.mumukiller.alert.recognition.trend.TrendDirection;
import com.mumukiller.alert.transport.Candlestick;
import com.mumukiller.alert.transport.Pattern;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Mumukiller on 24.06.2017.
 */
public class PatternsDetectorTest extends BaseTest {

    private static final PatternsDetector PROCESSOR = new PatternsDetector();

    @Test
    public void empty() {
        assertTrue(PROCESSOR.scan(Stream.empty()).count() == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ofSubListWithZeroLength() {
        PROCESSOR.ofSubLists(emptyList(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ofSubListWithNegativeLength() {
        PROCESSOR.ofSubLists(emptyList(), -1);
    }

    @Test
    public void ofSubListOfEmptyList() {
        assertEquals(Stream.empty().count(), PROCESSOR.ofSubLists(emptyList(), 1).count());
    }

    @Test
    public void single() {
        assertTrue(PROCESSOR.scan(
                Stream.of(buildCandlestick(1.0, 1.0, 1.0, 1.0, BodyType.FOUR_PRICE_DOJI, TrendDirection.DOWN))).count() == 0);
    }

    @Test
    public void doubleUnknownPattern() {
        assertTrue(PROCESSOR.scan(
                Stream.of(buildCandlestick(1.0, 1.0, 1.0, 1.0, BodyType.FOUR_PRICE_DOJI, TrendDirection.DOWN),
                          buildCandlestick(1.0, 1.0, 1.0, 1.0, BodyType.FOUR_PRICE_DOJI, TrendDirection.DOWN))).count() == 0);
    }

    @Test
    public void groupsOfFiveUndefined() {
        final List<Pattern> lateralPatterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(1.0, 1.0, 1.0, 1.0, BodyType.FOUR_PRICE_DOJI, TrendDirection.LATERAL),
                                         buildCandlestick(1.0, 1.0, 1.0, 1.0, BodyType.FOUR_PRICE_DOJI, TrendDirection.LATERAL),
                                         buildCandlestick(1.0, 1.0, 1.0, 1.0, BodyType.FOUR_PRICE_DOJI, TrendDirection.LATERAL),
                                         buildCandlestick(1.0, 1.0, 1.0, 1.0, BodyType.FOUR_PRICE_DOJI, TrendDirection.LATERAL),
                                         buildCandlestick(1.0, 1.0, 1.0, 1.0, BodyType.FOUR_PRICE_DOJI, TrendDirection.LATERAL)))
                        .collect(toList());
        assertEquals(0, lateralPatterns.size());

        final List<Pattern> upperPatterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(1.0, 2.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(2.0, 3.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(3.0, 4.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(4.0, 5.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(5.0, 6.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(0, upperPatterns.size());

        final List<Pattern> downPatterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(6.0, 5.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(5.0, 4.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(4.0, 3.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(3.0, 2.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(2.0, 1.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(0, downPatterns.size());
    }

    @Test
    public void checkResultProperties() {
        final Candlestick first = buildCandlestick(10.0, 5.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN);
        final Candlestick last = buildCandlestick(3.0, 4.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN);
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(first,
                                         buildCandlestick(3.0, 2.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(10.0, 5.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(10.0, 5.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                         last))
                        .collect(toList());
        assertEquals(1, patterns.size());

        final Pattern pattern = patterns.get(0);
        assertEquals(PatternType.BREAKAWAY_BULL, pattern.getPatternType());
        assertEquals(first.getDate(), pattern.getStartAtDate());
        assertEquals(first.getTime(), pattern.getStartAtTime());
        assertEquals(last.getDate(), pattern.getEndAtDate());
        assertEquals(last.getTime(), pattern.getEndAtTime());
    }

    @Test
    public void groupsOfFiveBreakawayBull() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 5.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(3.0, 2.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(10.0, 5.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(10.0, 5.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(3.0, 4.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.BREAKAWAY_BULL, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfFiveBreakawayBear() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(1.0, 5.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(6.0, 8.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(6.5, 8.5, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(7.0, 9.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(15.0, 5.5, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.BREAKAWAY_BEAR, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfFourThreeLineStrikeBear() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 7.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(9.0, 6.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(8.0, 5.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(4.0, 11.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.THREE_LINE_STRIKE_BEAR, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfFourThreeLineStrikeBull() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(6.0, 8.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(7.0, 9.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(8.0, 10.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(11.0, 5.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.THREE_LINE_STRIKE_BULL, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfFourConcealingBabySwallow() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 5.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN),
                                         buildCandlestick(6.0, 4.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN),
                                         buildCandlestick(3.0, 2.0, 5.0, 2.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(5.5, 1.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.CONCEALING_BABY_SWALLOW, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoEngulfingBullish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(8.0, 5.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(4.0, 11.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.ENGULFING_BULLISH, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoEngulfingBearish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(5.0, 8.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(11.0, 4.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.ENGULFING_BEARISH, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoDojiStarBullish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(8.0, 5.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(4.0, 4.0, 1.0, 1.0, BodyType.DOJI, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.DOJI_STAR_BULLISH, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoDojiStarBearish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(5.0, 8.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(10.0, 10.0, 1.0, 1.0, BodyType.DOJI, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.DOJI_STAR_BEARISH, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoHaramiCrossBullish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(8.0, 4.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(5.0, 5.0, 1.0, 1.0, BodyType.DOJI, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.HARAMI_CROSS_BULLISH, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoHaramiCrossBearish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(4.0, 8.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(5.0, 5.0, 1.0, 1.0, BodyType.DOJI, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.HARAMI_CROSS_BEARISH, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoHaramiBullish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(8.0, 4.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(5.0, 5.5, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.HARAMI_BULLISH, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoHaramiBearish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(2.5, 8.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(3.5, 3.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.HARAMI_BEARISH, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoBeltHoldBullish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(12.0, 11.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(2.0, 10.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.BELT_HOLD_BULL, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoBeltHoldBearish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(4.0, 8.0, 1.0, 1.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(16.0, 11.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.BELT_HOLD_BEAR, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoKickingBearish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(12.0, 13.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.LATERAL),
                                         buildCandlestick(10.0, 9.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.LATERAL)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.KICKING_BEAR, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoKickingBullish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(8.0, 4.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.LATERAL),
                                         buildCandlestick(11.0, 16.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.LATERAL)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.KICKING_BULL, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoHomingPigeonBearish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(13.0, 7.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN),
                                         buildCandlestick(10.0, 9.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.HOMING_PIGEON, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoMatchingLowBullish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(13.0, 7.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN),
                                         buildCandlestick(10.0, 7.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.MATCHING_LOW, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoMeetingLinesBullish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 9.0, 12.0, 11.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN),
                                         buildCandlestick(6.0, 9.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.MEETING_LINES_BULLISH, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoMeetingLinesBearish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(8.0, 10.0, 12.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER),
                                         buildCandlestick(13.0, 10.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.MEETING_LINES_BEARISH, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoPiercing() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 7.5, 12.0, 7.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN),
                                         buildCandlestick(6.0, 9.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.PIERCING_PATTERN, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoDarkCloudCover() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 12.0, 12.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER),
                                         buildCandlestick(15.0, 10.0, 1.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.DARK_CLOUD_COVER, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoHangingMan() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 2.0, 16.0, 2.0, BodyType.HAMMER, TrendDirection.UPPER),
                                         buildCandlestick(9.0, 8.0, 10, 1.0, BodyType.SHORT, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.HANGING_MAN, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfTwoShootingStar() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 2.0, 16.0, 2.0, BodyType.INVERTED_HUMMER, TrendDirection.UPPER),
                                         buildCandlestick(9.0, 8.0, 10, 1.0, BodyType.SHORT, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.SHOOTING_STAR, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeAbandonBabyBullish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(16.0, 12.0, 18.0, 11.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN),
                                         buildCandlestick(1.0, 1.0, 10, 1.0, BodyType.DOJI, TrendDirection.DOWN),
                                         buildCandlestick(12.0, 14.0, 18.0, 11.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(2, patterns.size());

        final Optional<Pattern> detectedPattern = patterns.stream()
                .filter(p -> p.getPatternType() != PatternType.DOJI_STAR_BULLISH)
                .findFirst();
        assertTrue(detectedPattern.isPresent());
        assertEquals(PatternType.ABANDON_BABY_BULLISH, detectedPattern.get().getPatternType());
    }

    @Test
    public void groupsOfThreeAbandonBabyBearish() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(5.0, 7.0, 9.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER),
                                         buildCandlestick(15.0, 15.0, 15.0, 14.0, BodyType.DOJI, TrendDirection.UPPER),
                                         buildCandlestick(10.0, 6.0, 11.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(2, patterns.size());

        final Optional<Pattern> detectedPattern = patterns.stream()
                .filter(p -> p.getPatternType() != PatternType.DOJI_STAR_BEARISH)
                .findFirst();
        assertTrue(detectedPattern.isPresent());
        assertEquals(PatternType.ABANDON_BABY_BEARISH, detectedPattern.get().getPatternType());
    }

    @Test
    public void groupsOfThreeEveningStar() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 12.0, 18.0, 11.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER),
                                         buildCandlestick(13.0, 11.0, 14, 10.0, BodyType.SHORT, TrendDirection.UPPER),
                                         buildCandlestick(14.0, 11.0, 18.0, 11.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.EVENING_STAR, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeMorningStar() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(12.0, 10.0, 9.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN),
                                         buildCandlestick(9.0, 8.0, 10.0, 7.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(10.0, 11.0, 11.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.MORNING_STAR, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeEveningDojiStar() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 12.0, 18.0, 11.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER),
                                         buildCandlestick(12.0, 12.0, 14, 10.0, BodyType.DOJI, TrendDirection.UPPER),
                                         buildCandlestick(14.0, 11.0, 18.0, 11.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.EVENING_DOJI_STAR, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeMorningDojiStar() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(12.0, 11.0, 13.0, 10.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN),
                                         buildCandlestick(10.0, 10.0, 12.0, 7.0, BodyType.DOJI, TrendDirection.DOWN),
                                         buildCandlestick(10.0, 11.5, 11.0, 1.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN)))
                        .collect(toList());

        assertEquals(2, patterns.size());

        final Optional<Pattern> detectedPattern = patterns.stream()
                .filter(p -> p.getPatternType() != PatternType.DOJI_STAR_BULLISH)
                .findFirst();
        assertTrue(detectedPattern.isPresent());
        assertEquals(PatternType.MORNING_DOJI_STAR, detectedPattern.get().getPatternType());
    }

    @Test
    public void groupsOfThreeWhiteSoldier() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 12.0, 13.0, 9.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN),
                                         buildCandlestick(7.0, 9.0, 15, 5.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN),
                                         buildCandlestick(4.0, 6.0, 17.0, 2.0, BodyType.MARIBOZU_LONG, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.THREE_WHITE_SOLDIERS, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeBlackCrows() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(6.0, 5.0, 7.0, 2.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER),
                                         buildCandlestick(8.0, 7.0, 15, 5.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER),
                                         buildCandlestick(10.0, 9.0, 13.0, 8.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.THREE_BLACK_CROWS, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeIdenticalThreeBlackCrows() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(9.0, 8.0, 10.0, 2.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER),
                                         buildCandlestick(8.0, 7.0, 15, 5.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER),
                                         buildCandlestick(7.0, 6.0, 13.0, 5.0, BodyType.MARIBOZU_LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.IDENTICAL_THREE_CROWS, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeThreeStarBull() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(8.0, 8.0, 10.0, 2.0, BodyType.DOJI, TrendDirection.DOWN),
                                         buildCandlestick(7.0, 7.0, 15, 5.0, BodyType.DOJI, TrendDirection.DOWN),
                                         buildCandlestick(6.0, 6.0, 13.0, 5.0, BodyType.DOJI, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.THREE_START_BULL, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeThreeStarBear() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(9.0, 9.0, 10.0, 2.0, BodyType.DOJI, TrendDirection.UPPER),
                                         buildCandlestick(8.0, 8.0, 15, 5.0, BodyType.DOJI, TrendDirection.UPPER),
                                         buildCandlestick(7.0, 7.0, 13.0, 5.0, BodyType.DOJI, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.THREE_START_BEAR, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeDownsideTasukiGap() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(14.0, 12.5, 10.0, 2.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(12.0, 7.0, 15, 5.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(8.0, 12.3, 13.0, 5.0, BodyType.SHORT, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.DOWNSIDE_TASUKI_GAP, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeUpsideTasukiGap() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(7.0, 9.0, 10.0, 2.0, BodyType.SHORT, TrendDirection.UPPER),
                                         buildCandlestick(9.5, 11.0, 15, 5.0, BodyType.SHORT, TrendDirection.UPPER),
                                         buildCandlestick(10.0, 9.3, 13.0, 5.0, BodyType.SHORT, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.UPSIDE_TASUKI_GAP, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeDownsideGapThree() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(14.0, 12.5, 10.0, 2.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(12.0, 7.0, 15, 5.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(8.0, 15.0, 13.0, 5.0, BodyType.SHORT, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.DOWNSIDE_GAP_THREE_METHODS, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeUpsideGapThree() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(7.0, 15.0, 16.0, 2.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(16.0, 20.0, 21, 5.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(18.0, 10.0, 13.0, 5.0, BodyType.SHORT, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.UPSIDE_GAP_THREE_METHODS, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeThreeInsideUp() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(14.0, 10.0, 10.0, 2.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(11.0, 12.0, 15, 5.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(6.0, 13.0, 13.0, 5.0, BodyType.SHORT, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(2, patterns.size());

        final Optional<Pattern> detectedPattern = patterns.stream()
                .filter(p -> p.getPatternType() != PatternType.HARAMI_BULLISH)
                .findFirst();
        assertTrue(detectedPattern.isPresent());
        assertEquals(PatternType.THREE_INSIDE_UP, detectedPattern.get().getPatternType());
    }

    @Test
    public void groupsOfThreeThreeInsideDown() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 14.0, 10.0, 2.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(12.0, 11.0, 15, 5.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(13.0, 6.0, 13.0, 5.0, BodyType.SHORT, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(2, patterns.size());

        final Optional<Pattern> detectedPattern = patterns.stream()
                .filter(p -> p.getPatternType() != PatternType.HARAMI_BEARISH)
                .findFirst();
        assertTrue(detectedPattern.isPresent());
        assertEquals(PatternType.THREE_INSIDE_DOWN, detectedPattern.get().getPatternType());
    }

    @Test
    public void groupsOfThreeThreeOutsideDown() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(7.0, 10.0, 16.0, 2.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(12.0, 5.0, 21, 5.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(8.0, 4.0, 13.0, 5.0, BodyType.SHORT, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.THREE_OUTSIDE_DOWN, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeThreeOutsideUp() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 7.0, 16.0, 2.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(5.0, 12.0, 21, 5.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(4.0, 13.0, 13.0, 5.0, BodyType.SHORT, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.THREE_OUTSIDE_UP, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeThreeStarInTheSouth() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 7.0, 16.0, 2.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(8.0, 6.0, 21, 5.0, BodyType.SHORT, TrendDirection.DOWN),
                                         buildCandlestick(6.5, 5.5, 13.0, 5.2, BodyType.SHORT, TrendDirection.DOWN)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.THREE_STAR_IN_THE_SOUTH, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeDeliberation() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 14.0, 16.0, 2.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(5.0, 6.0, 21, 5.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(7.0, 12.0, 13.0, 5.0, BodyType.SHORT, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.DELIBERATION, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeTwoCrows() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(7.0, 10.0, 16.0, 2.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(12.0, 11.0, 21, 5.0, BodyType.MARIBOZU, TrendDirection.UPPER),
                                         buildCandlestick(14.0, 9.0, 13.0, 5.2, BodyType.LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.TWO_CROWS, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeBearishUpsideGapTwoCrows() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(3.0, 7.0, 16.0, 2.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(10.0, 8.0, 21, 5.0, BodyType.LONG, TrendDirection.UPPER),
                                         buildCandlestick(10.5, 7.5, 13.0, 5.0, BodyType.LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.BEARISH_UPSIDE_GAP_TWO_CROWS, patterns.get(0).getPatternType());
    }

    @Test
    public void groupsOfThreeUniqueThreeRiverBottom() {
        final List<Pattern> patterns =
                PROCESSOR.scan(Stream.of(buildCandlestick(10.0, 2.0, 16.0, 2.0, BodyType.LONG, TrendDirection.DOWN),
                                         buildCandlestick(9.0, 8.0, 10, 1.0, BodyType.SHORT, TrendDirection.UPPER),
                                         buildCandlestick(5.0, 7.0, 13.0, 5.0, BodyType.LONG, TrendDirection.UPPER)))
                        .collect(toList());
        assertEquals(1, patterns.size());
        assertEquals(PatternType.UNIQUE_THREE_RIVER_BOTTOM, patterns.get(0).getPatternType());
    }
}
