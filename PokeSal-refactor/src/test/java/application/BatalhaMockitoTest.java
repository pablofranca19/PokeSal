package application;

import models.Pokemon;
import models.Treinador;
import models.enums.EfeitoStatus;
import models.enums.Tipo;
import models.terrains.Canteiro;
import models.terrains.Terreno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.GeradorDoubleAleatorio;
import services.ServicoTerreno;
import support.PokemonDeTeste;
import utils.Ataque;
import utils.Batalha;
import utils.CalcDano;
import utils.Mochila;
import utils.Pocao;
import utils.RegistroBatalha;
import utils.RepositorioResultados;
import utils.ResultadoBatalha;

import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BatalhaMockitoTest {
    @Mock
    private ServicoTerreno servicoTerreno;
    @Mock
    private CalcDano calcDano;
    @Mock
    private RegistroBatalha registro;
    @Mock
    private RepositorioResultados repositorio;
    @Mock
    private GeradorDoubleAleatorio gerador;

    private final Terreno terreno = new Canteiro();

    private Ataque golpeJogador;
    private Ataque golpeOponente;
    private Pokemon pokemonJogador;
    private Pokemon pokemonOponente;
    private Treinador jogador;
    private Treinador oponente;

    @BeforeEach
    void setUp() {
        golpeJogador = new Ataque("Golpe do Jogador", Tipo.NORMAL, 40, EfeitoStatus.NENHUM, 0.0, 100);
        golpeOponente = new Ataque("Golpe do Oponente", Tipo.NORMAL, 40, EfeitoStatus.NENHUM, 0.0, 100);

        pokemonJogador = new PokemonDeTeste("Rapido", Tipo.NORMAL, 100, 50, 50, 90, golpeJogador);
        pokemonOponente = new PokemonDeTeste("Lento", Tipo.NORMAL, 50, 50, 50, 10, golpeOponente);

        jogador = new Treinador("Ash", new Mochila(), List.of(pokemonJogador), false);
        oponente = new Treinador("Gary", new Mochila(), List.of(pokemonOponente), false);

        lenient().when(servicoTerreno.gerarTerreno()).thenReturn(terreno);
    }

    private Batalha criarBatalha(Treinador j, Treinador o, String entradaDoUsuario, boolean podeFugir) {
        return new Batalha(j, o, servicoTerreno, calcDano, registro, repositorio, gerador,
                new Scanner(entradaDoUsuario), podeFugir);
    }

    private void danoSempre(int dano) {
        when(calcDano.calcularDano(any(Ataque.class), any(Pokemon.class), any(Pokemon.class), any(Terreno.class)))
                .thenReturn(dano);
    }

    @Test
    @DisplayName("Eventos do turno chegam ao RegistroBatalha na ordem: ataque, dano, status, terreno, item")
    void testSequenciaDeEventosNoTurno() {
        Pocao pocao = new Pocao("Poção");
        Mochila mochila = new Mochila();
        mochila.adicionarItem(pocao);
        Treinador jogadorHumano = new Treinador("Ash", mochila, List.of(
                new PokemonDeTeste("Rapido", Tipo.NORMAL, 200, 50, 50, 90, golpeJogador)));

        Ataque brasa = new Ataque("Brasa do Oponente", Tipo.FOGO, 40, EfeitoStatus.QUEIMADO, 1.0, 100);
        Pokemon pokOponente = new PokemonDeTeste("Lento", Tipo.NORMAL, 50, 50, 50, 10, brasa);
        Treinador oponenteCpu = new Treinador("Gary", new Mochila(), List.of(pokOponente), false);

        when(gerador.gerarDouble()).thenReturn(0.0);
        when(calcDano.calcularDano(any(Ataque.class), any(Pokemon.class), any(Pokemon.class), any(Terreno.class)))
                .thenReturn(5, 100);

        Batalha batalha = criarBatalha(jogadorHumano, oponenteCpu, "2\n1\n1\n1\n", false);
        ResultadoBatalha resultado = batalha.iniciarBatalha();

        InOrder ordem = inOrder(registro);
        ordem.verify(registro).registrarAtaque(brasa);
        ordem.verify(registro).registrarDano(5);
        ordem.verify(registro).registrarEfeitoStatus(EfeitoStatus.QUEIMADO);
        ordem.verify(registro).registrarEfeitoTerreno(terreno);
        ordem.verify(registro).registrarItem(pocao);

        assertEquals(2, resultado.turnos());
    }

    @Test
    @DisplayName("Ao fim da batalha o resultado (vencedor, perdedor, turnos) é salvo no repositório")
    void testResultadoSalvoAoFimDaBatalha() {
        danoSempre(100);

        criarBatalha(jogador, oponente, "", false).iniciarBatalha();

        ArgumentCaptor<ResultadoBatalha> captor = ArgumentCaptor.forClass(ResultadoBatalha.class);
        verify(repositorio, times(1)).salvarResultados(captor.capture());

        ResultadoBatalha salvo = captor.getValue();
        assertSame(jogador, salvo.vencedor());
        assertSame(oponente, salvo.perdedor());
        assertEquals(1, salvo.turnos());
        assertEquals(List.of(pokemonJogador, pokemonOponente), salvo.pokesalUtilizados());
    }

    @Test
    @DisplayName("Se o repositório lançar exceção, o vencedor continua definido")
    void testFalhaNoRepositorioNaoInvalidaBatalha() {
        danoSempre(100);
        doThrow(new IllegalStateException("banco fora do ar"))
                .when(repositorio).salvarResultados(any(ResultadoBatalha.class));

        ResultadoBatalha resultado = criarBatalha(jogador, oponente, "", false).iniciarBatalha();

        assertNotNull(resultado, "a batalha não pode ser invalidada pela falha do repositório");
        assertSame(jogador, resultado.vencedor());
        assertSame(oponente, resultado.perdedor());
        verify(repositorio, times(1)).salvarResultados(any(ResultadoBatalha.class));
    }

    @Test
    @DisplayName("Repositório não é chamado se a batalha não terminou (jogador fugiu)")
    void testRepositorioNaoChamadoSeBatalhaNaoTerminou() {
        Treinador jogadorHumano = new Treinador("Ash", new Mochila(), List.of(pokemonJogador));

        ResultadoBatalha resultado = criarBatalha(jogadorHumano, oponente, "4\n", true).iniciarBatalha();

        assertNull(resultado, "fugir não gera resultado de batalha");
        verify(repositorio, never()).salvarResultados(any(ResultadoBatalha.class));
        verify(registro, never()).registrarAtaque(any(Ataque.class));
    }

    @Test
    @DisplayName("Batalha de vários turnos: conta turnos e registra cada golpe")
    void testBatalhaComVariosTurnosRegistraTodosOsGolpes() {
        danoSempre(10);
        pokemonOponente.setHpAtual(25);

        ResultadoBatalha resultado = criarBatalha(jogador, oponente, "", false).iniciarBatalha();

        assertEquals(3, resultado.turnos());
        assertSame(jogador, resultado.vencedor());
        verify(registro, times(5)).registrarAtaque(any(Ataque.class));
        verify(registro, times(5)).registrarEfeitoTerreno(terreno);
        verify(registro, atLeastOnce()).registrarDano(10);
        verify(calcDano, times(5))
                .calcularDano(any(Ataque.class), any(Pokemon.class), any(Pokemon.class), any(Terreno.class));
    }

    @Test
    @DisplayName("Golpe que erra (dano 0) não altera o HP do alvo e é registrado com dano 0")
    void testGolpeErradoNaBatalhaNaoAlteraHpDoAlvo() {
        when(calcDano.calcularDano(any(Ataque.class), any(Pokemon.class), any(Pokemon.class), any(Terreno.class)))
                .thenReturn(0, 100);

        ResultadoBatalha resultado = criarBatalha(jogador, oponente, "", false).iniciarBatalha();

        assertEquals(50, pokemonOponente.getHpAtual(), "o golpe errado não pode tirar HP do alvo");
        assertSame(oponente, resultado.vencedor());

        InOrder ordem = inOrder(registro);
        ordem.verify(registro).registrarDano(0);
        ordem.verify(registro).registrarDano(100);
        verify(registro, never()).registrarEfeitoStatus(any(EfeitoStatus.class));
    }
}
