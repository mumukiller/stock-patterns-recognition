package com.mumukiller.alert.repository;

import com.mumukiller.alert.converter.CandlestickConverter;
import com.mumukiller.alert.transport.Candlestick;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;


/**
 * Created by Mumukiller on 16.06.2017.
 */
@Slf4j
@Repository
public class CandlestickRepositoryImpl implements CustomCandlestickRepository {

    @Getter private final CandlestickConverter candlestickConverter;

    @Autowired
    public CandlestickRepositoryImpl(final CandlestickConverter candlestickConverter) {
        this.candlestickConverter = candlestickConverter;
    }

    @Override
    public Stream<Candlestick> loadLastCandlesticksFromHistory(final String code, final int size) {
        return Stream.empty();
    }

    public Logger getLogger(){
        return log;
    }
}
