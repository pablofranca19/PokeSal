package models.pokesal;

import models.Inicial;
import models.enums.EfeitoStatus;
import models.enums.Tipo;
import utils.Ataque;
import utils.Buff;

public class CyndaSal extends Inicial {
    public CyndaSal(){
        super("CyndaSal", Tipo.FOGO, 39, 52, 43, 65);
        this.golpes.add(new Ataque("Investida", Tipo.NORMAL, 35, EfeitoStatus.NENHUM, 0.0));
        this.golpes.add(new Ataque("Brasa", Tipo.FOGO, 40, EfeitoStatus.QUEIMADO, 0.10));
        this.golpes.add(new Ataque("Roda Flamejante", Tipo.FOGO, 60, EfeitoStatus.NENHUM, 0.0, null,
                new Buff(0,0,1),
                new Buff(0,0,0)));
    }
}