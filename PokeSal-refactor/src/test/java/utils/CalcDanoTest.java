package utils;

import models.Pokemon;
import models.enums.EfeitoStatus;
import models.enums.Tipo;
import models.terrains.Asfalto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.GeradorDoubleAleatorio;
import services.GeradorInteiroAleatorio;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcDanoTest {

    private static final double DELTA = 0.0001;

    /** Geradores determinísticos simples (sem Mockito): sempre acerta e nunca dá crítico. */
    private static CalcDano calcDanoDeterministico() {
        GeradorDoubleAleatorio semCritico = () -> 0.5;
        GeradorInteiroAleatorio sempreAcerta = new GeradorInteiroAleatorio() {
            @Override public int gerarInt() { return 0; }
            @Override public int gerarInt(int limite) { return 0; }
        };
        return new CalcDano(semCritico, sempreAcerta);
    }

    @Test
    @DisplayName("fogo é mais efetivo contra planta e pouco efetivo contra agua")
    void testVantagemElemental() {
        Ataque ataqueFogo = new Ataque("Brasa", Tipo.FOGO, 40, EfeitoStatus.NENHUM, 0.0, 100);
        Ataque ataqueAgua = new Ataque("Bolha", Tipo.AGUA, 40, EfeitoStatus.NENHUM, 0.0, 100);
        Ataque ataquePlanta = new Ataque("Vinha", Tipo.PLANTA, 40, EfeitoStatus.NENHUM, 0.0, 100);
        Ataque ataqueNormal = new Ataque("Investida", Tipo.NORMAL, 40, EfeitoStatus.NENHUM, 0.0, 100);

        assertEquals(CalcDano.SUPER_EFETIVO, CalcDano.calcEfet(ataqueFogo, Tipo.PLANTA), DELTA);
        assertEquals(CalcDano.POUCO_EFETIVO, CalcDano.calcEfet(ataqueFogo, Tipo.AGUA), DELTA);
        assertEquals(CalcDano.NEUTRO, CalcDano.calcEfet(ataqueFogo, Tipo.NORMAL), DELTA);

        assertEquals(CalcDano.SUPER_EFETIVO, CalcDano.calcEfet(ataqueAgua, Tipo.FOGO), DELTA);
        assertEquals(CalcDano.POUCO_EFETIVO, CalcDano.calcEfet(ataqueAgua, Tipo.PLANTA), DELTA);

        assertEquals(CalcDano.SUPER_EFETIVO, CalcDano.calcEfet(ataquePlanta, Tipo.AGUA), DELTA);
        assertEquals(CalcDano.POUCO_EFETIVO, CalcDano.calcEfet(ataquePlanta, Tipo.FOGO), DELTA);

        assertEquals(CalcDano.NEUTRO, CalcDano.calcEfet(ataqueNormal, Tipo.FOGO), DELTA);
        assertEquals(CalcDano.NEUTRO, CalcDano.calcEfet(ataqueNormal, Tipo.AGUA), DELTA);
        assertEquals(CalcDano.NEUTRO, CalcDano.calcEfet(ataqueNormal, Tipo.PLANTA), DELTA);
    }

    @Test
    @DisplayName("HP não deve ficar negativo ao receber dano maior que o HP atual")
    void testCalculoDanoBoundaryValues_hpNaoFicaNegativo() {
        Pokemon alvo = new Pokemon("Alvo", Tipo.NORMAL, 50, 50, 50, 50);

        alvo.receberDano(9999);

        assertEquals(0, alvo.getHpAtual(), "HP deveria ser travado em 0, nunca negativo");
    }

    @Test
    @DisplayName("Cura não deve ultrapassar o HP máximo (boundary)")
    void testCalculoDanoBoundaryValues_curaNaoUltrapassaHpMax() {
        Pokemon alvo = new Pokemon("Alvo", Tipo.NORMAL, 50, 50, 50, 50);
        alvo.setHpAtual(1);

        alvo.curar(9999);

        assertEquals(alvo.getHpMax(), alvo.getHpAtual(), "HP não deveria ultrapassar o HP máximo");
    }

    @Test
    @DisplayName("Estágios de ATK/DEF/SPD ficam travados entre -6 e +6")
    void testCalculoDanoBoundaryValues_estagioLimitado() {
        Pokemon pokesal = new Pokemon("Teste", Tipo.NORMAL, 50, 50, 50, 50);

        for (int i = 0; i < 10; i++) {
            pokesal.alterarAtk(+1);
        }
        assertEquals(6, pokesal.getEstagioAtk(), "Estágio de ATK não deveria ultrapassar +6");

        for (int i = 0; i < 20; i++) {
            pokesal.alterarAtk(-1);
        }
        assertEquals(-6, pokesal.getEstagioAtk(), "Estágio de ATK não deveria ultrapassar -6");
    }

    @Test
    @DisplayName("bug encontrado mas não corrigido: DEF efetivo = 0 causa divisão por zero e dano absurdo")
    void testCalculoDanoBoundaryValues_defZeroNaoQuebraCalculo() {
        Pokemon atacante = new Pokemon("Atacante", Tipo.NORMAL, 100, 20, 50, 50);
        Pokemon alvo = new Pokemon("Alvo", Tipo.NORMAL, 100, 10, 1, 50);
        alvo.alterarDef(-6);

        assertEquals(0, alvo.getDef());

        Ataque golpeFraco = new Ataque("Golpe Fraco", Tipo.NORMAL, 20, EfeitoStatus.NENHUM, 0.0, 100);
        int dano = calcDanoDeterministico().calcularDano(golpeFraco, atacante, alvo, new Asfalto());

        assertEquals(Integer.MAX_VALUE, dano,
                "BUG: DEF efetivo 0 provoca divisão por zero -> Infinity -> cast vira Integer.MAX_VALUE (dano absurdo)");
    }
}
