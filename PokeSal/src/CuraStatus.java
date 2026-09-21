public class CuraStatus extends Item{
    private EfeitoStatus statusCura;

    public CuraStatus(String nome, EfeitoStatus statusCura) {
        super(nome);
        this.statusCura = statusCura;
    }

    @Override
    public void usar(Inicial inicial) {
        if (inicial.getStatus() == statusCura) {
            inicial.curarStatus();
        } else{
            IO.println("Não haverá efeito.");
        }
    }
}
