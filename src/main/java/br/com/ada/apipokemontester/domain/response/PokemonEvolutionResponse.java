package br.com.ada.apipokemontester.domain.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PokemonEvolutionResponse {

    @JsonProperty("forms")
    private List<String> evolutionNames = new ArrayList<>();
}
