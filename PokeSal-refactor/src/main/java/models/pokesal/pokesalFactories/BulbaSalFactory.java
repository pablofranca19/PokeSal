package models.pokesal.pokesalFactories;

import models.pokesal.BulbaSal;

public class BulbaSalFactory implements PokemonAbstractFactory{
    @Override
    public BulbaSal criarPokemon() {
        return new BulbaSal();
    }
}
