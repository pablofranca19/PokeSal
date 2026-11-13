package utils;

import models.Pokemon;
import models.Treinador;

import java.util.List;

public record ResultadoBatalha (Treinador vencedor, Treinador perdedor, int turnos, List<Pokemon> pokesalUtilizados) {
    public ResultadoBatalha {
        pokesalUtilizados = List.copyOf(pokesalUtilizados);
    }
}