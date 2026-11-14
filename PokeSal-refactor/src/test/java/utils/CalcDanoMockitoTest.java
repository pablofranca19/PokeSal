package utils;

import models.Pokemon;
import models.enums.EfeitoStatus;
import models.enums.Tipo;
import models.terrains.Canteiro;
import models.terrains.Terreno;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.GeradorDoubleAleatorio;
import services.GeradorInteiroAleatorio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * CalcDano isolado dos geradores aleatórios.
 *
 * @InjectMocks cria o CalcDano real passando pelo construtor os dois @Mock (injeção por construtor).
 * Mockados: GeradorDoubleAleatorio (crítico) e GeradorInteiroAleatorio (precisão).
 * Não mockados: Pokemon, Ataque e Terreno (dados puros).
 */
@ExtendWith(MockitoExtension.class)
class CalcDanoMockitoTest {

    @Mock
    private GeradorDoubleAleatorio geradorDouble;
    @Mock
    private GeradorInteiroAleatorio geradorInt;
    @InjectMocks
    private CalcDano calcDano;

    private final Terreno terreno = new Canteiro(); // modificador 1.0 para qualquer tipo
    // atacante NORMAL usando golpe de FOGO: sem STAB; alvo NORMAL: efetividade neutra
    private final Pokemon atacante = new Pokemon("Atacante", Tipo.NORMAL, 100, 100, 50, 50);
    private final Pokemon alvo = new Pokemon("Alvo", Tipo.NORMAL, 100, 50, 50, 50);

    private Ataque golpe(int precisao) {
        return new Ataque("Golpe", Tipo.FOGO, 100, EfeitoStatus.NENHUM, 0.0, precisao);
    }

    @Test
    @DisplayName("Acerto crítico: gerador dentro da faixa (< 6,25%) multiplica o dano por 1.5")
    void testAcertoCriticoComGeradorMockado() {
        when(geradorInt.gerarInt()).thenReturn(50); // 50 <= precisão 100: acerta
        // retornos em sequência: 1ª chamada dentro da faixa crítica, 2ª fora dela
        when(geradorDouble.gerarDouble()).thenReturn(0.01, 0.50);

        int danoCritico = calcDano.calcularDano(golpe(100), atacante, alvo, terreno);
        assertTrue(calcDano.foiCritico(), "0.01 < 0.0625 deveria ser crítico");

        int danoNormal = calcDano.calcularDano(golpe(100), atacante, alvo, terreno);
        assertFalse(calcDano.foiCritico(), "0.50 está fora da faixa crítica");

        assertEquals(1.5, (double) danoCritico / danoNormal, 0.05);
        verify(geradorDouble, times(2)).gerarDouble();
    }

    @Test
    @DisplayName("Limite da faixa crítica: 0.0624 é crítico, 0.0625 já não é")
    void testLimiteDaFaixaCritica() {
        when(geradorInt.gerarInt()).thenReturn(0);
        when(geradorDouble.gerarDouble()).thenReturn(0.0624, 0.0625);

        calcDano.calcularDano(golpe(100), atacante, alvo, terreno);
        assertTrue(calcDano.foiCritico());

        calcDano.calcularDano(golpe(100), atacante, alvo, terreno);
        assertFalse(calcDano.foiCritico());
    }

    @Test
    @DisplayName("Golpe erra quando o número sorteado passa da precisão: dano 0 e HP do alvo inalterado")
    void testGolpeErraQuandoPrecisaoFalha() {
        when(geradorInt.gerarInt()).thenReturn(95); // 95 > precisão 80: erra
        int hpAntes = alvo.getHpAtual();

        int dano = calcDano.calcularDano(golpe(80), atacante, alvo, terreno);

        assertEquals(0, dano);
        assertEquals(hpAntes, alvo.getHpAtual());
        assertFalse(calcDano.foiCritico());
        // errou -> nem chega a sortear crítico
        verify(geradorDouble, never()).gerarDouble();
    }

    @Test
    @DisplayName("Limite da precisão: sorteio igual à precisão ainda acerta")
    void testSorteioIgualAPrecisaoAindaAcerta() {
        when(geradorInt.gerarInt()).thenReturn(80);
        when(geradorDouble.gerarDouble()).thenReturn(0.5);

        int dano = calcDano.calcularDano(golpe(80), atacante, alvo, terreno);

        assertTrue(dano > 0);
    }
}
