package models.pokesal;

import models.Pokemon;
import models.enums.EfeitoStatus;
import models.terrains.Canteiro;
import models.enums.Tipo;
import utils.Ataque;

public class ChikoSal extends Pokemon {
    public ChikoSal(){
        super("ChikoSal", Tipo.PLANTA, 45, 49, 65, 45);
        this.golpes.add(new Ataque("Pancada Corporal", Tipo.NORMAL, 30, EfeitoStatus.PARALISADO, 0.3, 95));
        this.golpes.add(new Ataque("Folha Cortante", Tipo.PLANTA, 55, EfeitoStatus.NENHUM, 0.0, 95));
        this.golpes.add(new Ataque("Pó Venenoso", Tipo.VENENO, 0, EfeitoStatus.ENVENENADO, 1.0, 75));
        this.golpes.add(new Ataque("Terreno Gramado", Tipo.PLANTA, 0, EfeitoStatus.NENHUM, 0.0, new Canteiro()));
    }
}