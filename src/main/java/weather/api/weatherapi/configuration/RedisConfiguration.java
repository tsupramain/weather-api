package weather.api.weatherapi.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import weather.api.weatherapi.model.Weather;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Weather> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Weather> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());

        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
