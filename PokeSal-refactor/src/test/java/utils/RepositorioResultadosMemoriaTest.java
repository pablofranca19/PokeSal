package utils;

import models.Pokemon;
import models.Treinador;
import models.enums.Tipo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import support.PokemonDeTeste;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Repositório em memória (implementação real do RepositorioResultados). */
class RepositorioResultadosMemoriaTest {

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private PrintStream saidaOriginal;
    private RepositorioResultadosMemoria repositorio;
    private ResultadoBatalha resultado;

    @BeforeEach
    void setUp() {
        saidaOriginal = System.out;
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));

        repositorio = new RepositorioResultadosMemoria();
        Treinador ash = treinador("Ash");
        Treinador gary = treinador("Gary");
        resultado = new ResultadoBatalha(ash, gary, 4,
                List.of(ash.getPokemons().getFirst(), gary.getPokemons().getFirst()));
    }

    @AfterEach
    void restaurarSaida() {
        System.setOut(saidaOriginal);
    }

    private Treinador treinador(String nome) {
        Pokemon p = new PokemonDeTeste("Pokesal de " + nome, Tipo.NORMAL, 10, 10, 10, 10);
        return new Treinador(nome, new Mochila(), List.of(p), false);
    }

    @Test
    @DisplayName("Salva um resultado")
    void testSalvaResultado() {
        repositorio.salvarResultados(resultado);

        assertEquals(List.of(resultado), repositorio.getResultados());
    }

    @Test
    @DisplayName("Ignora resultado nulo e resultado repetido")
    void testIgnoraNuloERepetido() {
        repositorio.salvarResultados(null);
        repositorio.salvarResultados(resultado);
        repositorio.salvarResultados(resultado);

        assertEquals(1, repositorio.getResultados().size());
    }

    @Test
    @DisplayName("A lista devolvida não pode ser alterada de fora")
    void testListaDeResultadosEImutavel() {
        assertThrows(UnsupportedOperationException.class, () -> repositorio.getResultados().add(resultado));
    }

    @Test
    @DisplayName("exibirResultados imprime vencedor, perdedor e número de turnos")
    void testExibirResultados() {
        repositorio.salvarResultados(resultado);

        repositorio.exibirResultados();

        String texto = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(texto.contains("Vencedor: Ash"));
        assertTrue(texto.contains("Perdedor: Gary"));
        assertTrue(texto.contains("Turnos : 4"));
    }
}
