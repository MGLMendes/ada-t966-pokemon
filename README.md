# ada-t966-pokemon

## Bem-vindo ao Pokemon Tester

## API criada para o projeto final do curso Jornada do Conhecimento Back-End JAVA - ADA - Testes Unitários

API com 3 endpoints disponíveis e x% de cobertura de testes realizados com JUnit e Mockito

Para começar clone o repositório: https://github.com/MGLMendes/ada-t966-pokemon

Rode a aplicação Spring Boot

Endpoints disponíveis: <br/>
1.<br/>
GET -> localhost:8080/pokemon/{pokemonName} <br/>
Endpoint que encontra do Pokemon escolhido trazendo alguns de suas características. Troque onde está escrito {pokemonName} pelo nome do Pokemon.<br/>

2.<br/>
GET -> localhost:8080/pokemon/{pokemonName}/evolution <br/>
Endpoint que descreve todas as evoluções do Pokemon escolhido. Troque onde está escrito {pokemonName} pelo nome do Pokemon.<br/>

3.<br/>
POST -> localhost:8080/pokemon/battle <br/>
Exemplo de body:<br/>
{<br/>
  "challenger": "charmander",<br/>
  "challenged": "pikachu"<br/>
}<br/>
Endpoint que coloca os Pokemons escolhidos em uma batalha. 

## Espero que goste! 
