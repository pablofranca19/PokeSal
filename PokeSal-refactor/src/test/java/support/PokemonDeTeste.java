package support;

import models.Pokemon;
import models.enums.Tipo;
import utils.Ataque;

import java.util.Arrays;

public class PokemonDeTeste extends Pokemon {
    public PokemonDeTeste(String nome, Tipo tipo, int hp, int atk, int def, int spd, Ataque... golpes) {
        super(nome, tipo, hp, atk, def, spd);
        this.golpes.addAll(Arrays.asList(golpes));
    }
}
