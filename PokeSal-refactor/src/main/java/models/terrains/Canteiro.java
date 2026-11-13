package models.terrains;

import models.Pokemon;
import models.enums.Tipo;

public class Canteiro extends Terreno {
    private static final int DIVIDENDO_PERCENTUAL = 5;
    private static final int DIVISOR_PERCENTUAL = 100;
    private static final int CURA_MINIMA = 1;

    @Override
    public void curaTerreno(Pokemon pokemon) {
        if (pokemon.getTipo() == Tipo.PLANTA && pokemon.getHpAtual() < pokemon.getHpMax()){
            pokemon.curar(Math.max(pokemon.getHpMax() * DIVIDENDO_PERCENTUAL / DIVISOR_PERCENTUAL, CURA_MINIMA));
        }
    }
}