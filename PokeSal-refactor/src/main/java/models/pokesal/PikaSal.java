package models.pokesal;

import models.Pokemon;
import models.enums.EfeitoStatus;
import models.enums.Tipo;
import utils.Ataque;
import utils.Buff;

public class PikaSal extends Pokemon {
    public PikaSal(){
        super("PikaSal", Tipo.ELETRICO, 35, 55, 40, 90);
        this.golpes.add(new Ataque("Ataque Veloz", Tipo.NORMAL,60, EfeitoStatus.NENHUM, 0.0, 100));
        this.golpes.add(new Ataque("Choque do Trovão", Tipo.ELETRICO, 40, EfeitoStatus.PARALISADO, 0.10, 100));
        this.golpes.add(new Ataque("Onda Trovão", Tipo.ELETRICO, 0, EfeitoStatus.PARALISADO, 1.0, 90));
        this.golpes.add(new Ataque("Agilidade", Tipo.NORMAL, 0, EfeitoStatus.NENHUM, 0.0, null,
                new Buff(0,0,1),
                new Buff(0,0,0), 100));
    }
}
