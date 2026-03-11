package weather.api.weatherapi.configuration.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimit {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String ip) {
        Bucket bucket = cache.get(ip);
        if (bucket == null) {
            bucket = createNewBucket(ip);
            cache.put(ip, bucket);
        }
        return bucket;
    }

    private Bucket createNewBucket(String ip) {
        Bandwidth limit = Bandwidth.simple(5, Duration.ofMinutes(1));
        Bucket bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
        return bucket;
    }
}
