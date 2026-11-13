package utils;

import models.Pokemon;

public class Pocao extends Item {

    public Pocao(String nome) {
        super(nome);
    }

    private static final int QUANT_CURA_POCAO = 20;

    @Override
    public void usar(Pokemon pokemon) {
        if (pokemon.getHpAtual() != pokemon.getHpMax()) {
            pokemon.curar(QUANT_CURA_POCAO);
        } else{
            System.out.println("Não haverá efeito.");
        }
    }
}