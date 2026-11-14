package utils;

import models.Pokemon;
import models.Treinador;
import models.enums.Tipo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.GeradorInteiroAleatorio;
import support.PokemonDeTeste;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GerarTreinadorMockitoTest {
    @Mock
    private GeradorInteiroAleatorio gerador;
    @Mock
    private GerarInicial gerarInicial;

    private final Pokemon pokemon = new PokemonDeTeste("Qualquer", Tipo.NORMAL, 10, 10, 10, 10);

    @Test
    @DisplayName("O índice sorteado escolhe o nome, e o nome sai da lista (não se repete)")
    void testNomeSorteadoSaiDaLista() {
        when(gerador.gerarInt(anyInt())).thenReturn(0);
        when(gerarInicial.gerar()).thenReturn(pokemon);
        GerarTreinador gerarTreinador = new GerarTreinador(gerador, gerarInicial);

        Treinador primeiro = gerarTreinador.gerar(new Mochila());
        Treinador segundo = gerarTreinador.gerar(new Mochila());

        assertEquals("Osvaldo", primeiro.nome());
        assertEquals("Sheila", segundo.nome());
        InOrder ordem = inOrder(gerador);
        ordem.verify(gerador).gerarInt(10);
        ordem.verify(gerador).gerarInt(9);
    }

    @Test
    @DisplayName("O treinador gerado é CPU, usa a mochila recebida e o Pokémon do GerarInicial")
    void testTreinadorGeradoEhCpuComPokemonSorteado() {
        when(gerador.gerarInt(anyInt())).thenReturn(3);
        when(gerarInicial.gerar()).thenReturn(pokemon);
        Mochila mochila = new Mochila();

        Treinador treinador = new GerarTreinador(gerador, gerarInicial).gerar(mochila);

        assertFalse(treinador.isUsuario());
        assertSame(mochila, treinador.mochila());
        assertEquals(1, treinador.getPokemons().size());
        assertSame(pokemon, treinador.getPokemons().getFirst());
        verify(gerarInicial, times(1)).gerar();
    }

    @Test
    @DisplayName("Depois de 10 treinadores os nomes acabam e gerar() lança exceção")
    void testNomesDeTreinadoresAcabam() {
        when(gerador.gerarInt(anyInt())).thenReturn(0);
        when(gerarInicial.gerar()).thenReturn(pokemon);
        GerarTreinador gerarTreinador = new GerarTreinador(gerador, gerarInicial);

        Set<String> nomes = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            nomes.add(gerarTreinador.gerar(new Mochila()).nome());
        }

        assertEquals(10, nomes.size(), "nenhum nome pode se repetir");
        assertThrows(IllegalStateException.class, () -> gerarTreinador.gerar(new Mochila()));
    }
}
