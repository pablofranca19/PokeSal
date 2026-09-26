package utils;

import models.*;
import models.pokesal.*;

import java.util.Random;
public class GerarInicial {
    private static final Random RANDOM = new Random();

    public static Inicial gerar(){
        int in = RANDOM.nextInt(6) + 1;

        return switch (in){
            case 1 -> new BulbaSal();
            case 2 -> new CharSal();
            case 3 -> new SquirtSal();
            case 4 -> new ChikoSal();
            case 5 -> new CyndaSal();
            case 6 -> new TotoSal();
            default -> throw new IllegalStateException("Valor inesperado: " + in);
        };
    }
}
