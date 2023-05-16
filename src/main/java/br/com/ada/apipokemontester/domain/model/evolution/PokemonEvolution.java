package br.com.ada.apipokemontester.domain.model.evolution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PokemonEvolution {

    @JsonProperty("evolution_chain")
    private EvolutionChainUrl evolutionChainUrl;
}
