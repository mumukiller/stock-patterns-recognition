package com.mumukiller.alert.repository;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


/**
 * Created by Mumukiller on 15.07.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockRepositoryIT {

    @Autowired
    private StockRepository stockRepository;

    @Test
    public void checkEmptyList() throws Exception {
        assertEquals(0, stockRepository.findByCodeIn(ImmutableList.of("AMD")).size());
    }

    @Test
    public void checkEmpty() throws Exception {
        assertFalse(stockRepository.findByCode("AMD").isPresent());
    }

}
