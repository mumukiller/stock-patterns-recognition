package com.mumukiller.alert.service;

import com.google.common.collect.ImmutableList;
import com.mumukiller.alert.repository.StockRepository;
import com.mumukiller.alert.transport.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rx.observers.TestSubscriber;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


/**
 * Created by Mumukiller on 15.07.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockServiceIT {

    @Autowired
    private StockService stockService;

    @Test
    public void checkList() throws Exception {
        assertEquals(4, stockService.getMonitoredStockCodes().size());
    }

    @Test
    public void checkEmpty() throws Exception {
        assertFalse(stockService.getRealtimeStockSample("ANY").isPresent());
    }

    @Test
    public void checkEmptyList() throws Exception {
        assertEquals(0, stockService.getRealtimeStocksSamples(ImmutableList.of("ANY", "ANY")).count());
    }

    @Test
    public void checkEmptyStream() throws Exception {
        TestSubscriber<Map<String, Stock>> testSubscriber = new TestSubscriber<>();
        stockService.getStockSamplesObservable()
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        final List<Map<String, Stock>> result = testSubscriber.getOnNextEvents();
        assertEquals(0, result.size());
    }

}
