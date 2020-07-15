package uk.co.idv.context.adapter.repository;

import java.time.Duration;
import java.time.Instant;

public class DurationCalculator {

    private DurationCalculator() {
        // utility class
    }

    public static long millisBetweenNowAnd(Instant start) {
        return Duration.between(start, Instant.now()).toMillis();
    }

}
