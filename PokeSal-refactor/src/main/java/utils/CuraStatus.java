package utils;

import models.Pokemon;
import models.enums.EfeitoStatus;


public class CuraStatus extends Item {
    private EfeitoStatus statusCura;

    public CuraStatus(String nome, EfeitoStatus statusCura) {
        super(nome);
        this.statusCura = statusCura;
    }

    @Override
    public void usar(Pokemon pokemon) {
        if (pokemon.getStatus() == statusCura) {
            pokemon.curarStatus();
        } else{
            System.out.println("Não haverá efeito.");
        }
    }
}
