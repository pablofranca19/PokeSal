import java.util.Random;
public class CalcDano {
    private static final int NIVEL = 5;
    protected static final double SUPER_EFETIVO = 2.0;
    protected static final double POUCO_EFETIVO = 0.5;
    protected static final double NEUTRO = 1.0;
    private static final int FATOR_NIVEL = 2;
    private static final int CONST_NIVEL = 10;
    private static final double DIVISOR = 250.0;
    private static final int CONST_FINAL = 2;
    private static final Random RANDOM = new Random();
    private static boolean foiCrit = false;

    public static double calcEfet(Ataque atk, Tipo defensor) {
        return switch (atk.getTipo()) {
            case PLANTA -> (defensor == Tipo.FOGO) ? POUCO_EFETIVO : (defensor == Tipo.AGUA ? SUPER_EFETIVO : NEUTRO);
            case FOGO   -> (defensor == Tipo.PLANTA) ? SUPER_EFETIVO : (defensor == Tipo.AGUA ? POUCO_EFETIVO : NEUTRO);
            case AGUA   -> (defensor == Tipo.FOGO) ? SUPER_EFETIVO : (defensor == Tipo.PLANTA ? POUCO_EFETIVO : NEUTRO);
            default     -> NEUTRO;
        };
    }

    public static boolean calcSTAB(Inicial atacante, Ataque atk){
        return (atacante.getTipo() == atk.getTipo());
    }

    private static final double MULTI_CRIT = 2.0;
    private static final double MULT_STAB = 1.5;
    private static final int DENOMINADOR_CRIT = 16;
    private static final int NUMERADOR_CRIT = 1;

    public static boolean calcCrit(){
        int binario = RANDOM.nextInt(DENOMINADOR_CRIT);

        return (binario == NUMERADOR_CRIT);
    }

    public static int calcularDano(Ataque atk, Inicial atacante, Inicial alvo, Terreno terreno){
        double mult = calcEfet(atk, alvo.getTipo());
        double modTerreno = terreno.getMod(atk.getTipo());
        double crit = 1.0;
        double stab = 1.0;

        if (calcCrit()){
            crit = MULTI_CRIT;
            foiCrit = true;
        } else {
            foiCrit = false;
        }
        if (calcSTAB(atacante, atk)) stab = MULT_STAB;

        double dano = (((FATOR_NIVEL * NIVEL + CONST_NIVEL)/DIVISOR)
                * ((double) atacante.getAtk()/alvo.getDef())
                * atk.getPoder() + CONST_FINAL)
                * mult * modTerreno * crit * stab;

        return (int) dano;
    }

    public static boolean foiCritico(){
        return foiCrit;
    }
}
