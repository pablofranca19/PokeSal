package application;

import models.Pokemon;
import models.Treinador;
import models.enums.EfeitoStatus;
import models.enums.Tipo;
import models.terrains.Asfalto;
import models.terrains.Canteiro;
import models.terrains.Terreno;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.GeradorDoubleAleatorio;
import services.ServicoTerreno;
import support.PokemonDeTeste;
import utils.Ataque;
import utils.Batalha;
import utils.Buff;
import utils.CalcDano;
import utils.Mochila;
import utils.Pocao;
import utils.RegistroBatalha;
import utils.RepositorioResultados;
import utils.ResultadoBatalha;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Caminhos "menos comuns" da Batalha: menu do jogador, paralisia, ordem por velocidade, dano de status,
 * mudança de terreno, buffs/debuffs, efetividade, crítico. Mesmos mocks de BatalhaMockitoTest.
 *
 * Os golpes de cada cenário são montados com a classe de apoio PokemonDeTeste (um golpe só por Pokémon),
 * então o CPU sempre escolhe esse golpe (índice 0) e o teste não depende de sorteio.
 */
@ExtendWith(MockitoExtension.class)
class BatalhaCaminhosMockitoTest {

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
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private PrintStream saidaOriginal;

    private Ataque golpeJogador;
    private Ataque golpeOponente;
    private Pokemon pokemonJogador;
    private Pokemon pokemonOponente;
    private Treinador oponente;

    @BeforeEach
    void setUp() {
        // guarda o System.out para conferir mensagens impressas pela Batalha
        saidaOriginal = System.out;
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));

        golpeJogador = new Ataque("Golpe do Jogador", Tipo.NORMAL, 40, EfeitoStatus.NENHUM, 0.0, 100);
        golpeOponente = new Ataque("Golpe do Oponente", Tipo.NORMAL, 40, EfeitoStatus.NENHUM, 0.0, 100);
        pokemonJogador = pokemon("Rapido", Tipo.NORMAL, 100, 90, golpeJogador);   // SPD 90
        pokemonOponente = pokemon("Lento", Tipo.NORMAL, 50, 10, golpeOponente);   // SPD 10
        oponente = cpu("Gary", pokemonOponente);

        lenient().when(servicoTerreno.gerarTerreno()).thenReturn(terreno);
        lenient().when(servicoTerreno.getHorarioAtual()).thenReturn(LocalTime.of(10, 30));
    }

    @AfterEach
    void restaurarSaida() {
        System.setOut(saidaOriginal);
    }

    // ----------------------------------- auxiliares -----------------------------------

    private String impresso() {
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private Pokemon pokemon(String nome, Tipo tipo, int hp, int spd, Ataque... golpes) {
        return new PokemonDeTeste(nome, tipo, hp, 50, 50, spd, golpes);
    }

    private Treinador cpu(String nome, Pokemon pokemon) {
        return new Treinador(nome, new Mochila(), List.of(pokemon), false);
    }

    private Treinador humano(String nome, Pokemon pokemon) {
        return new Treinador(nome, new Mochila(), List.of(pokemon)); // usuario = true
    }

    private Batalha batalha(Treinador jogador, Treinador oponente, String entradaDoTeclado, boolean podeFugir) {
        return new Batalha(jogador, oponente, servicoTerreno, calcDano, registro, repositorio, gerador,
                new Scanner(entradaDoTeclado), podeFugir);
    }

    /** any() porque o valor exato dos argumentos não é o foco; o que importa é o dano devolvido. */
    private void danoSempre(int dano) {
        when(calcDano.calcularDano(any(Ataque.class), any(Pokemon.class), any(Pokemon.class), any(Terreno.class)))
                .thenReturn(dano);
    }

    // ------------------------------------ menu do jogador ------------------------------------

    @Test
    @DisplayName("Construtor sem 'podeFugir' (batalha selvagem): o jogador pode fugir")
    void testConstrutorPadraoPermiteFugir() {
        Batalha batalha = new Batalha(humano("Ash", pokemonJogador), oponente, servicoTerreno, calcDano,
                registro, repositorio, gerador, new Scanner("4\n"));

        assertNull(batalha.iniciarBatalha());
        verify(repositorio, never()).salvarResultados(any(ResultadoBatalha.class));
    }

    @Test
    @DisplayName("Menu: opções inválidas, inspecionar, item inválido e limite de 2 itens; depois foge")
    void testMenuDoJogadorTrataOpcoesInvalidasInspecionarELimiteDeItens() {
        Pocao pocao = new Pocao("Poção");
        Mochila mochila = new Mochila();
        mochila.adicionarItem(pocao);
        Treinador ash = new Treinador("Ash", mochila, List.of(pokemonJogador));
        danoSempre(1);

        // Teclado, na ordem em que a Batalha pede:
        //  9          -> opção de menu inválida
        //  3 1 / 3 2  -> inspecionar o próprio Pokémon / o oponente
        //  3 3 / 3 9  -> sair do inspecionar / opção inválida no inspecionar
        //  1 9        -> lutar, mas golpe inválido
        //  2 9        -> mochila, mas item inválido
        //  2 1        -> usa a Poção (gasta o turno 1)
        //  2 1        -> usa a Poção de novo (gasta o turno 2)
        //  2 1        -> 3º uso: estoura o limite de 2 itens (mensagem de erro, o turno continua)
        //  4          -> foge
        String teclado = "9\n3\n1\n3\n2\n3\n3\n3\n9\n1\n9\n2\n9\n2\n1\n2\n1\n2\n1\n4\n";

        ResultadoBatalha resultado = batalha(ash, oponente, teclado, true).iniciarBatalha();

        assertNull(resultado, "fugiu: não há resultado");
        verify(registro, times(2)).registrarItem(pocao);               // só os 2 usos válidos
        verify(calcDano, times(2)).calcularDano(any(Ataque.class), any(Pokemon.class), any(Pokemon.class),
                any(Terreno.class));                                    // o oponente atacou nos 2 turnos de item
        assertTrue(impresso().contains("Você só pode usar 2 itens por batalha!"));
        assertTrue(impresso().contains("Opção inválida."));
    }

    @Test
    @DisplayName("Em batalha de treinador não é possível fugir; o jogador então luta e vence")
    void testNaoPodeFugirDeBatalhaDeTreinador() {
        danoSempre(100);

        // "4" tenta fugir (negado); "1" lutar; "1" primeiro golpe
        ResultadoBatalha resultado = batalha(humano("Ash", pokemonJogador), oponente, "4\n1\n1\n", false)
                .iniciarBatalha();

        assertTrue(impresso().contains("Não é possível fugir de uma batalha de treinador!"));
        assertEquals("Ash", resultado.vencedor().nome());
        verify(registro).registrarAtaque(golpeJogador);
    }

    // ------------------------------------ paralisia ------------------------------------

    @Test
    @DisplayName("Jogador humano paralisado (sorteio 0.10 < 0.25) perde o turno e só o oponente ataca")
    void testJogadorHumanoParalisadoPerdeOTurno() {
        pokemonJogador.setStatus(EfeitoStatus.PARALISADO);
        when(gerador.gerarDouble()).thenReturn(0.10);
        danoSempre(1000);

        ResultadoBatalha resultado = batalha(humano("Ash", pokemonJogador), oponente, "1\n1\n", false)
                .iniciarBatalha();

        verify(registro, never()).registrarAtaque(golpeJogador);
        verify(registro).registrarAtaque(golpeOponente);
        assertSame(oponente, resultado.vencedor());
    }

    @Test
    @DisplayName("Jogador CPU paralisado perde o turno e só o oponente ataca")
    void testJogadorCpuParalisadoPerdeOTurno() {
        pokemonJogador.setStatus(EfeitoStatus.PARALISADO);
        when(gerador.gerarDouble()).thenReturn(0.10);
        danoSempre(1000);

        ResultadoBatalha resultado = batalha(cpu("Ash", pokemonJogador), oponente, "", false).iniciarBatalha();

        verify(registro, never()).registrarAtaque(golpeJogador);
        assertSame(oponente, resultado.vencedor());
    }

    @Test
    @DisplayName("Oponente paralisado (sorteio 0.10) nunca consegue atacar")
    void testOponenteParalisadoNaoConsegueAtacar() {
        pokemonOponente.setStatus(EfeitoStatus.PARALISADO);
        when(gerador.gerarDouble()).thenReturn(0.10);
        danoSempre(10); // oponente tem 50 de HP: cai no 5º golpe

        ResultadoBatalha resultado = batalha(cpu("Ash", pokemonJogador), oponente, "", false).iniciarBatalha();

        assertEquals(5, resultado.turnos());
        verify(registro, times(5)).registrarAtaque(golpeJogador);
        verify(registro, never()).registrarAtaque(golpeOponente);
        assertTrue(impresso().contains("está paralisado! Não consegue se mover!"));
    }

    // ------------------------------------ velocidade ------------------------------------

    @Test
    @DisplayName("Oponente mais rápido ataca primeiro; o jogador ataca em seguida")
    void testOponenteMaisRapidoAtacaPrimeiro() {
        Pokemon lento = pokemon("Jogador lento", Tipo.NORMAL, 100, 10, golpeJogador);
        Pokemon rapido = pokemon("Oponente rapido", Tipo.NORMAL, 15, 90, golpeOponente);
        danoSempre(10);

        batalha(cpu("Ash", lento), cpu("Gary", rapido), "", false).iniciarBatalha();

        InOrder ordem = inOrder(registro);
        ordem.verify(registro).registrarAtaque(golpeOponente);
        ordem.verify(registro).registrarAtaque(golpeJogador);
    }

    @Test
    @DisplayName("Se o oponente mais rápido derruba o jogador, o jogador não chega a atacar")
    void testOponenteRapidoDerrubaJogadorAntesDeAgir() {
        Pokemon lento = pokemon("Jogador lento", Tipo.NORMAL, 100, 10, golpeJogador);
        Pokemon rapido = pokemon("Oponente rapido", Tipo.NORMAL, 50, 90, golpeOponente);
        danoSempre(1000);

        ResultadoBatalha resultado = batalha(cpu("Ash", lento), cpu("Gary", rapido), "", false).iniciarBatalha();

        verify(registro, never()).registrarAtaque(golpeJogador);
        assertEquals("Gary", resultado.vencedor().nome());
    }

    // ------------------------------------ dano de status ------------------------------------

    @Test
    @DisplayName("Fim do turno: jogador envenenado perde 1/8 do HP máx. e oponente queimado perde 1/16")
    void testDanoDeStatusJogadorEnvenenadoEOponenteQueimado() {
        Pokemon jogadorEnvenenado = pokemon("Jogador", Tipo.NORMAL, 160, 90, golpeJogador);
        jogadorEnvenenado.setStatus(EfeitoStatus.ENVENENADO);
        Pokemon oponenteQueimado = pokemon("Oponente", Tipo.NORMAL, 25, 10, golpeOponente);
        oponenteQueimado.setStatus(EfeitoStatus.QUEIMADO);
        danoSempre(10);

        ResultadoBatalha resultado = batalha(cpu("Ash", jogadorEnvenenado), cpu("Gary", oponenteQueimado), "", false)
                .iniciarBatalha();

        // 3 turnos. Turnos 1 e 2: o oponente bate 10 (-10) e o veneno tira 160/8 = 20: 160 - 2*30 = 100
        assertEquals(3, resultado.turnos());
        assertEquals(100, jogadorEnvenenado.getHpAtual());
        assertTrue(impresso().contains("sofreu dano pelo veneno!"));
        assertTrue(impresso().contains("sofreu dano pela queimadura!"));
    }

    @Test
    @DisplayName("Fim do turno: oponente envenenado perde 1/8 do HP máx.")
    void testDanoDeStatusOponenteEnvenenado() {
        Pokemon jogadorQueimado = pokemon("Jogador", Tipo.NORMAL, 160, 90, golpeJogador);
        jogadorQueimado.setStatus(EfeitoStatus.QUEIMADO);
        Pokemon oponenteEnvenenado = pokemon("Oponente", Tipo.NORMAL, 50, 10, golpeOponente);
        oponenteEnvenenado.setHpAtual(25);
        oponenteEnvenenado.setStatus(EfeitoStatus.ENVENENADO);
        danoSempre(10);

        batalha(cpu("Ash", jogadorQueimado), cpu("Gary", oponenteEnvenenado), "", false).iniciarBatalha();

        // turno 1: oponente 25 -10 = 15, depois o veneno tira 50/8 = 6; turno 2: o golpe de 10 o derruba
        assertTrue(impresso().contains("sofreu dano pelo veneno!"));
        assertEquals(0, oponenteEnvenenado.getHpAtual());
    }

    // ------------------------------------ terreno ------------------------------------

    @Test
    @DisplayName("Se o serviço de terreno passa a devolver outro terreno, a batalha o adota")
    void testTerrenoDoServicoMudaDuranteABatalha() {
        // 1ª chamada (construtor) = Canteiro; 2ª em diante = Asfalto
        when(servicoTerreno.gerarTerreno()).thenReturn(new Canteiro(), new Asfalto());
        danoSempre(100);

        batalha(cpu("Ash", pokemonJogador), oponente, "", false).iniciarBatalha();

        verify(registro).registrarEfeitoTerreno(isA(Asfalto.class));
    }

    @Test
    @DisplayName("Golpe que muda o terreno troca o terreno da batalha")
    void testGolpeMudaOTerreno() {
        Ataque diaEnsolarado = new Ataque("Dia Ensolarado", Tipo.FOGO, 0, EfeitoStatus.NENHUM, 0.0, new Asfalto());
        Pokemon fogo = pokemon("Fogo", Tipo.FOGO, 100, 90, diaEnsolarado);
        danoSempre(1000); // o oponente derruba o jogador depois

        batalha(cpu("Ash", fogo), oponente, "", false).iniciarBatalha();

        assertTrue(impresso().contains("O terreno mudou para Asfalto"));
        verify(registro, atLeastOnce()).registrarEfeitoTerreno(isA(Asfalto.class));
    }

    @Test
    @DisplayName("Golpe que ativa o terreno que já está ativo falha")
    void testGolpeQueAtivaOMesmoTerrenoFalha() {
        when(servicoTerreno.gerarTerreno()).thenReturn(new Asfalto());
        Ataque diaEnsolarado = new Ataque("Dia Ensolarado", Tipo.FOGO, 0, EfeitoStatus.NENHUM, 0.0, new Asfalto());
        Pokemon fogo = pokemon("Fogo", Tipo.FOGO, 100, 90, diaEnsolarado);
        danoSempre(1000);

        batalha(cpu("Ash", fogo), oponente, "", false).iniciarBatalha();

        assertTrue(impresso().contains("Mas falhou."));
    }

    // ------------------------------------ buffs e debuffs ------------------------------------

    @Test
    @DisplayName("Golpe de debuff reduz o ataque do alvo em 1 estágio")
    void testDebuffReduzAtaqueDoAlvo() {
        Ataque rosnar = new Ataque("Rosnar", Tipo.NORMAL, 0, EfeitoStatus.NENHUM, 0.0, null,
                new Buff(0, 0, 0), new Buff(-1, 0, 0), 100);
        Pokemon usuario = pokemon("Usuario", Tipo.NORMAL, 100, 90, rosnar);
        danoSempre(1000);

        batalha(cpu("Ash", usuario), oponente, "", false).iniciarBatalha();

        assertEquals(-1, pokemonOponente.getEstagioAtk());
        assertTrue(impresso().contains("diminuiu!"));
    }

    @Test
    @DisplayName("Golpe de buff aumenta defesa e velocidade do usuário em 1 estágio")
    void testBuffAumentaDefesaEVelocidadeDoUsuario() {
        Ataque fortalecer = new Ataque("Fortalecer", Tipo.NORMAL, 0, EfeitoStatus.NENHUM, 0.0, null,
                new Buff(0, 1, 1), new Buff(0, 0, 0), 100);
        Pokemon usuario = pokemon("Usuario", Tipo.NORMAL, 100, 90, fortalecer);
        danoSempre(1000); // o oponente derruba o jogador no fim do turno 1

        batalha(cpu("Ash", usuario), oponente, "", false).iniciarBatalha();

        assertEquals(1, usuario.getEstagioDef());
        assertEquals(1, usuario.getEstagioSpd());
        assertTrue(impresso().contains("aumentou!"));
    }

    @Test
    @DisplayName("Buff/debuff no limite do estágio (+6 / -6) não altera nada e avisa")
    void testBuffNoLimiteDoEstagioNaoAltera() {
        Ataque golpe = new Ataque("Golpe de limite", Tipo.NORMAL, 0, EfeitoStatus.NENHUM, 0.0, null,
                new Buff(1, 0, 0), new Buff(-1, 0, 0), 100);
        Pokemon usuario = pokemon("Usuario", Tipo.NORMAL, 100, 90, golpe);
        usuario.alterarAtk(6);                 // já no máximo
        pokemonOponente.alterarAtk(-6);        // já no mínimo
        danoSempre(1000);

        batalha(cpu("Ash", usuario), oponente, "", false).iniciarBatalha();

        assertEquals(6, usuario.getEstagioAtk());
        assertEquals(-6, pokemonOponente.getEstagioAtk());
        assertTrue(impresso().contains("não consegue aumentar mais!"));
        assertTrue(impresso().contains("não consegue diminuir mais!"));
    }

    // ------------------------------------ efetividade, crítico e status ------------------------------------

    @Test
    @DisplayName("Mensagens de golpe super efetivo e de acerto crítico")
    void testSuperEfetivoECritico() {
        Ataque brasa = new Ataque("Brasa", Tipo.FOGO, 40, EfeitoStatus.NENHUM, 0.0, 100);
        Pokemon fogo = pokemon("Fogo", Tipo.FOGO, 100, 90, brasa);
        Pokemon planta = pokemon("Planta", Tipo.PLANTA, 50, 10, golpeOponente);
        danoSempre(100);
        when(calcDano.foiCritico()).thenReturn(true);

        batalha(cpu("Ash", fogo), cpu("Gary", planta), "", false).iniciarBatalha();

        assertTrue(impresso().contains("É super efetivo!"));
        assertTrue(impresso().contains("Foi um acerto crítico!"));
    }

    @Test
    @DisplayName("Mensagem de golpe pouco efetivo")
    void testPoucoEfetivo() {
        Ataque brasa = new Ataque("Brasa", Tipo.FOGO, 40, EfeitoStatus.NENHUM, 0.0, 100);
        Pokemon fogo = pokemon("Fogo", Tipo.FOGO, 100, 90, brasa);
        Pokemon agua = pokemon("Agua", Tipo.AGUA, 50, 10, golpeOponente);
        danoSempre(100);

        batalha(cpu("Ash", fogo), cpu("Gary", agua), "", false).iniciarBatalha();

        assertTrue(impresso().contains("Não é muito efetivo..."));
    }

    @Test
    @DisplayName("Golpe de status sem dano falha se o alvo já tem um status")
    void testStatusEmAlvoQueJaTemStatusFalha() {
        Ataque ondaTrovao = new Ataque("Onda Trovão", Tipo.ELETRICO, 0, EfeitoStatus.PARALISADO, 1.0, 100);
        Pokemon eletrico = pokemon("Eletrico", Tipo.ELETRICO, 100, 90, ondaTrovao);
        pokemonOponente.setStatus(EfeitoStatus.QUEIMADO);
        danoSempre(1000);

        batalha(cpu("Ash", eletrico), oponente, "", false).iniciarBatalha();

        assertTrue(impresso().contains("Mas falhou."));
        verify(registro, never()).registrarEfeitoStatus(any(EfeitoStatus.class));
    }

    @Test
    @DisplayName("Golpe com 100% de chance envenena o alvo e registra o efeito")
    void testGolpeEnvenenaOAlvo() {
        Ataque peconha = new Ataque("Peçonha", Tipo.VENENO, 0, EfeitoStatus.ENVENENADO, 1.0, 100);
        Pokemon venenoso = pokemon("Venenoso", Tipo.VENENO, 100, 90, peconha);
        danoSempre(1000);

        batalha(cpu("Ash", venenoso), oponente, "", false).iniciarBatalha();

        verify(registro).registrarEfeitoStatus(EfeitoStatus.ENVENENADO);
        assertEquals(EfeitoStatus.ENVENENADO, pokemonOponente.getStatus());
        assertTrue(impresso().contains("foi envenenado!"));
    }
}
