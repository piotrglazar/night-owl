package com.piotrglazar.nightowl.ui.map;

import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.concurrent.Immutable;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

@Component
public class StarSizeCalculator {

    public static final StarSize SMALLEST_STAR = new StarSize(0);

    private final List<StarSizeMatcher> starSizeMatchers;

    @Autowired
    public StarSizeCalculator(@Value("#{configProperties['star.size.thresholds']}") String rawThresholds) {
        this.starSizeMatchers = splitMap(rawThresholds);
    }

    private List<StarSizeMatcher> splitMap(final String rawThresholds) {
        return Splitter.on(",").trimResults().splitToList(rawThresholds).stream()
                .filter(s -> !s.isEmpty())
                .map(keyValue -> Splitter.on("=").trimResults().splitToList(keyValue))
                .map(keyValue -> {
                    checkArgument(keyValue.size() == 2);
                    return new StarSizeMatcher(parseDouble(keyValue.get(0)), parseInt(keyValue.get(1)));
                })
                .collect(toList());
    }

    public StarSize calculateStarSize(double magnitude) {
        return starSizeMatchers.stream()
                .filter(starSizeMatcher -> starSizeMatcher.matchesMagnitude(magnitude))
                .map(StarSizeMatcher::starSize)
                .findFirst()
                .orElse(SMALLEST_STAR);
    }

    @Immutable
    private static final class StarSizeMatcher {

        private final double magnitude;
        private final StarSize starSize;

        public StarSizeMatcher(double magnitude, int starSize) {
            this.magnitude = magnitude;
            this.starSize = new StarSize(starSize);
        }

        public boolean matchesMagnitude(double magnitude) {
            return magnitude < this.magnitude;
        }

        public StarSize starSize() {
            return starSize;
        }
    }
}
