package utils;

import models.Pokemon;
import models.Treinador;
import services.GeradorInteiroAleatorio;

import java.util.ArrayList;
import java.util.List;

public class GerarTreinador {
    private static final List<String> NOMES_TREINADORES = List.of(
            "Osvaldo", "Sheila", "Cléber", "Mario", "Odete", "Marcos",
            "Everton", "Cristianne", "Patrícia", "Fernando"
    );

    private final GeradorInteiroAleatorio geradorInteiro;
    private final GerarInicial gerarInicial;
    private final List<String> nomesDisponiveis;

    public GerarTreinador(GeradorInteiroAleatorio geradorInteiro, GerarInicial gerarInicial) {
        this.geradorInteiro = geradorInteiro;
        this.gerarInicial = gerarInicial;
        this.nomesDisponiveis = new ArrayList<>(NOMES_TREINADORES);
    }

    public Treinador gerar(Mochila mochila) {
        if (nomesDisponiveis.isEmpty()) {
            throw new IllegalStateException("Não há mais nomes de treinadores disponíveis.");
        }

        int indice = geradorInteiro.gerarInt(nomesDisponiveis.size());
        String nomeSorteado = nomesDisponiveis.remove(indice);
        Pokemon pokemon = gerarInicial.gerar();

        return new Treinador(nomeSorteado, mochila, List.of(pokemon), false);
    }
}