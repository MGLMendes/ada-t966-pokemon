package br.com.ada.apipokemontester.controller;


import br.com.ada.apipokemontester.domain.exception.PokemonIncorretoException;
import br.com.ada.apipokemontester.domain.model.Pokemon;
import br.com.ada.apipokemontester.domain.model.PokemonBattle;
import br.com.ada.apipokemontester.domain.model.PokemonBattleResponse;
import br.com.ada.apipokemontester.domain.model.pokemon.*;
import br.com.ada.apipokemontester.domain.response.PokemonEvolutionResponse;
import br.com.ada.apipokemontester.domain.response.PokemonResponse;
import br.com.ada.apipokemontester.service.PokemonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
class PokemonControllerTest {

    @Mock
    private PokemonService pokemonService;

    @InjectMocks
    private PokemonController pokemonController;

    private MockMvc mockMvc;

    private Pokemon pokemon = new Pokemon();

    private PokemonResponse pokemonResponse = new PokemonResponse();

    private PokemonEvolutionResponse pokemonEvolutionResponse;

    private PokemonBattle pokemonBattle = new PokemonBattle();

    private PokemonBattleResponse pokemonBattleResponse = new PokemonBattleResponse();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(pokemonController).build();
        initialize();
    }


    @Test
    void getPokemonWithSuccess() throws Exception {

        Mockito.when(pokemonService.getPokemonByName(Mockito.any())).thenReturn(pokemonResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/pokemon/{name}", "pikachu"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("pikachu")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.height", Matchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.weight", Matchers.is(60)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.locationAreas", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stats", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.types", Matchers.hasSize(1)));
    }

    @Test
    void getPokemonThrowingException() throws Exception {
        Mockito.when(pokemonService.getPokemonByName(Mockito.anyString()))
                .thenThrow(new PokemonIncorretoException("Esse pokemon não existe!" +
                        " Verifique se digitou corretamente o nome do Pokemon"));

        mockMvc.perform(MockMvcRequestBuilders.get("/pokemon/{name}", "charmander"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Esse pokemon não existe!" +
                        " Verifique se digitou corretamente o nome do Pokemon"));

    }

    @Test
    void getPokemonEvolutionWitchSuccess() throws Exception {
        Mockito.when(pokemonService.getPokemonEvolution(Mockito.anyString())).thenReturn(pokemonEvolutionResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/pokemon/{name}/evolution", "charmander"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.forms", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.forms", Matchers.contains("charmander", "charmeleon", "charizard")));

    }

    @Test
    void getPokemonEvolutionThrowingException() throws Exception {
        Mockito.when(pokemonService.getPokemonEvolution(Mockito.anyString()))
                .thenThrow(new PokemonIncorretoException("Esse pokemon não existe!" +
                        " Verifique se digitou corretamente o nome do Pokemon"));

        mockMvc.perform(MockMvcRequestBuilders.get("/pokemon/{name}/evolution", "charmander"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Esse pokemon não existe!" +
                        " Verifique se digitou corretamente o nome do Pokemon"));
    }


    @Test
    void pokemonBattle() throws Exception {
        Mockito.when(pokemonService.getPokemonBattle(Mockito.any(PokemonBattle.class))).thenReturn(pokemonBattleResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/pokemon/battle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(pokemonBattle)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.winner", Matchers.is("pikachu")));
    }

    @Test
    void pokemonBattleThrowingException() throws Exception {

        Mockito.when(pokemonService.getPokemonBattle(Mockito.any(PokemonBattle.class)))
                .thenThrow(new PokemonIncorretoException("Esse pokemon não existe!" +
                        " Verifique se digitou corretamente o nome do Pokemon"));

        mockMvc.perform(MockMvcRequestBuilders.post("/pokemon/battle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(pokemonBattle)))
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andExpect(MockMvcResultMatchers.content().string("Esse pokemon não existe!" +
                                " Verifique se digitou corretamente o nome do Pokemon"));

    }


    private void initialize() {

        this.pokemon = Pokemon.builder()
                .id(1L)
                .name("pikachu")
                .height(4)
                .weight(60)
                .locationAreas(locationAreas())
                .stats(stats())
                .types(types())
                .build();

        BeanUtils.copyProperties(pokemon, this.pokemonResponse);

        this.pokemonEvolutionResponse = pokemonEvolutions();

        this.pokemonBattle = PokemonBattle.builder()
                .challenger("charmander")
                .challenged("pikachu")
                .build();

        this.pokemonBattleResponse = PokemonBattleResponse.builder()
                .winner("pikachu")
                .build();

    }

    private List<Types> types() {

        Type type = Type.builder()
                .name("electric")
                .build();

        Types types = Types.builder()
                .slot(1)
                .type(type)
                .build();

        return new ArrayList<>(Collections.singletonList(types));
    }

    private List<Stats> stats() {

        Stat stat1  = Stat.builder()
                .name("hp")
                .build();

        Stat stat2  = Stat.builder()
                .name("attack")
                .build();

        Stats stats1 = Stats.builder()
                .effort(0)
                .stat(stat1)
                .baseStat(35)
                .build();

        Stats stats2 = Stats.builder()
                .effort(0)
                .stat(stat2)
                .baseStat(55)
                .build();

        return new ArrayList<>(Arrays.asList(stats1, stats2));
    }

    private List<LocationAreas> locationAreas() {

        LocationArea tropyGardenArea = LocationArea.builder()
                .name("trophy-garden-area")
                .build();

        LocationArea palletTown = LocationArea.builder()
                .name("pallet-town-area")
                .build();

        LocationArea kantoRoute = LocationArea.builder()
                .name("kanto-route-2-south-towards-viridian-city")
                .build();

        LocationAreas locationAreas1 = LocationAreas.builder()
                .locationArea(tropyGardenArea)
                .build();

        LocationAreas locationAreas2 = LocationAreas.builder()
                .locationArea(palletTown)
                .build();

        LocationAreas locationAreas3 = LocationAreas.builder()
                .locationArea(kantoRoute)
                .build();

        return new ArrayList<>(Arrays.asList(locationAreas1, locationAreas2, locationAreas3));
    }

    private PokemonEvolutionResponse pokemonEvolutions() {

        String pokemon = "charmander";
        String evolution1 = "charmeleon";
        String evolution2 = "charizard";

        return PokemonEvolutionResponse.builder()
                .evolutionNames(Arrays.asList(pokemon, evolution1, evolution2))
                .build();
    }
}