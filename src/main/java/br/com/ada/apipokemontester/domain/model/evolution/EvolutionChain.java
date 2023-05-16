package br.com.ada.apipokemontester.domain.model.evolution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvolutionChain {

    private List<EvolvesTo> evolves_to;
    private Boolean is_baby;
    private Species species;
}
