public class ChikoSal extends Inicial {
    public ChikoSal(){
        super("ChikoSal", Tipo.PLANTA, 45, 49, 65, 45);
        this.golpes.add(new Ataque("Pancada Corporal", Tipo.NORMAL, 30, EfeitoStatus.PARALISADO, 0.3));
        this.golpes.add(new Ataque("Folha Cortante", Tipo.PLANTA, 55, EfeitoStatus.NENHUM, 0.0));
        this.golpes.add(new Ataque("Pó Venenoso", Tipo.VENENO, 0, EfeitoStatus.ENVENENADO, 1.0));
        this.golpes.add(new Ataque("Terreno Gramado", Tipo.PLANTA, 0, EfeitoStatus.NENHUM, 0.0, Terreno.CANTEIRO));
    }
}