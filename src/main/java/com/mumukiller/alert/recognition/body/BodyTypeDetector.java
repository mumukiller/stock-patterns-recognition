package com.mumukiller.alert.recognition.body;

import com.mumukiller.alert.transport.OhlcContainer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Created by Mumukiller on 17.06.2017.
 */
@Slf4j
@Service
public class BodyTypeDetector {

    @Getter private final Double dojiCoefficient;
    @Getter private final Double shortCoefficient;
    @Getter private final Double longCoefficient;

    public BodyTypeDetector(@Value("${patterns.coefficents.doji}") final Double dojiCoefficient,
                            @Value("${patterns.coefficents.short}") final Double shortCoefficient,
                            @Value("${patterns.coefficents.long}") final Double longCoefficient) {
        this.dojiCoefficient = dojiCoefficient;
        this.shortCoefficient = shortCoefficient;
        this.longCoefficient = longCoefficient;
    }

    public BodyType calculateBodyType(final OhlcContainer sample, final double averageBodysize){
        final double bodysize = Math.abs(sample.getOpen() - sample.getClose());
        final double hl = sample.getHigh() - sample.getLow();

        final double shadeLow = sample.isBullish() ?
                Math.abs(sample.getOpen() - sample.getLow()) :
                Math.abs(sample.getClose() - sample.getLow());
        final double shadeHigh = sample.isBullish() ?
                Math.abs(sample.getHigh() - sample.getClose()) :
                Math.abs(sample.getHigh() - sample.getOpen());

        log.debug("Processed sample with {}, [AvgBS, BS] -> [{}, {}], [HL, SL, SH] -> [{}, {}, {}]",
                  sample,
                  averageBodysize,
                  bodysize,
                  hl,
                  shadeLow,
                  shadeHigh);

        if (isFourPriceDoji(sample))
            return BodyType.FOUR_PRICE_DOJI;


        if(isDoji(bodysize, hl)) {
            if (isDragonflyDoji(sample))
                return BodyType.DRAGONFLY_DOJI;

            if (isGravestoneDoji(sample))
                return BodyType.GRAVESTONE_DOJI;

            return BodyType.DOJI;
        }

        final boolean isMaribozu = isMaribozu(bodysize, shadeLow, shadeHigh);

        if(bodysize > averageBodysize * getLongCoefficient()) {
            if(isMaribozu)
                return BodyType.MARIBOZU_LONG;

            return BodyType.LONG;
        }

        if(isMaribozu)
            return BodyType.MARIBOZU;

        if(bodysize < averageBodysize * getShortCoefficient()) {
            if (isHammer(bodysize, shadeLow, shadeHigh))
                return BodyType.HAMMER;

            if (isInvertedHammer(bodysize, shadeLow, shadeHigh))
                return BodyType.INVERTED_HUMMER;

            if (isSpinningTop(bodysize, shadeLow, shadeHigh))
                return BodyType.SPINING_TOP;

            return BodyType.SHORT;
        }

        return BodyType.UNKNOWN;
    }

    public boolean isDragonflyDoji(final OhlcContainer sample){
        return sample.getOpen() == sample.getClose() && sample.getClose() == sample.getHigh();
    }

    public boolean isGravestoneDoji(final OhlcContainer sample){
        return sample.getOpen() == sample.getClose() && sample.getClose() == sample.getLow();
    }

    public boolean isDoji(final double bodysize, final double hl){
        return bodysize < hl * getDojiCoefficient();
    }

    public boolean isFourPriceDoji(final OhlcContainer sample){
        return sample.getOpen() == sample.getClose() && sample.getClose() == sample.getHigh() &&
                sample.getHigh() == sample.getLow();
    }

    public boolean isMaribozu(final double bodysize, final double shadeLow, final double shadeHigh){
        return (shadeLow < bodysize * 0.01 || shadeHigh < bodysize * 0.01) && bodysize > 0;
    }

    public boolean isSpinningTop(final double bodysize, final double shadeLow, final double shadeHigh){
        return (shadeLow > bodysize && shadeHigh > bodysize);
    }

    public boolean isHammer(final double bodysize, final double shadeLow, final double shadeHigh){
        return (shadeLow > bodysize * 2 && shadeHigh < bodysize * 0.1);
    }

    public boolean isInvertedHammer(final double bodysize, final double shadeLow, final double shadeHigh){
        return (shadeLow < bodysize * 0.1 && shadeHigh > bodysize * 2);
    }
}
