package models.enums;


import models.Inicial;

public enum Terreno {
    ASFALTO,
    POCA,
    CANTEIRO;

    private static final double BONUS_ATAQUE_FOGO = 1.15;
    private static final double BONUS_ATAQUE_AGUA = 1.10;

    public double getMod(Tipo atk){
        if (atk == Tipo.FOGO && this == ASFALTO){
            return BONUS_ATAQUE_FOGO;
        }
        if (atk == Tipo.AGUA && this == POCA){
            return BONUS_ATAQUE_AGUA;
        }
        return 1.0;
    }

    private static final double PERCENTUAL_CURA_CANTEIRO = 0.05;

    public void curaTerreno(Inicial inicial){
        if ((this == CANTEIRO) && (inicial.getTipo() == Tipo.PLANTA)) {
            if (inicial.getHpAtual() < inicial.getHpMax()){
                int hpAntes = inicial.getHpAtual();
                int cura = (int) (inicial.getHpMax() * PERCENTUAL_CURA_CANTEIRO);
                if (cura < 1) cura = 1;

                inicial.curar(cura);
                inicial.setHpAtual(Math.min(inicial.getHpMax(), inicial.getHpAtual() + cura));
                int curaReal = inicial.getHpAtual() - hpAntes;

                if (curaReal > 0){
                    IO.println(inicial.getNome() + " foi curado pelo terreno! (+" + curaReal + "HP | HP: "
                            + inicial.getHpAtual() + "/" + inicial.getHpMax() + ")");
                }
            }

        }
    }
}

