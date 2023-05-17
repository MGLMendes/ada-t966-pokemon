package br.com.ada.apipokemontester.controller;

import br.com.ada.apipokemontester.domain.exception.PokemonIncorretoException;
import br.com.ada.apipokemontester.domain.model.PokemonBattle;
import br.com.ada.apipokemontester.domain.model.PokemonBattleResponse;
import br.com.ada.apipokemontester.domain.response.PokemonEvolutionResponse;
import br.com.ada.apipokemontester.domain.response.PokemonResponse;
import br.com.ada.apipokemontester.service.PokemonService;
import br.com.ada.apipokemontester.domain.model.Pokemon;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pokemon")
@RequiredArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;

    @GetMapping("/{name}")
    public ResponseEntity<?> getPokemon(@PathVariable String name) {
        try {
            PokemonResponse pokemon = pokemonService.getPokemonByName(name);
            return ResponseEntity.ok(pokemon);
        } catch (PokemonIncorretoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{name}/evolution")
    public ResponseEntity<?> getPokemonEvolution(@PathVariable String name) {
        try {
            PokemonEvolutionResponse pokemon = pokemonService.getPokemonEvolution(name);
            return ResponseEntity.ok(pokemon);
        } catch (PokemonIncorretoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/battle")
    public ResponseEntity<?> battle(@RequestBody PokemonBattle pokemonBattle) {
        try {
            PokemonBattleResponse response = pokemonService.getPokemonBattle(pokemonBattle);
            return ResponseEntity.ok(response);
        } catch (PokemonIncorretoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
