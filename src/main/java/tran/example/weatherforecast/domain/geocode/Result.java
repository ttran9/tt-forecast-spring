package tran.example.weatherforecast.domain.geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * The data members of this class hold information about the address entered such as the entered
 * address and the latitude and longitude (location) for this address.
 */
@Getter
@Setter
@JsonIgnoreProperties(value = {"address_components", "place_id", "types", "plus_code",
        "formatted_address"})
public class Result {
    @JsonProperty(value = "geometry")
    private Geometry geometry;
}
