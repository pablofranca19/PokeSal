package utils;

import java.util.List;

public interface RepositorioResultados {
    List<ResultadoBatalha> getResultados();
    void salvarResultados (ResultadoBatalha resultado);
    void exibirResultados();
}
