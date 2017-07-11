package com.mumukiller.alert.recognition.body;

import com.mumukiller.alert.BaseTest;
import com.mumukiller.alert.transport.OhlcContainer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by Mumukiller on 24.06.2017.
 */
public class BodyTypeDetectorTests extends BaseTest {

    private static final BodyTypeDetector PROCESSOR = new BodyTypeDetector(0.03, 0.5, 1.3);

    @Test
    public void unknown() {
        assertEquals(BodyType.UNKNOWN,
                     PROCESSOR.calculateBodyType(buildStock(1.0, 3.0, 0.5, 2.0), 1.0));
    }


    @Test(expected = IllegalArgumentException.class)
    public void highLessLowShouldFail() {
        buildStock(1.0, 0.0, 1.0, 1.0);
    }

    @Test
    public void fpdoji() {
        final OhlcContainer sample = buildStock(1.0, 1.0, 1.0, 1.0);

        assertEquals(BodyType.FOUR_PRICE_DOJI,
                     PROCESSOR.calculateBodyType(sample, Double.MAX_VALUE));

        assertEquals(BodyType.FOUR_PRICE_DOJI,
                     PROCESSOR.calculateBodyType(sample, 1.0));
    }

    @Test
    public void dragonflyDoji() {
        final OhlcContainer sample = buildStock(40.550, 40.550, 40.540, 40.550);

        assertEquals(BodyType.DRAGONFLY_DOJI,
                     PROCESSOR.calculateBodyType(sample, Double.MAX_VALUE));

        assertEquals(BodyType.DRAGONFLY_DOJI,
                     PROCESSOR.calculateBodyType(sample, 1.0));
    }

    @Test
    public void gravestoneDoji() {
        final OhlcContainer sample = buildStock(40.560, 40.570, 40.560, 40.560);

        assertEquals(BodyType.GRAVESTONE_DOJI,
                     PROCESSOR.calculateBodyType(sample, Double.MAX_VALUE));

        assertEquals(BodyType.GRAVESTONE_DOJI,
                     PROCESSOR.calculateBodyType(sample, 1.0));
    }

    @Test
    public void doji() {
        final OhlcContainer sample = buildStock(40.555, 41.0, 40.0, 40.556);

        assertEquals(BodyType.DOJI,
                     PROCESSOR.calculateBodyType(sample, Double.MAX_VALUE));

        assertEquals(BodyType.DOJI,
                     PROCESSOR.calculateBodyType(sample, 1.0));
    }

    @Test
    public void maribozu() {
        //bear
        assertEquals(BodyType.MARIBOZU,
                     PROCESSOR.calculateBodyType(buildStock(20.000, 20.050, 10.050, 10.000),
                                                 10.0));
        //bull
        assertEquals(BodyType.MARIBOZU,
                     PROCESSOR.calculateBodyType(buildStock(10.000, 20.050, 10.050, 20.000),
                                                 10.0));
    }

    @Test
    public void maribozuLong() {
        //bear
        assertEquals(BodyType.MARIBOZU_LONG,
                     PROCESSOR.calculateBodyType(buildStock(5.000, 5.005, 3.995, 4.000),
                                                 0.1));

        assertEquals(BodyType.MARIBOZU_LONG,
                     PROCESSOR.calculateBodyType(buildStock(500.000, 500.500, 399.500, 400.000),
                                                 10.0));

        //bull
        assertEquals(BodyType.MARIBOZU_LONG,
                     PROCESSOR.calculateBodyType(buildStock(4.000, 5.005, 3.995, 5.000),
                                                 0.1));

        assertEquals(BodyType.MARIBOZU_LONG,
                     PROCESSOR.calculateBodyType(buildStock(400.000, 500.500, 399.500, 500.000),
                                                 10.0));
    }

    @Test
    public void longCandlestick() {
        assertEquals(BodyType.LONG,
                     PROCESSOR.calculateBodyType(buildStock(2.0, 2.5, 0.5, 1.0),
                                                 0.1));

        assertEquals(BodyType.LONG,
                     PROCESSOR.calculateBodyType(buildStock(10000.0, 15000.0, 5000.0, 11000.0),
                                                 100.0));
    }

    @Test
    public void shortCandlestick() {
        assertEquals(BodyType.SHORT,
                     PROCESSOR.calculateBodyType(buildStock(2.0, 2.5, 0.5, 1.0),
                                                 3.0));

        assertEquals(BodyType.SHORT,
                     PROCESSOR.calculateBodyType(buildStock(1.0, 2.5, 0.5, 2.0),
                                                 3.0));
    }

    @Test
    public void spiningTop() {
        assertEquals(BodyType.SPINING_TOP,
                     PROCESSOR.calculateBodyType(buildStock(4.0, 6.0, 1.0, 3.0),
                                                 3.0));

        assertEquals(BodyType.SPINING_TOP,
                     PROCESSOR.calculateBodyType(buildStock(3.0, 6.0, 1.0, 4.0),
                                                 3.0));
    }

    @Test
    public void hammer() {
        assertEquals(BodyType.HAMMER,
                     PROCESSOR.calculateBodyType(buildStock(5.0, 5.05, 1.5, 4.0),
                                                 3.0));
    }

    @Test
    public void invertedHammer() {
        assertEquals(BodyType.INVERTED_HUMMER,
                     PROCESSOR.calculateBodyType(buildStock(5.0, 8.5, 3.95, 4.0),
                                                 3.0));
    }
}
