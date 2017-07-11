package com.mumukiller.alert.service;

import com.mumukiller.alert.converter.CandlestickConverter;
import com.mumukiller.alert.recognition.body.BodyType;
import com.mumukiller.alert.recognition.trend.TrendDirection;
import com.mumukiller.alert.transport.OhlcContainer;
import com.mumukiller.alert.transport.Candlestick;
import com.mumukiller.alert.recognition.body.BodyTypeDetector;
import com.mumukiller.alert.recognition.trend.TrendDetector;
import com.mumukiller.alert.repository.CandlestickRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Mumukiller on 04.07.2017.
 */
@Slf4j
@Service
public class CandlestickServiceImpl implements CandlestickService {

    @Getter private final CandlestickRepository candlestickRepository;
    @Getter private final CandlestickConverter candlestickConverter;
    @Getter private final BodyTypeDetector bodyTypeDetector;
    @Getter private final TrendDetector trendDetector;

    @Autowired
    public CandlestickServiceImpl(final CandlestickRepository candlestickRepository,
                                  final CandlestickConverter candlestickConverter,
                                  final BodyTypeDetector bodyTypeDetector,
                                  @Value("${trend.short-term.period}") final int shortTermTrendPeriod) {
        this.candlestickRepository = candlestickRepository;
        this.candlestickConverter = candlestickConverter;
        this.bodyTypeDetector = bodyTypeDetector;
        this.trendDetector = new TrendDetector(shortTermTrendPeriod);
    }


    @Override
    public Stream<Candlestick> loadLastCandlesticksFromHistory(final String code, final int size) {
        return getCandlestickRepository().loadLastCandlesticksFromHistory(code, size);
    }
}
