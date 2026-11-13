package utils;

import models.Pokemon;
import models.Treinador;

import java.util.ArrayList;
import java.util.List;

public class Torneio {
    private static final int MINIMO_PARTICIPANTES = 4;
    private static final int MAXIMO_PARTICIPANTES = 8;

    private final List<Treinador> participantes;
    private final ExecutorBatalha executor;
    private final RepositorioResultados repositorio;

    public Torneio(List<Treinador> participantes, ExecutorBatalha executor, RepositorioResultados repositorio) {
        if (participantes == null || (participantes.size() != MINIMO_PARTICIPANTES && participantes.size() != MAXIMO_PARTICIPANTES)){
            throw new IllegalArgumentException("O torneio deve ter 4 OU 8 participantes.");
        } else if (participantes.contains(null) || executor == null || repositorio == null){
            throw new IllegalArgumentException("Argumento inválido. Nenhum parâmetro pode ser nulo.");
        }
        this.participantes = List.copyOf(participantes);
        this.executor = executor;
        this.repositorio = repositorio;
    }

    public Treinador iniciarTorneio(){
        List<Treinador> rodadaAtual = new ArrayList<>(participantes);

        while (rodadaAtual.size() > 1){
            List<Treinador> proximaRodada = new ArrayList<>();

            for (int i = 0; i < rodadaAtual.size(); i += 2){
                Treinador t1 = rodadaAtual.get(i);
                Treinador t2 = rodadaAtual.get(i+1);

                ResultadoBatalha resultado = executor.executar(t1, t2);

                if (resultado == null || resultado.vencedor() == null){
                    throw new IllegalStateException("Batalha encerrada sem vencedor.");
                }

                try {
                    repositorio.salvarResultados(resultado);
                } catch (Exception e){
                    System.err.println("Não foi possível salvar o resultado " + e.getMessage());
                }

                Treinador vencedor = resultado.vencedor();
                vencedor.getPokemons().forEach(Pokemon::restaurar);
                proximaRodada.add(vencedor);
            }

            rodadaAtual = proximaRodada;
        }

        return rodadaAtual.getFirst();
    }
}
