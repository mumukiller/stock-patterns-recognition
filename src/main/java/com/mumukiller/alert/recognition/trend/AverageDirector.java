package com.mumukiller.alert.recognition.trend;

import com.mumukiller.alert.transport.OhlcContainer;
import com.mumukiller.alert.transport.Stock;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AverageDirector {

    private final Map<String, Double> values;
    private final Map<String, Queue<OhlcContainer>> sequences;

    public AverageDirector(final List<String> monitoredStockCodes) {
        final Map<String, Double> values = new ConcurrentHashMap<>();
        final Map<String, Queue<OhlcContainer>> sequences = new ConcurrentHashMap<>();

        monitoredStockCodes.forEach(code -> {
            values.put(code, 0.0);
            sequences.put(code, new ArrayBlockingQueue<>(10));
        });

        this.values = values;
        this.sequences = sequences;
    }

    public Double get(final String code){
        return this.values.get(code);
    }


    public void add(final Stock stock){
        final Queue<OhlcContainer> sequence = this.sequences.get(stock.getCode());
        final OhlcContainer latest = sequence.peek();

        if ((latest == null) || (latest.getTime().isBefore(stock.getTime()) && latest.getClose() != stock.getClose())) {
            getLogger().debug("New {} stock {} passed for average processing", stock.getCode(), stock);
            if (sequence.size() == 10)
                sequence.remove();

            sequence.add(stock);

            this.values.replace(stock.getCode(), sequence.stream()
                    .mapToDouble(OhlcContainer::getClose)
                    .sum() / 10);
        }
    }

    abstract Logger getLogger();
}
