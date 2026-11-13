package models.pokesal.pokesalFactories;

import models.pokesal.SquirtSal;

public class SquirtSalFactory implements PokemonAbstractFactory{
    @Override
    public SquirtSal criarPokemon() {
        return new SquirtSal();
    }
}
