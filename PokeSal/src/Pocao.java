public class Pocao extends Item{

    public Pocao(String nome) {
        super(nome);
    }

    private static final int QUANT_CURA_POCAO = 20;

    @Override
    public void usar(Inicial inicial) {
        if (inicial.getHpAtual() != inicial.getHpMax()) {
            inicial.curar(QUANT_CURA_POCAO);
        } else{
            IO.println("Não haverá efeito.");
        }
    }
}