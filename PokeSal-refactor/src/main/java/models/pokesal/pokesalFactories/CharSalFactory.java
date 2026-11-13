package models.pokesal.pokesalFactories;

import models.pokesal.CharSal;

public class CharSalFactory implements PokemonAbstractFactory{
    @Override
    public CharSal criarPokemon() {
        return new CharSal();
    }
}
