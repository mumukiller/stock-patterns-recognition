package com.mumukiller.alert.service;

import com.google.common.collect.ImmutableList;
import com.mumukiller.alert.converter.CandlestickConverter;
import com.mumukiller.alert.recognition.OrderSuggestion;
import com.mumukiller.alert.recognition.body.BodyType;
import com.mumukiller.alert.recognition.body.BodyTypeDetector;
import com.mumukiller.alert.recognition.pattern.PatternsDetector;
import com.mumukiller.alert.recognition.trend.TrendDetector;
import com.mumukiller.alert.recognition.trend.TrendDirection;
import com.mumukiller.alert.transport.Candlestick;
import com.mumukiller.alert.transport.Stock;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by Mumukiller on 16.07.2017.
 */
@Slf4j
@Service
public class SuggestionServiceImpl implements SuggestionService {

    @Getter private final CandlestickService candlestickService;
    @Getter private final TrendDetector trendDetector;
    @Getter private final StockService stockService;
    @Getter private final PatternsDetector patternsDetector;
    @Getter private final BodyTypeDetector bodyTypeDetector;
    @Getter private final CandlestickConverter candlestickConverter;

    public SuggestionServiceImpl(final CandlestickService candlestickService,
                                 final StockService stockService,
                                 final PatternsDetector patternsDetector,
                                 final BodyTypeDetector bodyTypeDetector,
                                 final CandlestickConverter candlestickConverter,
                                 @Value("${trend.short-term.period}") final int shortTermTrendPeriod) {
        this.candlestickService = candlestickService;
        this.trendDetector = new TrendDetector(shortTermTrendPeriod);
        this.stockService = stockService;
        this.patternsDetector = patternsDetector;
        this.bodyTypeDetector = bodyTypeDetector;
        this.candlestickConverter = candlestickConverter;
    }

    @Override
    public OrderSuggestion suggestionFor(final String code) {
        final Stock stock = getStockService().getRealtimeStockSample(code)
                .orElseThrow(() -> new StockIsNotFoundException(code));

        final List<Candlestick> history = getCandlestickService().loadLastCandlesticksFromHistory(code, 4)
                .collect(toList());

        if (history.isEmpty())
            return OrderSuggestion.UNKNOWN;


        final double averageBodySize = getTrendDetector().getAverageBodySize(history);
        final BodyType bodyType = getBodyTypeDetector().calculateBodyType(stock, averageBodySize);
        final TrendDirection trendDirection = getTrendDetector().getTrendDirection(stock, history);
        final Candlestick candidate = getCandlestickConverter().convert(stock, bodyType, trendDirection);

        log.debug("Processing suggestion for {} and {}", candidate, history);
        return getPatternsDetector().findDescendingFirst(ImmutableList.<Candlestick>builder()
                                                             .add(candidate)
                                                             .addAll(history)
                                                             .build().stream())
                .map(p -> OrderSuggestion.suggestionBy(p.getPatternType()))
                .orElse(OrderSuggestion.UNKNOWN);
    }
}
