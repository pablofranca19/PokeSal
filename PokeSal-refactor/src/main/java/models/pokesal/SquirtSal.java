package models.pokesal;

import models.Pokemon;
import models.enums.EfeitoStatus;
import models.terrains.Poca;
import models.enums.Tipo;
import utils.Ataque;
import utils.Buff;

public class SquirtSal extends Pokemon {
    public SquirtSal(){
        super("SquirtSal", Tipo.AGUA, 44, 48, 65, 43);
        this.golpes.add(new Ataque("Investida", Tipo.NORMAL, 35, EfeitoStatus.NENHUM, 0.0, 95));
        this.golpes.add(new Ataque("Bolha", Tipo.AGUA, 20, EfeitoStatus.NENHUM, 0.0, 100));
        this.golpes.add(new Ataque("Retirada", Tipo.AGUA, 0, EfeitoStatus.NENHUM, 0.0, null,
                new Buff(0,1,0),
                new Buff(0,0,0), 100));
        this.golpes.add(new Ataque("Dança da Chuva", Tipo.AGUA, 0, EfeitoStatus.NENHUM, 0.0, new Poca()));
    }
}
