package br.com.ada.apipokemontester.domain.response;

import br.com.ada.apipokemontester.domain.model.pokemon.LocationAreas;
import br.com.ada.apipokemontester.domain.model.pokemon.Stats;
import br.com.ada.apipokemontester.domain.model.pokemon.Types;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PokemonResponse {

    private String name;
    private int height;
    private int weight;
    private List<LocationAreas> locationAreas;
    private List<Stats> stats;
    private List<Types> types;
}
