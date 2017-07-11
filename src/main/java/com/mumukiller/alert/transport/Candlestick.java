package com.mumukiller.alert.transport;

import com.mumukiller.alert.recognition.body.BodyType;
import com.mumukiller.alert.recognition.trend.TrendDirection;

/**
 * Created by Mumukiller on 24.06.2017.
 */
public interface Candlestick extends OhlcContainer {
    BodyType getBodyType();
    TrendDirection getTrendDirection();
}
