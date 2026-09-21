public class CharSal extends Inicial {
    public CharSal(){
        super("CharSal", Tipo.FOGO, 39, 52, 43, 65);
        this.golpes.add(new Ataque("Arranhão", Tipo.NORMAL, 40, EfeitoStatus.NENHUM, 0.0));
        this.golpes.add(new Ataque("Brasa", Tipo.FOGO, 40, EfeitoStatus.QUEIMADO, 0.10));
        this.golpes.add(new Ataque("Rosnar", Tipo.NORMAL, 0, EfeitoStatus.NENHUM, 0.0, null,
                new Buff (0, 0,0),
                new Buff (-1, 0,0)));
        this.golpes.add(new Ataque("Dia Ensolarado", Tipo.FOGO, 0, EfeitoStatus.NENHUM, 0.0, Terreno.ASFALTO));
    }
}