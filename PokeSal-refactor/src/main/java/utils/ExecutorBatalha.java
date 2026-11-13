package utils;

import models.Treinador;

@FunctionalInterface
public interface ExecutorBatalha {
    ResultadoBatalha executar(Treinador jogador, Treinador oponente);
}