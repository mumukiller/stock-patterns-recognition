package com.mumukiller.alert.recognition.trend;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AveragePriceDirector extends AverageDirector {

    public AveragePriceDirector(@Value("#{'${app.stocks-names-to-process}'.split(',')}")
                                final List<String> monitoredStockCodes) {
        super(monitoredStockCodes);
    }

    @Override
    public Logger getLogger() {
        return log;
    }
}
