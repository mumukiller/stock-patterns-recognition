package com.mumukiller.alert.recognition;

import com.mumukiller.alert.recognition.pattern.PatternType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mumukiller on 15.07.2017.
 */
public class OrderSuggestionTest {
    @Test
    public void checkOrderSuggestion() {
        assertEquals(OrderSuggestion.SELL, OrderSuggestion.suggestionBy(PatternType.ABANDON_BABY_BEARISH));
        assertEquals(OrderSuggestion.BUY, OrderSuggestion.suggestionBy(PatternType.ABANDON_BABY_BULLISH));
        assertEquals(OrderSuggestion.NEUTRAL, OrderSuggestion.suggestionBy(PatternType.UNKNOWN));
    }
}
