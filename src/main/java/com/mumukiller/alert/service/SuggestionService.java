package com.mumukiller.alert.service;

import com.mumukiller.alert.recognition.OrderSuggestion;


/**
 * Created by Mumukiller on 16.07.2017.
 */
public interface SuggestionService {

    OrderSuggestion suggestionFor(final String code);
}
