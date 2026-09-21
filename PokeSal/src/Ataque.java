import java.util.Random;
public class Ataque {
    private final String nome;
    private final Tipo tipo;
    private final int poder;
    private final EfeitoStatus status;
    private final double chanceEfeito;
    private Terreno ativaTerreno;
    private static final Random RANDOM = new Random();

    private Buff buffUsuario;
    private Buff buffAlvo;

    public Ataque(String nome, Tipo tipo, int poder, EfeitoStatus status, double chanceEfeito){
        this(nome, tipo, poder, status, chanceEfeito, null, null, null);
    }

    public Ataque(String nome, Tipo tipo, int poder, EfeitoStatus status, double chanceEfeito, Terreno ativaTerreno){
        this(nome, tipo, poder, status, chanceEfeito, ativaTerreno, null, null);
    }

    public Ataque(String nome, Tipo tipo, int poder, EfeitoStatus status, double chanceEfeito,
                  Terreno ativaTerreno, Buff buffUsuario, Buff buffAlvo){
        this.nome = nome;
        this.tipo = tipo;
        this.poder = poder;
        this.status = status;
        this.chanceEfeito = chanceEfeito;
        this.ativaTerreno = ativaTerreno;
        this.buffUsuario = buffUsuario;
        this.buffAlvo = buffAlvo;
    }

    public static boolean chanceStatus(double chanceEfeito){
        return RANDOM.nextDouble() < chanceEfeito;
    }

    public Terreno getAtivaTerreno(){
        return ativaTerreno;
    }

    public String getNome(){
        return nome;
    }

    public EfeitoStatus getStatus(){
        return status;
    }

    public double getChanceEfeito(){
        return chanceEfeito;
    }

    public int getPoder(){
        return poder;
    }

    public Tipo getTipo(){
        return tipo;
    }

    public Buff getBuffUsuario(){
        return buffUsuario;
    }

    public Buff getBuffAlvo(){
        return buffAlvo;
    }

    @Override
    public String toString() {
        return nome + " (" + tipo + " - " + poder + ")";
    }
}
