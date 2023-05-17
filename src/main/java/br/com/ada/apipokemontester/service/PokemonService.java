package br.com.ada.apipokemontester.service;

import br.com.ada.apipokemontester.domain.exception.PokemonIncorretoException;
import br.com.ada.apipokemontester.domain.model.Pokemon;
import br.com.ada.apipokemontester.domain.model.PokemonBattle;
import br.com.ada.apipokemontester.domain.model.PokemonBattleResponse;
import br.com.ada.apipokemontester.domain.model.evolution.EvolvesTo;
import br.com.ada.apipokemontester.domain.model.evolution.PokemonEvolution;
import br.com.ada.apipokemontester.domain.model.evolution.PokemonEvolutionChainDetails;
import br.com.ada.apipokemontester.domain.model.pokemon.LocationAreas;
import br.com.ada.apipokemontester.domain.response.PokemonEvolutionResponse;
import br.com.ada.apipokemontester.domain.response.PokemonResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PokemonService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${base.pokemon.url}")
    private String BASE_URL;

    public PokemonResponse getPokemonByName(String name) {
        PokemonResponse pokemonResponse = new PokemonResponse();
        try {
            Pokemon pokemon = getPokemon(name);

            if (pokemon != null) {

                String url = BASE_URL+"/pokemon/"+pokemon.getId()+"/encounters";

                var locationAreas = restTemplate.getForObject(url, LocationAreas[].class);
                pokemon.setLocationAreas(List.of(locationAreas));

                BeanUtils.copyProperties(pokemon, pokemonResponse);
            }
            return pokemonResponse;
        } catch (RuntimeException e) {
            throw new PokemonIncorretoException("Esse pokemon não existe!" +
                    " Verifique se digitou corretamente o nome do Pokemon");
        }
    }
    public PokemonEvolutionResponse getPokemonEvolution(String name) {
        try {
            PokemonEvolutionResponse response = new PokemonEvolutionResponse();

            List<String> evolutionNames = response.getEvolutionNames();

            Pokemon pokemon = getPokemon(name);

            PokemonEvolution evolutionChainUrl = restTemplate.getForObject(
                    BASE_URL + "/pokemon-species/" + pokemon.getId(), PokemonEvolution.class);

            String chainUrl = evolutionChainUrl.getEvolutionChainUrl().getUrl();

            PokemonEvolutionChainDetails evolutionChain = restTemplate.getForObject(chainUrl, PokemonEvolutionChainDetails.class);
            if (evolutionChain != null) {
                evolutionNames.add(evolutionChain.getChain().getSpecies().getName());

                for (EvolvesTo evolves : evolutionChain.getChain().getEvolves_to()) {
                    evolutionNames.add(evolves.getSpecies().getName());
                    for (EvolvesTo evolves1 : evolves.getEvolves_to()) {
                        evolutionNames.add(evolves1.getSpecies().getName());
                    }
                }
                return response;
            } else {
                throw new PokemonIncorretoException("Esse pokemon não existe!" +
                        " Verifique se digitou corretamente o nome do Pokemon");
            }
        } catch (RuntimeException e) {
            throw new PokemonIncorretoException("Esse pokemon não existe!" +
                    " Verifique se digitou corretamente o nome do Pokemon");
        }
    }

    public PokemonBattleResponse getPokemonBattle(PokemonBattle pokemonBattle) {
        try {
            Pokemon challenger = getPokemon(pokemonBattle.getChallenger());
            Pokemon challenged = getPokemon(pokemonBattle.getChallenged());

            List<Integer> challengerStats = new ArrayList<>();
            List<Integer> challengedStats = new ArrayList<>();

            challenger.getStats().forEach(
                    stats -> {
                        challengerStats.add(stats.getBaseStat());
                    }
            );

            challenged.getStats().forEach(
                    stats -> {
                        challengedStats.add(stats.getBaseStat());
                    }
            );

            int somaStatsChalleger = challengerStats.stream().reduce(0, Integer::sum);
            int somaStatsChalleged = challengedStats.stream().reduce(0, Integer::sum);

            if (somaStatsChalleger > somaStatsChalleged) {
                return PokemonBattleResponse.builder()
                        .winner(challenger.getName())
                        .build();
            } else if (somaStatsChalleged > somaStatsChalleger) {
                return PokemonBattleResponse.builder()
                        .winner(challenged.getName())
                        .build();
            } else {
                return PokemonBattleResponse.builder()
                        .winner("DRAW")
                        .build();
            }
        } catch (RuntimeException e) {
            throw new PokemonIncorretoException("Esse pokemon não existe!" +
                    " Verifique se digitou corretamente o nome do Pokemon");
        }
    }
    private Pokemon getPokemon(String name) {
        return restTemplate.getForObject(BASE_URL + "/pokemon/" + name, Pokemon.class);
    }
}
