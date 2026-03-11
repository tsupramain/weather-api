package weather.api.weatherapi.restcontroller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import weather.api.weatherapi.model.Weather;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api/weather")
public class WeatherRestController {

    @Value("${api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    private final RedisTemplate<String, Weather> redisTemplate;

    public WeatherRestController(RedisTemplate<String, Weather> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/{country}")
    public Weather Weather(@PathVariable String country) {
        String cacheKey = "weather:" + country.toLowerCase();

        Weather cachedWeather = redisTemplate.opsForValue().get(cacheKey);
        if (cachedWeather != null) {
            System.out.println("Дані з кешу для: " + country);
            return cachedWeather;
        }

        String url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                + country + "/?key=" + apiKey;

        Weather weather = restClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new RuntimeException("Місто не знайдено або помилка API");
                })
                .body(Weather.class);
        System.out.println("Отримано дані від API: " + (weather != null ? weather.getAddress() : "NULL"));

        if (weather != null) {
            redisTemplate.opsForValue().set(cacheKey, weather, 12, TimeUnit.HOURS);
        }

        return weather;
    }

}








