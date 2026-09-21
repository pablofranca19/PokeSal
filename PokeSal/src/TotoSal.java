public class TotoSal extends Inicial{
    public TotoSal(){
        super("TotoSal", Tipo.AGUA, 50, 65, 64, 43);
        this.golpes.add(new Ataque("Arranhão", Tipo.NORMAL, 40, EfeitoStatus.NENHUM, 0.0));
        this.golpes.add(new Ataque("Revolver d'Água", Tipo.AGUA, 40, EfeitoStatus.NENHUM, 0.0));
        this.golpes.add(new Ataque("Mau-Olhado", Tipo.NORMAL, 0, EfeitoStatus.NENHUM, 0.0, null,
                new Buff(0,0,0),
                new Buff(0,-1,0)));
    }
}
