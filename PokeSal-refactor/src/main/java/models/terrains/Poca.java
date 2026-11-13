package models.terrains;
import models.enums.Tipo;

public class Poca extends Terreno {
    private static final double BONUS_ATAQUE_AGUA = 1.10;
    private static final double BONUS_PADRAO = 1.0;

    @Override
    public double getMod(Tipo tipo) {
        if (tipo == Tipo.AGUA){
            return BONUS_ATAQUE_AGUA;
        } else{
            return BONUS_PADRAO;
        }
    }
}