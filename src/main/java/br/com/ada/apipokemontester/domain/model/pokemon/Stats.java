package br.com.ada.apipokemontester.domain.model.pokemon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
    private int baseValue;
    private int effort;

    private Stat stat;

}
