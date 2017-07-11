package com.mumukiller.alert.recognition.trend;

import com.google.common.collect.ImmutableList;
import com.mumukiller.alert.BaseTest;
import com.mumukiller.alert.transport.OhlcContainer;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

/**
 * Created by Mumukiller on 14.07.2017.
 */
public class TrendDetectorTest extends BaseTest {

    private static final int DEFAULT_TREND_PERIOD = 5;
    private static final TrendDetector DEFAULT_DETECTOR = new TrendDetector(DEFAULT_TREND_PERIOD);

    @Test
    public void sliceAtZeroTrendPeriod() {
        assertEquals(emptyList(),
                     DEFAULT_DETECTOR.slice(emptyList(), 0));
    }

    @Test
    public void sliceAtIndexMoreThanSizeTrendPeriod() {
        final List<OhlcContainer> samples = ImmutableList.of(buildStock(20.0, 100.0, 0.0, 10.0),
                                                             buildStock(30.0, 100.0, 0.0, 10.0),
                                                             buildStock(40.0, 100.0, 0.0, 10.0),
                                                             buildStock(50.0, 100.0, 0.0, 10.0),
                                                             buildStock(60.0, 100.0, 0.0, 10.0),
                                                             buildStock(70.0, 100.0, 0.0, 10.0),
                                                             buildStock(80.0, 100.0, 0.0, 10.0),
                                                             buildStock(90.0, 100.0, 0.0, 10.0),
                                                             buildStock(100.0, 100.0, 0.0, 10.0));

        assertEquals(emptyList(),
                     DEFAULT_DETECTOR.slice(samples, samples.size() + 1));
    }

    @Test
    public void sliceLessTrendPeriod() {
        final List<OhlcContainer> samples = ImmutableList.of(buildStock(20.0, 100.0, 0.0, 10.0),
                                                             buildStock(30.0, 100.0, 0.0, 10.0),
                                                             buildStock(40.0, 100.0, 0.0, 10.0),
                                                             buildStock(50.0, 100.0, 0.0, 10.0),
                                                             buildStock(60.0, 100.0, 0.0, 10.0),
                                                             buildStock(70.0, 100.0, 0.0, 10.0),
                                                             buildStock(80.0, 100.0, 0.0, 10.0),
                                                             buildStock(90.0, 100.0, 0.0, 10.0),
                                                             buildStock(100.0, 100.0, 0.0, 10.0));
        assertEquals(samples.subList(0, DEFAULT_TREND_PERIOD),
                     DEFAULT_DETECTOR.slice(samples, DEFAULT_TREND_PERIOD));
    }

    @Test
    public void sliceMoreTrendPeriod() {
        final List<OhlcContainer> samples = ImmutableList.of(buildStock(20.0, 100.0, 0.0, 10.0),
                                                             buildStock(30.0, 100.0, 0.0, 10.0),
                                                             buildStock(40.0, 100.0, 0.0, 10.0),
                                                             buildStock(50.0, 100.0, 0.0, 10.0),
                                                             buildStock(60.0, 100.0, 0.0, 10.0),
                                                             buildStock(70.0, 100.0, 0.0, 10.0),
                                                             buildStock(80.0, 100.0, 0.0, 10.0),
                                                             buildStock(90.0, 100.0, 0.0, 10.0),
                                                             buildStock(100.0, 100.0, 0.0, 10.0));
        assertEquals(samples.subList(2, DEFAULT_TREND_PERIOD + 2),
                     DEFAULT_DETECTOR.slice(samples, DEFAULT_TREND_PERIOD + 3));
    }

    @Test
    public void checkTrendPeriod() {
        assertEquals(Integer.MIN_VALUE,
                     new TrendDetector(Integer.MIN_VALUE).getTrendPeriod());
    }

    @Test
    public void checkAverageBodysizeForEmptyList() {
        assertEquals(0.0,
                     DEFAULT_DETECTOR.getAverageBodySize(emptyList()),
                     0.0);
    }

    @Test
    public void checkAverageBodysizeForSingleSample() {
        assertEquals(1.0,
                     DEFAULT_DETECTOR.getAverageBodySize(ImmutableList.of(buildStock(1.0, 3.0, 1.0, 2.0))),
                     0.0);
    }

    @Test
    public void checkAverageBodysize() {
        final List<OhlcContainer> samples = ImmutableList.of(buildStock(20.0, 100.0, 0.0, 10.0),
                                                             buildStock(30.0, 100.0, 0.0, 10.0),
                                                             buildStock(40.0, 100.0, 0.0, 10.0),
                                                             buildStock(50.0, 100.0, 0.0, 10.0),
                                                             buildStock(60.0, 100.0, 0.0, 10.0),
                                                             buildStock(70.0, 100.0, 0.0, 10.0));
        assertEquals((10.0 + 20.0 + 30.0 + 40.0 + 50.0 + 60.0) / 6,
                     DEFAULT_DETECTOR.getAverageBodySize(samples),
                     0.0);
    }

    @Test
    public void checkTrendDirectionWithEmptySamples() {
        assertEquals(TrendDirection.LATERAL,
                     DEFAULT_DETECTOR.getTrendDirection(buildStock(1.0, 3.0, 1.0, 2.0), emptyList()));
    }

    @Test
    public void checkTrendDirectionWithSingleSample() {
        assertEquals(TrendDirection.LATERAL,
                     DEFAULT_DETECTOR.getTrendDirection(buildStock(1.0, 3.0, 1.0, 2.0), emptyList()));
    }

    @Test
    public void checkTrendDirectionWithSamples() {
        assertEquals(TrendDirection.UPPER,
                     DEFAULT_DETECTOR.getTrendDirection(buildStock(20.0, 100.0, 0.0, 10.0),
                                                        ImmutableList.of(buildStock(20.0, 100.0, 0.0, 10.0),
                                                                         buildStock(30.0, 100.0, 0.0, 10.0),
                                                                         buildStock(40.0, 100.0, 0.0, 10.0),
                                                                         buildStock(50.0, 100.0, 0.0, 10.0),
                                                                         buildStock(60.0, 100.0, 0.0, 10.0),
                                                                         buildStock(70.0, 100.0, 0.0, 10.0),
                                                                         buildStock(80.0, 100.0, 0.0, 10.0),
                                                                         buildStock(90.0, 100.0, 0.0, 10.0),
                                                                         buildStock(100.0, 100.0, 0.0, 10.0),
                                                                         buildStock(110.0, 100.0, 0.0, 0.0))));

        assertEquals(TrendDirection.DOWN,
                     DEFAULT_DETECTOR.getTrendDirection(buildStock(20.0, 100.0, 0.0, 10.0),
                                                        ImmutableList.of(buildStock(20.0, 100.0, 0.0, 10.0),
                                                                         buildStock(30.0, 100.0, 0.0, 10.0),
                                                                         buildStock(40.0, 100.0, 0.0, 10.0),
                                                                         buildStock(50.0, 100.0, 0.0, 10.0),
                                                                         buildStock(60.0, 100.0, 0.0, 10.0),
                                                                         buildStock(70.0, 100.0, 0.0, 10.0),
                                                                         buildStock(80.0, 100.0, 0.0, 10.0),
                                                                         buildStock(90.0, 100.0, 0.0, 10.0),
                                                                         buildStock(100.0, 100.0, 0.0, 10.0),
                                                                         buildStock(110.0, 100.0, 0.0, 20.0))));

        assertEquals(TrendDirection.LATERAL,
                     DEFAULT_DETECTOR.getTrendDirection(buildStock(20.0, 100.0, 0.0, 10.0),
                                                        ImmutableList.of(buildStock(20.0, 100.0, 0.0, 10.0),
                                                                         buildStock(30.0, 100.0, 0.0, 10.0),
                                                                         buildStock(40.0, 100.0, 0.0, 10.0),
                                                                         buildStock(50.0, 100.0, 0.0, 10.0),
                                                                         buildStock(60.0, 100.0, 0.0, 10.0),
                                                                         buildStock(70.0, 100.0, 0.0, 10.0),
                                                                         buildStock(80.0, 100.0, 0.0, 10.0),
                                                                         buildStock(90.0, 100.0, 0.0, 10.0),
                                                                         buildStock(100.0, 100.0, 0.0, 10.0),
                                                                         buildStock(110.0, 100.0, 0.0, 10.0))));
    }
}
