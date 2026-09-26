package utils;

import models.enums.Terreno;

import java.util.Random;
public class GerarTerreno {
    private static final Random RANDOM = new Random();

    public static Terreno gerar(){
        int te = RANDOM.nextInt(3) + 1;

        return switch (te){
            case 1 -> Terreno.ASFALTO;
            case 2 -> Terreno.POCA;
            case 3 -> Terreno.CANTEIRO;
            default -> throw new IllegalStateException("Valor inesperado: " + te);
        };
    }
}
