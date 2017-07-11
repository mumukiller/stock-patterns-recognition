package com.mumukiller.alert.recognition.trend;

import com.mumukiller.alert.transport.OhlcContainer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Created by Mumukiller on 17.06.2017.
 */
@Slf4j
public class TrendDetector {

    @Getter private final int trendPeriod;

    public TrendDetector(final int trendPeriod) {
        this.trendPeriod = trendPeriod;
    }

    public TrendDirection getTrendDirection(final OhlcContainer current, final List<? extends OhlcContainer> previous) {
        if (previous.isEmpty())
            return TrendDirection.LATERAL;

        final Double average = previous.stream()
                .mapToDouble(OhlcContainer::getClose)
                .sum() / previous.size();

        if (average < current.getClose())
            return TrendDirection.UPPER;

        if (average > current.getClose())
            return TrendDirection.DOWN;

        return TrendDirection.LATERAL;
    }

    public double getAverageBodySize(final List<? extends OhlcContainer> samples){
        if (samples.isEmpty())
            return 0.0;

        log.debug("Samples count provided for counting AvgBS is {}", samples.size());
        return samples.stream()
                .mapToDouble(OhlcContainer::getBodysize)
                .sum() / samples.size();
    }

    public List<? extends OhlcContainer> slice(final List<? extends OhlcContainer> samples,
                                               final int index){
        if (index <= 0 || samples.size() <= 1 || index > samples.size())
            return emptyList();

        log.debug("Slicing samples with size {} using trend period {} and index {}", samples.size(), getTrendPeriod(), index);
        if (index > getTrendPeriod())
            return samples.subList(index - getTrendPeriod() - 1, index - 1);

        return samples.subList(0, index);
    }

}
