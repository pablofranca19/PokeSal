package models.terrains;

import models.Pokemon;
import models.enums.Tipo;

public abstract class Terreno {
    public double getMod(Tipo tipo) {
        return 1.0;
    }

    public void curaTerreno(Pokemon pokemon) {
    }

    @Override
    public String toString(){
        return getClass().getSimpleName();
    }
}

