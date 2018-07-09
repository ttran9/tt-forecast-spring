package tran.example.weatherforecast.domain.geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * This class contains an object which holds the latitude and longitude.
 */
@Getter
@Setter
@JsonIgnoreProperties(value = {"location_type", "viewport"})
public class Geometry {
    /**
     * An object holding the latitude and longitude.
     */
    @JsonProperty(value = "location")
    private Location location;
}
