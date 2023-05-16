package br.com.ada.apipokemontester.domain.model.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationAreas {

    @JsonProperty("location_area")
    private LocationArea locationArea;
}
