package tran.example.weatherforecast.domain.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * For the Geocode API there are multiple JSON objects which hold a latitude and longitude pair
 * so the purpose of this class is to provide a common parent class which holds data members
 * which all the inheriting classes/objects share.
 */
@Getter
@Setter
public abstract class AbstractLocation {
    /**
     * The latitude portion of the address the user is searching for.
     */
    @JsonProperty("lat")
    private Double latitude;
    /**
     * The longitude portion of the address the user is searching for.
     */
    @JsonProperty("lng")
    private Double longitude;
}
