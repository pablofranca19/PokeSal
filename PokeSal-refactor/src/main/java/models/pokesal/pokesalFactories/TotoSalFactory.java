package models.pokesal.pokesalFactories;

import models.pokesal.TotoSal;

public class TotoSalFactory implements PokemonAbstractFactory{
    @Override
    public TotoSal criarPokemon() {
        return new TotoSal();
    }
}
