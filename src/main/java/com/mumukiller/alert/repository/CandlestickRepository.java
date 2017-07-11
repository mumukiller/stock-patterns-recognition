package com.mumukiller.alert.repository;


import com.mumukiller.alert.domain.StockModel;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Mumukiller on 16.06.2017.
 */
public interface CandlestickRepository extends CrudRepository<StockModel, String>, CustomCandlestickRepository,
        LocalPriceRepository {

}
