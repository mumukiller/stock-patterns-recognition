package com.mumukiller.alert.service;

import com.mumukiller.alert.transport.Stock;
import rx.Observable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Mumukiller on 16.06.2017.
 */
public interface StockService {

    Stream<Stock> getRealtimeStocksSamples(final List<String> codes);

    Optional<Stock> getRealtimeStockSample(final String code);

    List<String> getMonitoredStockCodes();

    Observable<Map<String, Stock>> getStockSamplesObservable();
}
