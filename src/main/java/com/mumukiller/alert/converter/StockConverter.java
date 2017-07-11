package com.mumukiller.alert.converter;

import com.mumukiller.alert.domain.StockModel;
import com.mumukiller.alert.dto.StockDto;
import org.springframework.stereotype.Service;

/**
 * Created by Mumukiller on 11.07.2017.
 */
@Service
public class StockConverter {

    public StockDto convert(final StockModel model) {
        return new StockDto(model.getTool(),
                            model.getCode(),
                            model.getName(),
                            model.getOpen(),
                            model.getHigh(),
                            model.getLow(),
                            model.getClose(),
                            model.getSellPrice(),
                            model.getBuyPrice(),
                            model.getDate(),
                            model.getTime());
    }
}
