package br.com.ada.apipokemontester.domain.model.evolution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PokemonEvolutionChainDetails {
    private EvolutionChain chain;
}
