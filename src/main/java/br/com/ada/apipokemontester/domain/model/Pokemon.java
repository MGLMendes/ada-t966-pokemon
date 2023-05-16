package br.com.ada.apipokemontester.domain.model;

import br.com.ada.apipokemontester.domain.model.pokemon.LocationAreas;
import br.com.ada.apipokemontester.domain.model.pokemon.Stats;
import br.com.ada.apipokemontester.domain.model.pokemon.Types;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pokemon {


    private Long id;
    private String name;
    private int height;
    private int weight;
    @JsonProperty("location_area_encounters")
    @JsonIgnore
    private List<LocationAreas> locationAreas;
    private List<Stats> stats;
    private List<Types> types;
}
