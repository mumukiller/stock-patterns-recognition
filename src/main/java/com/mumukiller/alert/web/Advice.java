package com.mumukiller.alert.web;

import com.google.common.collect.ImmutableMap;
import com.mumukiller.alert.service.StockIsNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Created by Mumukiller on 15.06.2017.
 */
@Slf4j
@RestControllerAdvice
public class Advice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map defaultHandler(final Exception e) {
        log.error(e.getMessage(), e);
        return ImmutableMap.of("message", "Something goes wrong");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StockIsNotFoundException.class)
    public Map defaultHandler(final StockIsNotFoundException e) {
        log.error(e.getMessage(), e);
        return ImmutableMap.of("message", "Entity not found",
                               "code", e.getCode());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public Map emptyResultDataAccessExceptionHandler(final EmptyResultDataAccessException e) {
        log.error(e.getMessage(), e);
        return ImmutableMap.of("message", "Entity not found or incorrect result size");
    }

}
