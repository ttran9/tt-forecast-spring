package tran.example.weatherforecast.domain.geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * This holds a consists of objects that have information about the user entered address.
 */
@Getter
@Setter
@JsonIgnoreProperties(value = {"status", "plus_code"})
public class GeocodeResult {
    /**
     * An object holding the latitude and longitude of the address and the entered address.
     */
    @JsonProperty(value = "results")
    private List<Result> results;
}
