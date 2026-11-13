package models.pokesal.pokesalFactories;

import models.pokesal.CyndaSal;

public class CyndaSalFactory implements PokemonAbstractFactory{
    @Override
    public CyndaSal criarPokemon() {
        return new CyndaSal();
    }
}
