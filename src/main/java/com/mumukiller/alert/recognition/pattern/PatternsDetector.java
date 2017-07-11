package com.mumukiller.alert.recognition.pattern;

import com.mumukiller.alert.dto.PatternDto;
import com.mumukiller.alert.transport.Candlestick;
import com.mumukiller.alert.transport.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by Mumukiller on 17.06.2017.
 */
@Slf4j
@Service
public class PatternsDetector implements GroupOfTwoPatternDetector,
        GroupOfThreePatternDetector,
        GroupOfFourPatternDetector,
        GroupOfFivePatternDetector {

    public Optional<Pattern> findDescendingFirst(final Stream<? extends Candlestick> candlesticks){
        final List<? extends Candlestick> candlestickList = candlesticks.collect(toList());
        return Optional.of(detectGroupOfFivePattern(candlestickList)
                                   .orElse(detectGroupOfFourPattern(candlestickList)
                                                   .orElse(detectGroupOfThreePattern(candlestickList)
                                                                   .orElse(detectGroupOfTwoPattern(candlestickList)
                                                                                   .orElse(PatternDto.unknown())))));
    }

    public Optional<Pattern> findAscendingFirst(final Stream<? extends Candlestick> candlesticks){
        final List<? extends Candlestick> candlestickList = candlesticks.collect(toList());
        return Optional.of(detectGroupOfTwoPattern(candlestickList)
                                   .orElse(detectGroupOfThreePattern(candlestickList)
                                                   .orElse(detectGroupOfFourPattern(candlestickList)
                                                                   .orElse(detectGroupOfFivePattern(candlestickList)
                                                                                   .orElse(PatternDto.unknown())))));
    }

    public Stream<Pattern> scan(final Stream<? extends Candlestick> candlesticks) {
        final List<Candlestick> samples = candlesticks.collect(toList());

        if (samples.isEmpty())
            return Stream.empty();

        return ofSubLists(samples, 5)
                .map(sticks -> Stream.of(detectGroupOfTwoPattern(sticks),
                                         detectGroupOfThreePattern(sticks),
                                         detectGroupOfFourPattern(sticks),
                                         detectGroupOfFivePattern(sticks)))
                .flatMap(Function.identity())
                .filter(Optional::isPresent)
                .map(Optional::get);
    }
    /**
     * Thanks amaembo and https://github.com/amaembo/streamex
     *
     * @param source
     * @param length
     * @param <T>
     * @return
     */
    public <T> Stream<List<T>> ofSubLists(final List<T> source, final int length) {
        if (length <= 0)
            throw new IllegalArgumentException("length = " + length);
        final int size = source.size();
        if (size <= 0)
            return Stream.empty();
        final int fullChunks = (size - 1) / length;
        return IntStream.range(0, fullChunks + 1)
                .mapToObj(n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
    }
}
