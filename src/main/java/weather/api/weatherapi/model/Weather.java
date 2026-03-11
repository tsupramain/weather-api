package weather.api.weatherapi.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather  {
    private String dateTime;
    private String description;
    private String address;
    private List<Day> days;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Day {
        private String datetime;
        private double temp;
        private double tempmax;
        private double tempmin;
    }
}
