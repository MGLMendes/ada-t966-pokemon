package br.com.ada.apipokemontester.controller;

import br.com.ada.apipokemontester.domain.response.PokemonEvolutionResponse;
import br.com.ada.apipokemontester.domain.response.PokemonResponse;
import br.com.ada.apipokemontester.service.PokemonService;
import br.com.ada.apipokemontester.domain.model.Pokemon;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pokemon")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;

    @GetMapping("/{name}")
    public ResponseEntity<PokemonResponse> getPokemon(@PathVariable String name) {


        PokemonResponse pokemon = pokemonService.getPokemonByName(name);
        if (pokemon != null) {
            return ResponseEntity.ok(pokemon);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/{name}/evolution")
    public ResponseEntity<?> getPokemonEvolution(@PathVariable String name) {
        PokemonEvolutionResponse pokemon = pokemonService.getPokemonEvolution(name);
        if (pokemon != null) {
            return ResponseEntity.ok(pokemon);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
