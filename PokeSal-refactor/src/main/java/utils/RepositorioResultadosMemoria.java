package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepositorioResultadosMemoria implements RepositorioResultados{
    private final List<ResultadoBatalha> resultados;

    public RepositorioResultadosMemoria() {
        this.resultados = new ArrayList<>();
    }

    @Override
    public void salvarResultados(ResultadoBatalha resultado) {
        if (resultado != null && !resultados.contains(resultado)) {
            resultados.add(resultado);
        }
    }

    @Override
    public List<ResultadoBatalha> getResultados() {
        return Collections.unmodifiableList(resultados);
    }

    @Override
    public void exibirResultados() {
        for (ResultadoBatalha r : resultados){
            System.out.println("Vencedor: " + r.vencedor().nome() +
                    "\n Perdedor: " + r.perdedor().nome() +
                    "\n Turnos : " + r.turnos());
        }
    }


}
