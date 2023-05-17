package br.com.ada.apipokemontester.domain.exception;

public class PokemonIncorretoException extends RuntimeException {

    public PokemonIncorretoException(String message) {
        super(message);
    }
}
