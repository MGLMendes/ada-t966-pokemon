package br.com.ada.apipokemontester.domain.model.pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
    @JsonProperty("base_stat")
    private int baseStat;
    private int effort;

    private Stat stat;

}
