package com.mumukiller.alert.repository;

import com.mumukiller.alert.domain.StockModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


/**
 * Created by Mumukiller on 16.06.2017.
 */
public interface StockRepository extends CrudRepository<StockModel, String> {
    List<StockModel> findByCodeIn(final List<String> codes);
    Optional<StockModel> findByCode(final String code);
}
