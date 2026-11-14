package models;

import models.enums.EfeitoStatus;
import models.enums.Tipo;
import models.pokesal.PikaSal;
import models.pokesal.pokesalFactories.PikaSalFactory;
import models.pokesal.pokesalFactories.PokemonAbstractFactory;
import models.terrains.Canteiro;
import models.terrains.Terreno;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.GeradorDoubleAleatorio;
import services.GeradorInteiroAleatorio;
import services.ServicoTerreno;
import support.PokemonDeTeste;
import utils.Ataque;
import utils.Batalha;
import utils.CalcDano;
import utils.GerarInicial;
import utils.Mochila;
import utils.RegistroBatalha;
import utils.RepositorioResultados;
import utils.ResultadoBatalha;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes com Mockito dos 2 requisitos autorais da Fase 03.
 *
 *  RA-1: o Pokémon inicial deixa de ser escolhido diretamente e passa a ser sorteado ("procurar no mato").
 *        Colaboradores mockados: GeradorInteiroAleatorio (o sorteio) e PokemonAbstractFactory (a fábrica).
 *  RA-2: novo PokéSal do tipo elétrico, o PikaSal.
 *        Colaboradores mockados: GeradorDoubleAleatorio / GeradorInteiroAleatorio (chance de status, precisão,
 *        crítico), RegistroBatalha, ServicoTerreno, etc.
 *
 * Não mockados: PikaSal, Pokemon, Ataque e Terreno (objetos de domínio sem aleatoriedade nem I/O).
 */
@ExtendWith(MockitoExtension.class)
class RequisitosAutoraisMockitoTest {

    // =====================================================================================
    // RA-1: Pokémon inicial aleatório
    // =====================================================================================
    @Nested
    @DisplayName("RA-1: Pokémon inicial sorteado")
    class InicialAleatorio {

        @Mock
        private GeradorInteiroAleatorio gerador;
        @Mock
        private PokemonAbstractFactory fabrica0;
        @Mock
        private PokemonAbstractFactory fabrica1;
        @Mock
        private PokemonAbstractFactory fabrica2;

        private final Pokemon pokemon0 = new PokemonDeTeste("Zero", Tipo.NORMAL, 10, 10, 10, 10);
        private final Pokemon pokemon1 = new PokemonDeTeste("Um", Tipo.FOGO, 10, 10, 10, 10);
        private final Pokemon pokemon2 = new PokemonDeTeste("Dois", Tipo.AGUA, 10, 10, 10, 10);

        private GerarInicial gerarInicial() {
            return new GerarInicial(gerador, List.of(fabrica0, fabrica1, fabrica2));
        }

        @Test
        @DisplayName("RA-1: devolve o Pokémon da fábrica escolhida pelo sorteio e só ela é acionada")
        void testRA1_SorteioEscolheAFabrica() {
            when(gerador.gerarInt(3)).thenReturn(1);
            when(fabrica1.criarPokemon()).thenReturn(pokemon1);

            Pokemon sorteado = gerarInicial().gerar();

            assertSame(pokemon1, sorteado);
            verify(fabrica1, times(1)).criarPokemon();
            verify(fabrica0, never()).criarPokemon();
            verify(fabrica2, never()).criarPokemon();
        }

        @Test
        @DisplayName("RA-1: o sorteio é feito sobre a quantidade de fábricas (limite = tamanho da lista)")
        void testRA1_SorteioUsaTamanhoDaLista() {
            when(gerador.gerarInt(anyInt())).thenReturn(0);
            when(fabrica0.criarPokemon()).thenReturn(pokemon0);

            gerarInicial().gerar();

            ArgumentCaptor<Integer> limite = ArgumentCaptor.forClass(Integer.class);
            verify(gerador).gerarInt(limite.capture());
            assertEquals(3, limite.getValue());
        }

        @Test
        @DisplayName("RA-1: sorteios diferentes devolvem Pokémon diferentes, na ordem em que foram sorteados")
        void testRA1_SorteiosSucessivosDevolvemPokemonsDiferentes() {
            // retornos em sequência: 0, 2, 1
            when(gerador.gerarInt(3)).thenReturn(0, 2, 1);
            when(fabrica0.criarPokemon()).thenReturn(pokemon0);
            when(fabrica1.criarPokemon()).thenReturn(pokemon1);
            when(fabrica2.criarPokemon()).thenReturn(pokemon2);

            GerarInicial gerarInicial = gerarInicial();
            assertSame(pokemon0, gerarInicial.gerar());
            assertSame(pokemon2, gerarInicial.gerar());
            assertSame(pokemon1, gerarInicial.gerar());

            InOrder ordem = inOrder(fabrica0, fabrica2, fabrica1);
            ordem.verify(fabrica0).criarPokemon();
            ordem.verify(fabrica2).criarPokemon();
            ordem.verify(fabrica1).criarPokemon();
            verify(gerador, times(3)).gerarInt(3);
        }

        @Test
        @DisplayName("RA-1: com a lista padrão, os 7 PokéSal podem ser sorteados (inclusive o PikaSal)")
        void testRA1_ListaPadraoCobreOsSetePokesal() {
            // o gerador mockado "percorre" todos os índices possíveis, de 0 a 6
            when(gerador.gerarInt(7)).thenReturn(0, 1, 2, 3, 4, 5, 6);
            GerarInicial gerarInicial = new GerarInicial(gerador);

            Set<String> nomes = new HashSet<>();
            for (int i = 0; i < 7; i++) {
                nomes.add(gerarInicial.gerar().getNome());
            }

            assertEquals(7, nomes.size(), "cada índice deveria gerar um PokéSal diferente");
            assertTrue(nomes.contains("PikaSal"));
        }

        @Test
        @DisplayName("RA-1: sorteio fora da faixa lança exceção e nenhuma fábrica é acionada")
        void testRA1_SorteioForaDaFaixaLancaExcecao() {
            when(gerador.gerarInt(3)).thenReturn(5);

            GerarInicial gerarInicial = gerarInicial();
            assertThrows(IndexOutOfBoundsException.class, gerarInicial::gerar);
            verifyNoInteractions(fabrica0, fabrica1, fabrica2);
        }

        @Test
        @DisplayName("RA-1: falha numa fábrica é propagada (thenThrow)")
        void testRA1_FalhaNaFabricaEPropagada() {
            when(gerador.gerarInt(3)).thenReturn(2);
            when(fabrica2.criarPokemon()).thenThrow(new IllegalStateException("fábrica quebrou"));

            GerarInicial gerarInicial = gerarInicial();
            assertThrows(IllegalStateException.class, gerarInicial::gerar);
        }
    }

    // =====================================================================================
    // RA-2: PikaSal (elétrico)
    // =====================================================================================
    @Nested
    @DisplayName("RA-2: PikaSal (tipo elétrico)")
    class PikaSalEletrico {

        @Mock
        private GeradorDoubleAleatorio geradorDouble;
        @Mock
        private GeradorInteiroAleatorio geradorInt;
        @InjectMocks
        private CalcDano calcDano;

        private final Terreno terreno = new Canteiro(); // modificador 1.0
        private final PikaSal pikaSal = new PikaSal();

        private Ataque golpe(String nome) {
            return pikaSal.getGolpes().stream().filter(a -> a.nome().equals(nome)).findFirst().orElseThrow();
        }

        private Pokemon alvo(Tipo tipo) {
            return new Pokemon("Alvo", tipo, 100, 50, 50, 50);
        }

        @Test
        @DisplayName("RA-2: PikaSal é do tipo ELÉTRICO e a fábrica o cria corretamente")
        void testRA2_PikaSalEhEletrico() {
            assertEquals(Tipo.ELETRICO, pikaSal.getTipo());
            assertEquals("PikaSal", pikaSal.getNome());

            Pokemon dafabrica = new PikaSalFactory().criarPokemon();
            assertInstanceOf(PikaSal.class, dafabrica);
            assertEquals(Tipo.ELETRICO, dafabrica.getTipo());
        }

        @Test
        @DisplayName("RA-2: golpe elétrico é super efetivo contra Água e pouco efetivo contra Planta (gerador mockado)")
        void testRA2_GolpeEletricoVantagemElemental() {
            when(geradorInt.gerarInt()).thenReturn(50);        // sempre acerta
            when(geradorDouble.gerarDouble()).thenReturn(0.5); // nunca crítico
            Ataque choque = golpe("Choque do Trovão");

            int contraNormal = calcDano.calcularDano(choque, pikaSal, alvo(Tipo.NORMAL), terreno);
            int contraAgua = calcDano.calcularDano(choque, pikaSal, alvo(Tipo.AGUA), terreno);
            int contraPlanta = calcDano.calcularDano(choque, pikaSal, alvo(Tipo.PLANTA), terreno);

            assertEquals(2.0, (double) contraAgua / contraNormal, 0.1, "Água deveria receber o dobro");
            assertEquals(0.5, (double) contraPlanta / contraNormal, 0.1, "Planta deveria receber a metade");
        }

        @Test
        @DisplayName("RA-2: Onda Trovão (chance 100%) paralisa com qualquer sorteio válido")
        void testRA2_OndaTrovaoSempreParalisa() {
            Ataque ondaTrovao = golpe("Onda Trovão");
            when(geradorDouble.gerarDouble()).thenReturn(0.0, 0.5, 0.999);

            assertEquals(EfeitoStatus.PARALISADO, ondaTrovao.status());
            assertTrue(ondaTrovao.chanceStatus(geradorDouble));
            assertTrue(ondaTrovao.chanceStatus(geradorDouble));
            assertTrue(ondaTrovao.chanceStatus(geradorDouble));
        }

        @Test
        @DisplayName("RA-2: Choque do Trovão (chance 10%) só paraliza com sorteio abaixo de 0.10")
        void testRA2_ChoqueDoTrovaoParalisaComChanceDe10Porcento() {
            Ataque choque = golpe("Choque do Trovão");
            when(geradorDouble.gerarDouble()).thenReturn(0.05, 0.10);

            assertTrue(choque.chanceStatus(geradorDouble), "0.05 < 0.10 deveria paralisar");
            assertFalse(choque.chanceStatus(geradorDouble), "0.10 não é < 0.10");
            verify(geradorDouble, times(2)).gerarDouble();
        }

        @Test
        @DisplayName("RA-2: com sorteio 95, Onda Trovão (precisão 90) erra e Ataque Veloz (precisão 100) acerta")
        void testRA2_PrecisaoDosGolpesDoPikaSal() {
            when(geradorInt.gerarInt()).thenReturn(95);
            when(geradorDouble.gerarDouble()).thenReturn(0.5);

            assertEquals(0, calcDano.calcularDano(golpe("Onda Trovão"), pikaSal, alvo(Tipo.NORMAL), terreno));
            assertTrue(calcDano.calcularDano(golpe("Ataque Veloz"), pikaSal, alvo(Tipo.NORMAL), terreno) > 0);
        }

        @Test
        @DisplayName("RA-2: na batalha, PikaSal usa Onda Trovão e o RegistroBatalha recebe PARALISADO")
        void testRA2_BatalhaRegistraParalisiaCausadaPeloPikaSal(
                @Mock ServicoTerreno servicoTerreno,
                @Mock CalcDano calcDanoMock,
                @Mock RegistroBatalha registro,
                @Mock RepositorioResultados repositorio) {
            when(servicoTerreno.gerarTerreno()).thenReturn(terreno);
            // 0.6 -> índice (int)(0.6 * 4) = 2 = "Onda Trovão" no PikaSal; também passa na chance 1.0
            when(geradorDouble.gerarDouble()).thenReturn(0.6);
            // o oponente (paralisado, mas 0.6 >= 0.25 -> age) derruba o PikaSal e encerra a batalha
            when(calcDanoMock.calcularDano(any(Ataque.class), any(Pokemon.class), any(Pokemon.class), any(Terreno.class)))
                    .thenReturn(1000);

            Ataque golpeOponente = new Ataque("Golpe do Oponente", Tipo.NORMAL, 40, EfeitoStatus.NENHUM, 0.0, 100);
            Pokemon oponente = new PokemonDeTeste("Lento", Tipo.NORMAL, 50, 50, 50, 10, golpeOponente);
            Treinador ash = new Treinador("Ash", new Mochila(), List.of(pikaSal), false);
            Treinador gary = new Treinador("Gary", new Mochila(), List.of(oponente), false);

            ResultadoBatalha resultado = new Batalha(ash, gary, servicoTerreno, calcDanoMock, registro, repositorio,
                    geradorDouble, new Scanner(""), false).iniciarBatalha();

            verify(registro).registrarEfeitoStatus(EfeitoStatus.PARALISADO);
            assertEquals(EfeitoStatus.PARALISADO, oponente.getStatus());
            assertSame(gary, resultado.vencedor());
        }
    }
}
