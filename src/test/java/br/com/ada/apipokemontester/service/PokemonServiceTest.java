package br.com.ada.apipokemontester.service;

import br.com.ada.apipokemontester.domain.exception.PokemonIncorretoException;
import br.com.ada.apipokemontester.domain.model.Pokemon;
import br.com.ada.apipokemontester.domain.model.PokemonBattle;
import br.com.ada.apipokemontester.domain.model.PokemonBattleResponse;
import br.com.ada.apipokemontester.domain.model.evolution.*;
import br.com.ada.apipokemontester.domain.model.pokemon.*;
import br.com.ada.apipokemontester.domain.response.PokemonEvolutionResponse;
import br.com.ada.apipokemontester.domain.response.PokemonResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
class PokemonServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PokemonService pokemonService;

    private Pokemon pokemon;

    private PokemonEvolution pokemonEvolution = new PokemonEvolution();

    private PokemonEvolutionChainDetails evolutionChain = new PokemonEvolutionChainDetails();

    private EvolutionChain chain = new EvolutionChain();

    private EvolvesTo evolvesTo;

    private EvolvesTo evolvesTo1;

    @Value("${base.pokemon.url}")
    private String BASE_URL;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        initialize();
    }

    @Test
    void getPokemonByName() {
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Pokemon.class)))
                .thenReturn(pokemon);

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(LocationAreas[].class)))
                .thenReturn(locationAreas().toArray(new LocationAreas[0]));

        PokemonResponse pokemonResponse = pokemonService.getPokemonByName("pikachu");

        Assert.assertEquals("pikachu", pokemonResponse.getName());
        Assert.assertEquals(4, pokemonResponse.getHeight());
        Assert.assertEquals(60, pokemonResponse.getWeight());
        List<String> expectedLocationAreas = Arrays.asList("trophy-garden-area", "pallet-town-area", "kanto-route-2-south-towards-viridian-city");
        List<String> actualLocationAreas = pokemonResponse.getLocationAreas().stream()
                .map(LocationAreas::getLocationArea)
                .map(LocationArea::getName)
                .collect(Collectors.toList());
        Assert.assertEquals(expectedLocationAreas, actualLocationAreas);
        List<String> expectedStats = Arrays.asList("hp", "attack");
        List<String> actualStats = pokemonResponse.getStats().stream()
                .map(Stats::getStat)
                .map(Stat::getName)
                .collect(Collectors.toList());
        Assert.assertEquals(expectedStats, actualStats);
        List<String> expectedTypes = Collections.singletonList("electric");
        List<String> actualTypes = pokemonResponse.getTypes().stream()
                .map(Types::getType)
                .map(Type::getName)
                .collect(Collectors.toList());
        Assert.assertEquals(expectedTypes, actualTypes);
    }

    @Test
    public void testGetPokemonByNameThrowingException() {
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Pokemon.class)))
                .thenThrow(new PokemonIncorretoException("Esse pokemon não existe!" +
                        " Verifique se digitou corretamente o nome do Pokemon"));

        Assertions.assertThrows(PokemonIncorretoException.class,
                () -> pokemonService.getPokemonByName("Esse pokemon não existe!" +
                        " Verifique se digitou corretamente o nome do Pokemon"));
    }

    @Test
    void getPokemonEvolution() {
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Pokemon.class)))
                .thenReturn(pokemon);

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(PokemonEvolution.class)))
                .thenReturn(pokemonEvolution);

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(PokemonEvolutionChainDetails.class)))
                .thenReturn(evolutionChain);

        PokemonEvolutionResponse evolutionResponse = pokemonService.getPokemonEvolution("pikachu");

        Assert.assertEquals(3, evolutionResponse.getEvolutionNames().size());
        Assert.assertEquals("pichu", evolutionResponse.getEvolutionNames().get(0));
        Assert.assertEquals("pikachu", evolutionResponse.getEvolutionNames().get(1));
        Assert.assertEquals("raichu", evolutionResponse.getEvolutionNames().get(2));
    }

    @Test
    void getPokemonEvolutionThrowingException() {
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Pokemon.class)))
                .thenThrow(new PokemonIncorretoException("Esse pokemon não existe!" +
                        " Verifique se digitou corretamente o nome do Pokemon"));

        Assertions.assertThrows(PokemonIncorretoException.class,
                () -> pokemonService.getPokemonByName("Esse pokemon não existe!" +
                        " Verifique se digitou corretamente o nome do Pokemon"));
    }


    @Test
    void getPokemonBattle() {
        Pokemon challenger = new Pokemon();
        challenger.setName("charizard");
        List<Stats> challengerStats = Collections.singletonList(Stats.builder()
                        .baseStat(80)
                        .stat(Stat.builder()
                                .name("hp")
                                .build())
                .build());
        challenger.setStats(challengerStats);

        Pokemon challenged = new Pokemon();
        challenged.setName("blastoise");
        List<Stats> challengedStats = Collections.singletonList(Stats.builder()
                        .baseStat(80)
                        .stat(Stat.builder()
                                .name("hp")
                                .build())
                .build());
        challenged.setStats(challengedStats);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Pokemon.class)))
                .thenReturn(challenger)
                .thenReturn(challenged);
        PokemonBattleResponse battleResponse = pokemonService.getPokemonBattle(
                new PokemonBattle("charizard", "blastoise"));

        Assert.assertEquals("DRAW", battleResponse.getWinner());
    }

    @Test
    void pokemonBattleThrowingException() {
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(Pokemon.class)))
                .thenThrow(new PokemonIncorretoException("Esse pokemon não existe!" +
                        " Verifique se digitou corretamente o nome do Pokemon"));

        Assertions.assertThrows(PokemonIncorretoException.class,
                () -> pokemonService.getPokemonByName("Esse pokemon não existe!" +
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

        this.evolvesTo1 = EvolvesTo.builder()
                .is_baby(Boolean.FALSE)
                .species(Species.builder()
                        .name("raichu")
                        .build())
                .build();

        this.evolvesTo = EvolvesTo.builder()
                .is_baby(Boolean.TRUE)
                .evolves_to(Collections.singletonList(evolvesTo1))
                .species(Species.builder()
                        .name("pikachu")
                        .build())
                .build();



        this.chain = EvolutionChain.builder()
                .is_baby(Boolean.TRUE)
                .species(Species.builder()
                        .name("pichu")
                        .build())
                .evolves_to(Arrays.asList(evolvesTo))
                .build();


        this.evolutionChain = PokemonEvolutionChainDetails.builder()
                .chain(chain)
                .build();

        this.pokemonEvolution = PokemonEvolution.builder()
                .evolutionChainUrl(EvolutionChainUrl.builder()
                        .url(BASE_URL+ "/pokemon-species/" + pokemon.getId())
                        .build())
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
}