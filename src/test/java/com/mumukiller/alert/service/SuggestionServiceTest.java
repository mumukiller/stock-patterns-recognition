package com.mumukiller.alert.service;

import com.mumukiller.alert.BaseTest;
import com.mumukiller.alert.converter.CandlestickConverter;
import com.mumukiller.alert.recognition.OrderSuggestion;
import com.mumukiller.alert.recognition.body.BodyType;
import com.mumukiller.alert.recognition.body.BodyTypeDetector;
import com.mumukiller.alert.recognition.pattern.PatternsDetector;
import com.mumukiller.alert.recognition.trend.TrendDirection;
import com.mumukiller.alert.repository.LocalPriceRepository;
import org.junit.Test;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Mumukiller on 14.07.2017.
 */
public class SuggestionServiceTest extends BaseTest {
    private static final CandlestickService DEFAULT_CANDLESTICK_SERVICE = mock(CandlestickServiceImpl.class);
    private static final StockService DEFAULT_STOCK_SERVICE = mock(StockServiceImpl.class);
    private static final PatternsDetector DETECTOR = new PatternsDetector();
    private static final CandlestickConverter CONVERTER = new CandlestickConverter();
    private static final BodyTypeDetector BODY_TYPE_DETECTOR =
            new BodyTypeDetector(0.03, 0.5, 1.3);
    private static final SuggestionService SERVICE = new SuggestionServiceImpl(DEFAULT_CANDLESTICK_SERVICE,
                                                                               DEFAULT_STOCK_SERVICE,
                                                                               DETECTOR,
                                                                               BODY_TYPE_DETECTOR,
                                                                               CONVERTER,
                                                                               10);

    @Test(expected = StockIsNotFoundException.class)
    public void checkEmptyStock() {
        when(DEFAULT_STOCK_SERVICE.getRealtimeStockSample(anyString())).thenReturn(Optional.empty());
        SERVICE.suggestionFor("ANY");
    }

    @Test
    public void checkEmptyHistorySuggestion() {
        when(DEFAULT_STOCK_SERVICE.getRealtimeStockSample(anyString()))
                .thenReturn(Optional.of(buildStock(1.0, 1.0, 1.0, 1.0)));
        when(DEFAULT_CANDLESTICK_SERVICE.loadLastCandlesticksFromHistory(any(), anyInt()))
                .thenReturn(Stream.empty());

        assertEquals(OrderSuggestion.UNKNOWN, SERVICE.suggestionFor("ANY"));
    }

    @Test
    public void checkSellSuggestion() {
        when(DEFAULT_STOCK_SERVICE.getRealtimeStockSample(anyString()))
                .thenReturn(Optional.of(buildStock(1.0, 1.0, 1.0, 5.0)));
        when(DEFAULT_CANDLESTICK_SERVICE.loadLastCandlesticksFromHistory(any(), anyInt()))
                .thenReturn(Stream.of(buildCandlestick(6.0, 8.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                      buildCandlestick(6.5, 8.5, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                      buildCandlestick(7.0, 9.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                      buildCandlestick(15.0, 5.5, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN)));

        assertEquals(OrderSuggestion.SELL, SERVICE.suggestionFor("ANY"));
    }

    @Test
    public void checkBuySuggestion() {
        when(DEFAULT_STOCK_SERVICE.getRealtimeStockSample(anyString()))
                .thenReturn(Optional.of(buildStock(10.0, 1.0, 1.0, 5.0)));
        when(DEFAULT_CANDLESTICK_SERVICE.loadLastCandlesticksFromHistory(any(), anyInt()))
                .thenReturn(Stream.of(buildCandlestick(3.0, 2.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                      buildCandlestick(10.0, 5.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                      buildCandlestick(10.0, 5.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN),
                                      buildCandlestick(3.0, 4.0, 1.0, 1.0, BodyType.LONG, TrendDirection.DOWN)));

        assertEquals(OrderSuggestion.BUY, SERVICE.suggestionFor("ANY"));
    }

    @Test
    public void checkNeutralSuggestion() {
        when(DEFAULT_STOCK_SERVICE.getRealtimeStockSample(anyString()))
                .thenReturn(Optional.of(buildStock(1.0, 1.0, 1.0, 1.0)));
        when(DEFAULT_CANDLESTICK_SERVICE.loadLastCandlesticksFromHistory(any(), anyInt()))
                .thenReturn(Stream.of(buildCandlestick(1.0, 1.0, 1.0, 1.0, BodyType.DOJI, TrendDirection.DOWN)));

        assertEquals(OrderSuggestion.NEUTRAL, SERVICE.suggestionFor("ANY"));
    }
}
