package services;

import java.util.Random;

public class GeradorService implements GeradorDoubleAleatorio, GeradorInteiroAleatorio {
    private static final Random RANDOM = new Random();
    private static final int PRECISAO_MINIMA = 0;
    private static final int PRECISAO_MAXIMA = 100;

    @Override
    public double gerarDouble() {
        return RANDOM.nextDouble();
    }

    @Override
    public int gerarInt() {
        return RANDOM.nextInt(PRECISAO_MINIMA, PRECISAO_MAXIMA + 1);
    }

    @Override
    public int gerarInt(int limite){
        return RANDOM.nextInt(limite);
    }
}
