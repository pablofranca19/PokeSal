public class SquirtSal extends Inicial{
    public SquirtSal(){
        super("SquirtSal", Tipo.AGUA, 44, 48, 65, 43);
        this.golpes.add(new Ataque("Investida", Tipo.NORMAL, 35, EfeitoStatus.NENHUM, 0.0));
        this.golpes.add(new Ataque("Bolha", Tipo.AGUA, 20, EfeitoStatus.NENHUM, 0.0));
        this.golpes.add(new Ataque("Retirada", Tipo.AGUA, 0, EfeitoStatus.NENHUM, 0.0, null,
                new Buff(0,1,0),
                new Buff(0,0,0)));
        this.golpes.add(new Ataque("Dança da Chuva", Tipo.AGUA, 0, EfeitoStatus.NENHUM, 0.0, Terreno.POCA));
    }
}
