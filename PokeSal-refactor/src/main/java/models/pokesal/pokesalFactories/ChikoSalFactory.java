package models.pokesal.pokesalFactories;

import models.pokesal.ChikoSal;

public class ChikoSalFactory implements PokemonAbstractFactory{
    @Override
    public ChikoSal criarPokemon() {
        return new ChikoSal();
    }
}
