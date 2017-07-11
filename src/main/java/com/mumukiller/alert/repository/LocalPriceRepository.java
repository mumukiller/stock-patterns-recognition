package com.mumukiller.alert.repository;

import com.mumukiller.alert.converter.CandlestickConverter;
import com.mumukiller.alert.transport.Candlestick;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by Mumukiller on 17.07.2017.
 */
public interface LocalPriceRepository {

    CandlestickConverter getCandlestickConverter();

    Logger getLogger();

    default List<Candlestick> loadFrom(final Resource resource) {
        try (Stream<String> stream = Files.lines(resource.getFile().toPath())) {

            return stream.skip(1)
                    .filter(line -> !line.isEmpty())
                    .map(line -> getCandlestickConverter().convert(line))
                    .collect(toList());

        }  catch (FileNotFoundException e) {
            getLogger().error(e.getMessage(), e);
            throw new StockPricesFileIsNotFound(e, resource.getFilename());
        } catch (IOException e) {
            getLogger().error(e.getMessage(), e);
            throw new UnableToLoadFileException(e);
        }
    }
}
