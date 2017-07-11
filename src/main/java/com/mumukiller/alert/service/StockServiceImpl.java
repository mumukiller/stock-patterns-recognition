package com.mumukiller.alert.service;

import com.mumukiller.alert.converter.StockConverter;
import com.mumukiller.alert.repository.StockRepository;
import com.mumukiller.alert.transport.Stock;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * Created by Mumukiller on 16.06.2017.
 */
@Service
public class StockServiceImpl implements StockService {

    @Getter private final StockRepository stockRepository;
    @Getter private final StockConverter stockConverter;
    @Getter private final List<String> monitoredStockCodes;
    @Getter private final Observable<Map<String, Stock>> stockSamplesObservable;

    @Autowired
    public StockServiceImpl(final StockRepository stockRepository,
                            final StockConverter stockConverter,
                            @Value("#{'${app.stocks-names-to-process}'.split(',')}") final List<String> monitoredStockCodes) {
        this.stockRepository = stockRepository;
        this.stockConverter = stockConverter;
        this.monitoredStockCodes = monitoredStockCodes;
        this.stockSamplesObservable = Observable.interval(5, TimeUnit.SECONDS)
                .map(n -> this.getRealtimeStocksSamples(monitoredStockCodes)
                        .collect(toMap(Stock::getCode, identity())));
    }

    @Override
    public Stream<Stock> getRealtimeStocksSamples(final List<String> codes) {
        return getStockRepository().findByCodeIn(codes)
                .stream()
                .map(getStockConverter()::convert);
    }

    @Override
    public Optional<Stock> getRealtimeStockSample(final String code) {
        return getStockRepository().findByCode(code).map(getStockConverter()::convert);
    }
}
