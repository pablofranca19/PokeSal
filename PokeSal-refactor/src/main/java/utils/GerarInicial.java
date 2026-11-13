package utils;

import models.*;
import models.pokesal.pokesalFactories.*;
import services.GeradorInteiroAleatorio;

import java.util.List;

public class GerarInicial {
    private final GeradorInteiroAleatorio gerador;
    private final List<PokemonAbstractFactory> factories;

    public GerarInicial(GeradorInteiroAleatorio gerador, List<PokemonAbstractFactory> factories) {
        this.gerador = gerador;
        this.factories = List.copyOf(factories);
    }

    public GerarInicial(GeradorInteiroAleatorio gerador){
        this (gerador, List.of(new BulbaSalFactory(), new CharSalFactory (), new SquirtSalFactory(),
                new ChikoSalFactory(), new CyndaSalFactory(), new TotoSalFactory(), new PikaSalFactory()));
    }

    public Pokemon gerar(){
        int in = gerador.gerarInt(factories.size());
        return factories.get(in).criarPokemon();
    }
}
