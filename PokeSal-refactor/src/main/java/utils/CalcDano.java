package utils;

import models.Pokemon;
import models.terrains.Terreno;
import models.enums.Tipo;
import services.GeradorDoubleAleatorio;
import services.GeradorInteiroAleatorio;

public class CalcDano {
    private static final int NIVEL = 5;
    protected static final double SUPER_EFETIVO = 2.0;
    protected static final double POUCO_EFETIVO = 0.5;
    protected static final double NEUTRO = 1.0;
    private static final int FATOR_NIVEL = 2;
    private static final int CONST_NIVEL = 10;
    private static final double DIVISOR = 250.0;
    private static final int CONST_FINAL = 2;
    private static final double MULTI_CRIT = 1.5;
    private static final double MULT_STAB = 1.5;
    private static final double CHANCE_CRIT = 0.0625;
    private final GeradorDoubleAleatorio geradorDouble;
    private final GeradorInteiroAleatorio geradorInt;
    private boolean foiCrit = false;

    public CalcDano(GeradorDoubleAleatorio geradorDouble, GeradorInteiroAleatorio geradorInt){
        this.geradorDouble = geradorDouble;
        this.geradorInt = geradorInt;
    }

    public static double calcEfet(Ataque atk, Tipo defensor) {
        return switch (atk.tipo()) {
            case Tipo.PLANTA -> (defensor == Tipo.AGUA) ? SUPER_EFETIVO : (defensor == Tipo.FOGO ? POUCO_EFETIVO : NEUTRO);
            case Tipo.FOGO -> (defensor == Tipo.PLANTA) ? SUPER_EFETIVO : (defensor == Tipo.AGUA ? POUCO_EFETIVO : NEUTRO);
            case Tipo.AGUA -> (defensor == Tipo.FOGO) ? SUPER_EFETIVO : (defensor == Tipo.PLANTA ? POUCO_EFETIVO : NEUTRO);
            case Tipo.ELETRICO -> (defensor == Tipo.AGUA) ? SUPER_EFETIVO : (defensor == Tipo.PLANTA ? POUCO_EFETIVO : NEUTRO);
            default -> NEUTRO;
        };
    }

    public static boolean calcSTAB(Pokemon atacante, Ataque atk) {
        return (atacante.getTipo() == atk.tipo());
    }

    private boolean calcCrit() {
        return geradorDouble.gerarDouble() < CHANCE_CRIT;
    }

    public int calcularDano(Ataque atk, Pokemon atacante, Pokemon alvo, Terreno terreno) {
        if (geradorInt.gerarInt() > atk.precisao()) {
            foiCrit = false;
            return 0;
        }

        double mult = calcEfet(atk, alvo.getTipo());
        double modTerreno = terreno.getMod(atk.tipo());
        double crit = NEUTRO;
        double stab = NEUTRO;

        if (calcCrit()){
            crit = MULTI_CRIT;
            foiCrit = true;
        } else {
            foiCrit = false;
        }

        if (calcSTAB(atacante, atk)) {
            stab = MULT_STAB;
        }

        double dano = (((FATOR_NIVEL * NIVEL + CONST_NIVEL)/DIVISOR)
                * ((double) atacante.getAtk()/alvo.getDef())
                * atk.poder() + CONST_FINAL)
                * mult * modTerreno * crit * stab;

        return (int) dano;
    }

    public boolean foiCritico(){
        return foiCrit;
    }
}
