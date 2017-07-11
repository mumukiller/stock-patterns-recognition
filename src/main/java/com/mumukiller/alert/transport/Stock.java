package com.mumukiller.alert.transport;


/**
 * Created by Mumukiller on 15.06.2017.
 */
public interface Stock extends OhlcContainer {
    Double getBuyPrice();
    Double getSellPrice();
}
