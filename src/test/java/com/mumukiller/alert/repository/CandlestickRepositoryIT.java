package com.mumukiller.alert.repository;

import com.mumukiller.alert.converter.CandlestickConverter;
import com.mumukiller.alert.service.StockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Mumukiller on 14.07.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CandlestickRepositoryIT {

    @Autowired
    private CandlestickRepository candlestickRepository;

    @MockBean
    private CandlestickConverter candlestickConverter;

    @Test(expected = StockPricesFileIsNotFound.class)
    public void checkAverageBodysizeForEmptyList() {
        final Resource resource = new ClassPathResource("/prices/ANY.txt");
        assertEquals(0, candlestickRepository.loadFrom(resource).size());
    }

    @Test
    public void checkFileWithFiveRecords() {
        final Resource resource = new ClassPathResource("/prices/US33734X1928.txt");
        assertEquals(5, candlestickRepository.loadFrom(resource).size());
    }

    @Test
    public void checkFileWithSingleRecords() {
        final Resource resource = new ClassPathResource("/prices/US001.txt");
        assertEquals(1, candlestickRepository.loadFrom(resource).size());
    }

    //@Test(expected = UnableToLoadFileException.class)
    public void checkIOHandling() {
        when(candlestickConverter.convert(anyString())).thenThrow(new RuntimeException());
        final Resource resource = new ClassPathResource("/prices/US001.txt");
        candlestickRepository.loadFrom(resource);
    }
}
