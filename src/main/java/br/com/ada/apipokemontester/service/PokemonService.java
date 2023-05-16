package br.com.ada.apipokemontester.service;

import br.com.ada.apipokemontester.domain.model.*;
import br.com.ada.apipokemontester.domain.model.evolution.EvolutionChain;
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

                assert locationAreas != null;
                pokemon.setLocationAreas(List.of(locationAreas));

                BeanUtils.copyProperties(pokemon, pokemonResponse);
            }
            return pokemonResponse;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }
    public PokemonEvolutionResponse getPokemonEvolution(String name) {

        PokemonEvolutionResponse response = new PokemonEvolutionResponse();

        List<String> evolutionNames = response.getEvolutionNames();

        Pokemon pokemon = getPokemon(name);

        assert pokemon != null;
        PokemonEvolution evolutionChainUrl = restTemplate.getForObject(
                BASE_URL+"/pokemon-species/"+pokemon.getId(), PokemonEvolution.class);

        assert evolutionChainUrl != null;

        String chainUrl = evolutionChainUrl.getEvolutionChainUrl().getUrl();

        PokemonEvolutionChainDetails evolutionChain = restTemplate.getForObject(chainUrl, PokemonEvolutionChainDetails.class);
        assert evolutionChain != null;
        evolutionNames.add(evolutionChain.getChain().getSpecies().getName());

        for (EvolvesTo evolves : evolutionChain.getChain().getEvolves_to()) {
            evolutionNames.add(evolves.getSpecies().getName());
            for (EvolvesTo evolves1 : evolves.getEvolves_to()) {
                evolutionNames.add(evolves1.getSpecies().getName());
            }
        }

        return response;
    }



    private Pokemon getPokemon(String name) {
        return restTemplate.getForObject(BASE_URL + "/pokemon/" + name, Pokemon.class);
    }

}
