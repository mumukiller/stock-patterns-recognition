package com.mumukiller.alert.recognition.trend;

import com.mumukiller.alert.recognition.body.BodyType;
import com.mumukiller.alert.recognition.body.BodyTypeDetector;
import com.mumukiller.alert.transport.OhlcContainer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AverageBodysizeDirector extends AverageDirector {

    @Getter
    private final BodyTypeDetector bodyTypeDetector;

    @Autowired
    public AverageBodysizeDirector(final BodyTypeDetector bodyTypeDetector,
                                   @Value("#{'${app.stocks-names-to-process}'.split(',')}")
                                   final List<String> monitoredStockCodes) {
        super(monitoredStockCodes);
        this.bodyTypeDetector = bodyTypeDetector;
    }


    public BodyType get(final OhlcContainer container){
        return getBodyTypeDetector().calculateBodyType(container, get(container.getCode()));
    }

    @Override
    public Logger getLogger() {
        return log;
    }
}
