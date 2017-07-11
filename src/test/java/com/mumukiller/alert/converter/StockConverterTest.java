package com.mumukiller.alert.converter;

import com.mumukiller.alert.converter.StockConverter;
import com.mumukiller.alert.domain.StockModel;
import com.mumukiller.alert.dto.StockDto;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mumukiller on 13.07.2017.
 */
public class StockConverterTest {

    private static final StockConverter DEFAULT_CONVERTER = new StockConverter();

    @Test
    public void checkConversion() {
        final StockModel model = new StockModel();
        model.setCode(UUID.randomUUID().toString());
        model.setName(UUID.randomUUID().toString());
        model.setOpen(33.840000);
        model.setHigh(33.960000);
        model.setLow(33.816000);
        model.setClose(33.927500);
        model.setTool(UUID.randomUUID().toString());
        model.setBuyPrice(Double.MIN_VALUE);
        model.setSellPrice(Double.MIN_VALUE);
        model.setDate(LocalDate.MIN);
        model.setTime(LocalTime.MIDNIGHT);

        final StockDto dto = DEFAULT_CONVERTER.convert(model);

        assertEquals(dto.getName(), model.getName());
        assertEquals(dto.getCode(), model.getCode());
        assertEquals(dto.getOpen(), model.getOpen(), 0.0);
        assertEquals(dto.getHigh(), model.getHigh(), 0.0);
        assertEquals(dto.getLow(), model.getLow(), 0.0);
        assertEquals(dto.getClose(), model.getClose(), 0.0);
        assertEquals(dto.getTool(), model.getTool());
        assertEquals(dto.getBuyPrice(), model.getBuyPrice());
        assertEquals(dto.getSellPrice(), model.getSellPrice());
        assertEquals(dto.getDate(), model.getDate());
        assertEquals(dto.getTime(), model.getTime());
    }

}
