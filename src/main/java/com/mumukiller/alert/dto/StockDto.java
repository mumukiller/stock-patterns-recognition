package com.mumukiller.alert.dto;


import com.mumukiller.alert.transport.Stock;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Mumukiller on 15.06.2017.
 */
public class StockDto extends SimpleOhlcContainer implements Stock {

    private static final long serialVersionUID = 5857634629687418563L;

    @Getter private final Double sellPrice;
    @Getter private final Double buyPrice;

    public StockDto(final String tool,
                    final String code,
                    final String name,
                    final Double open,
                    final Double high,
                    final Double low,
                    final Double close,
                    final Double sellPrice,
                    final Double buyPrice,
                    final LocalDate date,
                    final LocalTime time) {
        super(tool, code, name, open, high, low, close, date, time);
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
    }
}
