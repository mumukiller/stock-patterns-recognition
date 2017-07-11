package com.mumukiller.alert.transport;

import com.mumukiller.alert.dto.SimpleOhlcContainer;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static junit.framework.TestCase.assertEquals;


/**
 * Created by Mumukiller on 15.07.2017.
 */
public class OhlcContainerTest {

    @Test
    public void testBodySizeOpenMoreClose(){
        final OhlcContainer container = new SimpleOhlcContainer("AMD", "AMD", "AMD",10.0, 1.0, 1.0, 1.0, LocalDate.MIN, LocalTime.MIN);
        assertEquals(9.0, container.getBodysize());
    }

    @Test
    public void testBodySizeOpenLessClose(){
        final OhlcContainer container = new SimpleOhlcContainer("AMD", "AMD", "AMD", 1.0, 1.0, 1.0, 10.0, LocalDate.MIN, LocalTime.MIN);
        assertEquals(9.0, container.getBodysize());
    }
}
