package utils;

import models.Pokemon;
import models.Treinador;
import models.enums.Tipo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Torneio isolado das batalhas reais e do repositório.
 * Mockados: ExecutorBatalha (decide quem vence) e RepositorioResultados.
 * Não mockados: Treinador, Pokemon e Mochila (dados puros).
 */
@ExtendWith(MockitoExtension.class)
class TorneioMockitoTest {

    @Mock
    private ExecutorBatalha executor;
    @Mock
    private RepositorioResultados repositorio;

    private Treinador t1, t2, t3, t4;

    private Treinador treinador(String nome) {
        Pokemon p = new Pokemon("Pokesal de " + nome, Tipo.NORMAL, 50, 50, 50, 50);
        return new Treinador(nome, new Mochila(), List.of(p), false);
    }

    private ResultadoBatalha resultado(Treinador vencedor, Treinador perdedor) {
        return new ResultadoBatalha(vencedor, perdedor, 3,
                List.of(vencedor.getPokemons().getFirst(), perdedor.getPokemons().getFirst()));
    }

    /**
     * Lista MUTÁVEL de propósito: o construtor do Torneio chama participantes.contains(null), e isso lança
     * NullPointerException em listas imutáveis (List.of). Bug registrado no relatório (ver resumo da entrega).
     */
    private List<Treinador> participantes(Treinador... ts) {
        return new ArrayList<>(List.of(ts));
    }

    @BeforeEach
    void setUp() {
        t1 = treinador("T1");
        t2 = treinador("T2");
        t3 = treinador("T3");
        t4 = treinador("T4");
    }

    @Test
    @DisplayName("Chaveamento de 4: resultados mockados definem quem avança até o campeão")
    void testChaveamentoTorneioAvancaVencedores() {
        // valores exatos (e não any()) porque o que se testa é QUEM enfrenta QUEM em cada fase
        when(executor.executar(t1, t2)).thenReturn(resultado(t1, t2)); // semifinal 1: T1 avança
        when(executor.executar(t3, t4)).thenReturn(resultado(t4, t3)); // semifinal 2: T4 avança
        when(executor.executar(t1, t4)).thenReturn(resultado(t4, t1)); // final: T4 campeão

        Treinador campeao = new Torneio(participantes(t1, t2, t3, t4), executor, repositorio).iniciarTorneio();

        assertSame(t4, campeao);

        InOrder ordem = inOrder(executor);
        ordem.verify(executor).executar(t1, t2);
        ordem.verify(executor).executar(t3, t4);
        ordem.verify(executor).executar(t1, t4);
        verify(executor, times(3)).executar(any(Treinador.class), any(Treinador.class));
        verify(repositorio, times(3)).salvarResultados(any(ResultadoBatalha.class));
    }

    @Test
    @DisplayName("Falha ao salvar no repositório não interrompe o torneio")
    void testTorneioContinuaQuandoRepositorioFalha() {
        when(executor.executar(any(Treinador.class), any(Treinador.class)))
                .thenReturn(resultado(t1, t2), resultado(t3, t4), resultado(t1, t3));
        doThrow(new IllegalStateException("banco fora do ar"))
                .when(repositorio).salvarResultados(any(ResultadoBatalha.class));

        Treinador campeao = new Torneio(participantes(t1, t2, t3, t4), executor, repositorio).iniciarTorneio();

        assertSame(t1, campeao);
        verify(repositorio, times(3)).salvarResultados(any(ResultadoBatalha.class));
    }

    @Test
    @DisplayName("Se o executor de batalha lançar exceção, o torneio propaga o erro")
    void testExecutorQueFalhaPropagaExcecao() {
        when(executor.executar(t1, t2)).thenThrow(new IllegalStateException("batalha quebrou"));

        Torneio torneio = new Torneio(participantes(t1, t2, t3, t4), executor, repositorio);

        assertThrows(IllegalStateException.class, torneio::iniciarTorneio);
        verify(repositorio, never()).salvarResultados(any(ResultadoBatalha.class));
    }

    @Test
    @DisplayName("Batalha sem vencedor invalida o torneio")
    void testBatalhaSemVencedorLancaExcecao() {
        when(executor.executar(any(Treinador.class), any(Treinador.class)))
                .thenReturn(new ResultadoBatalha(null, null, 0, List.of()));

        Torneio torneio = new Torneio(participantes(t1, t2, t3, t4), executor, repositorio);

        assertThrows(IllegalStateException.class, torneio::iniciarTorneio);
    }

    @Test
    @DisplayName("Torneio só aceita 4 ou 8 participantes")
    void testNumeroInvalidoDeParticipantes() {
        assertThrows(IllegalArgumentException.class,
                () -> new Torneio(participantes(t1, t2, t3), executor, repositorio));
    }
}
