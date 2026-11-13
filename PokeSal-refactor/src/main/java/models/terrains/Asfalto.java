package models.terrains;

import models.enums.Tipo;

public class Asfalto extends Terreno {
    private static final double BONUS_ATAQUE_FOGO = 1.15;
    private static final double BONUS_PADRAO = 1.0;

    @Override
    public double getMod(Tipo tipo) {
        if (tipo == Tipo.FOGO){
            return BONUS_ATAQUE_FOGO;
        }
        return BONUS_PADRAO;
    }
}
