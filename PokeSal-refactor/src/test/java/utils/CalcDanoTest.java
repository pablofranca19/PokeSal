package utils;

import models.Inicial;
import models.enums.EfeitoStatus;
import models.enums.Terreno;
import models.enums.Tipo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import utils.Ataque;
import utils.CalcDano;

import static org.junit.jupiter.api.Assertions.*;

class CalcDanoTest {

    private static final double DELTA = 0.0001;

    @Test
    @DisplayName("fogo é mais efetivo contra planta e pouco efetivo contra agua")
    void testVantagemElemental() {
        Ataque ataqueFogo = new Ataque("Brasa", Tipo.FOGO, 40, EfeitoStatus.NENHUM, 0.0);
        Ataque ataqueAgua = new Ataque("Bolha", Tipo.AGUA, 40, EfeitoStatus.NENHUM, 0.0);
        Ataque ataquePlanta = new Ataque("Vinha", Tipo.PLANTA, 40, EfeitoStatus.NENHUM, 0.0);
        Ataque ataqueNormal = new Ataque("Investida", Tipo.NORMAL, 40, EfeitoStatus.NENHUM, 0.0);

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
        Inicial alvo = new Inicial("Alvo", Tipo.NORMAL, 50, 50, 50, 50);

        alvo.receberDano(9999);

        assertEquals(0, alvo.getHpAtual(), "HP deveria ser travado em 0, nunca negativo");
    }

    @Test
    @DisplayName("Cura não deve ultrapassar o HP máximo (boundary)")
    void testCalculoDanoBoundaryValues_curaNaoUltrapassaHpMax() {
        Inicial alvo = new Inicial("Alvo", Tipo.NORMAL, 50, 50, 50, 50);
        alvo.setHpAtual(1);

        alvo.curar(9999);

        assertEquals(alvo.getHpMax(), alvo.getHpAtual(), "HP não deveria ultrapassar o HP máximo");
    }

    @Test
    @DisplayName("Estágios de ATK/DEF/SPD ficam travados entre -6 e +6")
    void testCalculoDanoBoundaryValues_estagioLimitado() {
        Inicial pokesal = new Inicial("Teste", Tipo.NORMAL, 50, 50, 50, 50);

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
    @DisplayName("bug encontraodo mas não corrigido , DEF efetivo = 0 causa divisão por zero e dano absurdo")
    void testCalculoDanoBoundaryValues_defZeroNaoQuebraCalculo() {
        Inicial atacante = new Inicial("Atacante", Tipo.NORMAL, 100, 20, 50, 50);
        Inicial alvo = new Inicial("Alvo", Tipo.NORMAL, 100, 10, 1, 50);
        alvo.alterarDef(-6);

        assertEquals(0, alvo.getDef());

        Ataque golpeFraco = new Ataque("Golpe Fraco", Tipo.NORMAL, 20, EfeitoStatus.NENHUM, 0.0);
        int dano = CalcDano.calcularDano(golpeFraco, atacante, alvo, Terreno.ASFALTO);

        assertEquals(Integer.MAX_VALUE, dano,
                "outro bug: DEF efetivo 0 provoca divisão por zero -> Infinity -> cast vira Integer.MAX_VALUE (dano absurdo)");
    }
}
