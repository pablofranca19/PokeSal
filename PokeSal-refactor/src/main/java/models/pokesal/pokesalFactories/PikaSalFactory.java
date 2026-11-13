package models.pokesal.pokesalFactories;

import models.pokesal.PikaSal;

public class PikaSalFactory implements PokemonAbstractFactory{
    @Override
    public PikaSal criarPokemon() {
        return new PikaSal();
    }
}
