package com.mumukiller.alert.domain;

import com.mumukiller.alert.transport.OhlcContainer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Mumukiller on 16.06.2017.
 */
@Entity(name = "candlesticks")
public class CandlestickModel implements OhlcContainer {
    @Id
    @Column(name = "code")
    @Getter @Setter private String code;
    @Column(name = "name")
    @Getter @Setter private String name;
    @Column(name = "tool")
    @Getter @Setter private String tool;

    @Column(name = "date")
    @Getter @Setter private LocalDate date;
    @Column(name = "time")
    @Getter @Setter private LocalTime time;

    @Column(name = "open_price")
    @Getter @Setter private double open;
    @Column(name = "high_price")
    @Getter @Setter private double high;
    @Column(name = "low_price")
    @Getter @Setter private double low;
    @Column(name = "close_price")
    @Getter @Setter private double close;

    @Override
    public double getBodysize() {
        return Math.abs(open - close);
    }

    @Override
    public boolean isBullish() {
        return open < close;
    }
}
