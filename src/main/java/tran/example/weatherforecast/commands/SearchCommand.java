package tran.example.weatherforecast.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An object which will assist with binding information from the search from to a domain object.
 */
@Setter
@Getter
@NoArgsConstructor
public class SearchCommand {
    /**
     * The address entered in the search.
     */
    private String address;
}
