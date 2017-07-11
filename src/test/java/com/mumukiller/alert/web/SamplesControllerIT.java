package com.mumukiller.alert.web;

import com.google.common.collect.ImmutableList;
import com.mumukiller.alert.BaseTest;
import com.mumukiller.alert.dto.PatternDto;
import com.mumukiller.alert.dto.StockDto;
import com.mumukiller.alert.recognition.body.BodyType;
import com.mumukiller.alert.recognition.pattern.PatternType;
import com.mumukiller.alert.recognition.trend.TrendDirection;
import com.mumukiller.alert.service.CandlestickService;
import com.mumukiller.alert.service.StockService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Mumukiller on 15.06.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SamplesControllerIT extends BaseTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StockService stockService;

    @MockBean
    private CandlestickService candlestickService;

    @Test
    public void testStockCodesEndpoint() throws Exception {
        final List<String> expectedCodes = ImmutableList.of("A", "B");
        given(this.stockService.getMonitoredStockCodes())
                .willReturn(expectedCodes);

        mvc.perform(get("/stocks/codes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", Matchers.containsInAnyOrder("A", "B")));
    }

    @Test
    public void testStockByCodeEndpoint() throws Exception {
        final StockDto expectedStock =
                new StockDto("AMD", "AMD", "AMD", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, LocalDate.MIN, LocalTime.MIN);
        given(this.stockService.getRealtimeStockSample(any()))
                .willReturn(Optional.of(expectedStock));

        mvc.perform(asyncDispatch(mvc.perform(get("/stocks/AMD").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                                          .andReturn()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.tool").value("AMD"))
                .andExpect(jsonPath("$.code").value("AMD"))
                .andExpect(jsonPath("$.name").value("AMD"))
                .andExpect(jsonPath("$.open").value(1.0))
                .andExpect(jsonPath("$.high").value(1.0))
                .andExpect(jsonPath("$.close").value(1.0))
                .andExpect(jsonPath("$.low").value(1.0))
                .andExpect(jsonPath("$.sellPrice").value(1.0))
                .andExpect(jsonPath("$.buyPrice").value(1.0));
    }

    @Test
    public void testStocksByCodeEndpoint() throws Exception {
        final StockDto expectedStock =
                new StockDto("AMD", "AMD", "AMD", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, LocalDate.MIN, LocalTime.MIN);
        given(this.stockService.getRealtimeStocksSamples(any()))
                .willReturn(Stream.of(expectedStock));

        mvc.perform(asyncDispatch(mvc.perform(get("/stocks").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                                          .andReturn()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tool").value("AMD"))
                .andExpect(jsonPath("$[0].code").value("AMD"))
                .andExpect(jsonPath("$[0].name").value("AMD"))
                .andExpect(jsonPath("$[0].open").value(1.0))
                .andExpect(jsonPath("$[0].high").value(1.0))
                .andExpect(jsonPath("$[0].close").value(1.0))
                .andExpect(jsonPath("$[0].low").value(1.0))
                .andExpect(jsonPath("$[0].sellPrice").value(1.0))
                .andExpect(jsonPath("$[0].buyPrice").value(1.0));
    }

    @Test
    public void testRuntimeException() throws Exception {
        given(this.stockService.getMonitoredStockCodes())
                .willThrow(RuntimeException.class);

        mvc.perform(get("/stocks/codes"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.message").value("Something goes wrong"));
    }

    @Test
    public void testEmptyResultDataAccessException() throws Exception {
        given(this.stockService.getMonitoredStockCodes())
                .willThrow(EmptyResultDataAccessException.class);

        mvc.perform(get("/stocks/codes"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.message").value("Entity not found or incorrect result size"));
    }

    @Test
    public void testSuggestionsNotFound() throws Exception {
        mvc.perform(get("/stocks/ANY/suggestion"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.message").value("Entity not found"))
                .andExpect(jsonPath("$.code").value("ANY"));
    }

    @Test
    public void testSuggestions() throws Exception {
        given(this.stockService.getRealtimeStockSample(anyString()))
                .willReturn(Optional.of(buildStock(10.0, 5.0, 1.0, 5.0)));

        given(this.candlestickService.loadLastCandlesticksFromHistory(anyString(), anyInt()))
                .willReturn(Stream.of(buildCandlestick(3.0, 2.0, 1.0, 1.0, BodyType.SHORT, TrendDirection.DOWN)));

        mvc.perform(asyncDispatch(mvc.perform(get("/stocks/AMD/suggestion").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                                          .andReturn()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").value("NEUTRAL"));
    }
}
