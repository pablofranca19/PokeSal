package utils;

import models.Pokemon;
import models.Treinador;
import models.enums.Tipo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Validações do construtor do Torneio, batalha sem resultado e torneio de 8 participantes. */
@ExtendWith(MockitoExtension.class)
class TorneioValidacaoMockitoTest {

    @Mock
    private ExecutorBatalha executor;
    @Mock
    private RepositorioResultados repositorio;

    private List<Treinador> treinadores;

    @BeforeEach
    void setUp() {
        treinadores = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            Pokemon p = new Pokemon("Pokesal " + i, Tipo.NORMAL, 50, 50, 50, 50);
            treinadores.add(new Treinador("T" + i, new Mochila(), List.of(p), false));
        }
    }

    private List<Treinador> primeiros(int n) {
        return new ArrayList<>(treinadores.subList(0, n));
    }

    @Test
    @DisplayName("Lista de participantes nula é recusada")
    void testParticipantesNulos() {
        assertThrows(IllegalArgumentException.class, () -> new Torneio(null, executor, repositorio));
    }

    @Test
    @DisplayName("Só 4 ou 8 participantes: 5 é recusado")
    void testCincoParticipantes() {
        assertThrows(IllegalArgumentException.class, () -> new Torneio(primeiros(5), executor, repositorio));
    }

    @Test
    @DisplayName("Participante nulo na lista é recusado")
    void testParticipanteNuloNaLista() {
        List<Treinador> comNulo = new ArrayList<>(Arrays.asList(treinadores.get(0), treinadores.get(1),
                treinadores.get(2), null));

        assertThrows(IllegalArgumentException.class, () -> new Torneio(comNulo, executor, repositorio));
    }

    @Test
    @DisplayName("Executor ou repositório nulos são recusados")
    void testExecutorOuRepositorioNulos() {
        assertThrows(IllegalArgumentException.class, () -> new Torneio(primeiros(4), null, repositorio));
        assertThrows(IllegalArgumentException.class, () -> new Torneio(primeiros(4), executor, null));
    }

    @Test
    @DisplayName("Batalha sem resultado (executor devolve null) invalida o torneio")
    void testExecutorDevolveNull() {
        when(executor.executar(any(Treinador.class), any(Treinador.class))).thenReturn(null);

        Torneio torneio = new Torneio(primeiros(4), executor, repositorio);

        assertThrows(IllegalStateException.class, torneio::iniciarTorneio);
    }

    @Test
    @DisplayName("Torneio de 8: 7 batalhas e o primeiro treinador de cada confronto avança até o título")
    void testTorneioDeOitoParticipantes() {
        // thenAnswer: o "vencedor" é sempre o 1º treinador do confronto (calculado na hora)
        when(executor.executar(any(Treinador.class), any(Treinador.class))).thenAnswer(invocacao -> {
            Treinador t1 = invocacao.getArgument(0);
            Treinador t2 = invocacao.getArgument(1);
            return new ResultadoBatalha(t1, t2, 1, List.of(t1.getPokemons().getFirst(), t2.getPokemons().getFirst()));
        });

        Treinador campeao = new Torneio(primeiros(8), executor, repositorio).iniciarTorneio();

        assertSame(treinadores.get(0), campeao);
        verify(executor, times(7)).executar(any(Treinador.class), any(Treinador.class));
        verify(repositorio, times(7)).salvarResultados(any(ResultadoBatalha.class));
    }
}
