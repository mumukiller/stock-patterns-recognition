package com.mumukiller.alert.service;

import com.mumukiller.alert.transport.Candlestick;
import com.mumukiller.alert.transport.OhlcContainer;
import com.mumukiller.alert.transport.Pattern;
import com.mumukiller.alert.transport.Stock;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Mumukiller on 04.07.2017.
 */
public interface CandlestickService {
    /**
     * Scan patterns for stocks which were saved in a table defined by code
     * @param code of Stock to scan
     * @return stream of recognized patterns
     */
    Stream<Candlestick> loadLastCandlesticksFromHistory(final String code, final int size);
}
