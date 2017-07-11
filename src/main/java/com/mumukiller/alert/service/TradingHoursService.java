package com.mumukiller.alert.service;

import com.mumukiller.alert.recognition.Exchange;
import com.mumukiller.alert.recognition.TradingStatus;
import org.springframework.stereotype.Service;

/**
 * Created by Mumukiller on 20.06.2017.
 */
@Service
public interface TradingHoursService {
    TradingStatus statusOf(final Exchange stockExchange);
}
