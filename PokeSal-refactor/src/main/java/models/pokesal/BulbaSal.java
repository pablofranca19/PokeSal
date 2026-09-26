package models.pokesal;

import models.Inicial;
import models.enums.EfeitoStatus;
import models.enums.Tipo;
import utils.Ataque;

public class BulbaSal extends Inicial {
    public BulbaSal(){
        super("BulbaSal", Tipo.PLANTA, 45, 49, 49, 45);
        this.golpes.add(new Ataque("Investida", Tipo.NORMAL, 35, EfeitoStatus.NENHUM, 0.0));
        this.golpes.add(new Ataque("Chicote de Vinha", Tipo.PLANTA, 35, EfeitoStatus.NENHUM, 0.0));
        this.golpes.add(new Ataque("Pó Venenoso", Tipo.VENENO, 0, EfeitoStatus.ENVENENADO, 1.0));

    }
}