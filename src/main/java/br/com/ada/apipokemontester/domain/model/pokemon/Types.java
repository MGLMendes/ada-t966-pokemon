package br.com.ada.apipokemontester.domain.model.pokemon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Types {

    private Integer slot;

    private Type type;
}
