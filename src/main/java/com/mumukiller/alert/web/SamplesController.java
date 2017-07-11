package com.mumukiller.alert.web;

import com.mumukiller.alert.recognition.OrderSuggestion;
import com.mumukiller.alert.service.SuggestionService;
import com.mumukiller.alert.transport.Stock;
import com.mumukiller.alert.service.CandlestickService;
import com.mumukiller.alert.service.StockService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Mumukiller on 15.06.2017.
 */
@Slf4j
@RestController
public class SamplesController {

    @Getter private final CandlestickService candlestickService;
    @Getter private final StockService stockService;
    @Getter private final SuggestionService suggestionService;

    @Autowired
    public SamplesController(final CandlestickService candlestickService,
                             final StockService stockService,
                             final SuggestionService suggestionService) {
        this.candlestickService = candlestickService;
        this.stockService = stockService;
        this.suggestionService = suggestionService;
    }

    @GetMapping("/stocks")
    public Flux<Stock> getStocksByMonitoredStockCodes() {
        return Flux.fromStream(getStockService().getRealtimeStocksSamples(getStockService().getMonitoredStockCodes()));
    }

    @GetMapping("/stocks/{code}")
    public Mono<Stock> getStock(@PathVariable final String code) {
        return Mono.justOrEmpty(getStockService().getRealtimeStockSample(code));
    }

    @GetMapping("/stocks/{code}/suggestion")
    public Mono<OrderSuggestion> getSuggestionByStockCode(@PathVariable final String code) {
        return Mono.just(getSuggestionService().suggestionFor(code));
    }

    @GetMapping("/stocks/codes")
    public List<String> getMonitoredStockCodes() {
        return getStockService().getMonitoredStockCodes();
    }
}
