package com.mumukiller.alert.repository;


import com.mumukiller.alert.transport.Candlestick;

import java.util.stream.Stream;

/**
 * Created by Mumukiller on 16.06.2017.
 */
public interface CustomCandlestickRepository {
    Stream<Candlestick> loadLastCandlesticksFromHistory(final String code, final int size);
}
