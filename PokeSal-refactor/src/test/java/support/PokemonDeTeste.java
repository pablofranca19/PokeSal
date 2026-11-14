package support;

import models.Pokemon;
import models.enums.Tipo;
import utils.Ataque;

import java.util.Arrays;

/**
 * Pokémon "de mentirinha" usado só nos testes: permite montar um PokéSal real
 * com exatamente os golpes que o cenário precisa (Pokemon.golpes é protected).
 * Não é mockado de propósito: é um objeto de valor simples, sem dependência externa.
 */
public class PokemonDeTeste extends Pokemon {
    public PokemonDeTeste(String nome, Tipo tipo, int hp, int atk, int def, int spd, Ataque... golpes) {
        super(nome, tipo, hp, atk, def, spd);
        this.golpes.addAll(Arrays.asList(golpes));
    }
}
