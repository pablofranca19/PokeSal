package application;

import models.Inicial;
import models.enums.EfeitoStatus;
import models.enums.Tipo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class BatalhaOrdemTest {

    private boolean jogadorAgePrimeiro(Inicial jogador, Inicial oponente) {
        return jogador.getSpd() >= oponente.getSpd();
    }

    @Test
    @DisplayName("orrdem de ataque por velocidade, quem tem maior SPD age primeiro")
    void testOrdemDeAtaquePorVelocidade() {
        Inicial rapido = new Inicial("Rapido", Tipo.NORMAL, 100, 50, 50, 100);
        Inicial lento = new Inicial("Lento", Tipo.NORMAL, 100, 50, 50, 10);

        assertTrue(jogadorAgePrimeiro(rapido, lento),
                "PokéSal com maior SPD deveria agir primeiro");
        assertFalse(jogadorAgePrimeiro(lento, rapido),
                "PokéSal com menor SPD não deveria agir primeiro");
    }

    @Test
    @DisplayName("Ordem de ataque por velocidade em caso de empate, o jogador age primeiro")
    void testOrdemDeAtaquePorVelocidade_empate() {
        Inicial jogador = new Inicial("Jogador", Tipo.NORMAL, 100, 50, 50, 50);
        Inicial oponente = new Inicial("Oponente", Tipo.NORMAL, 100, 50, 50, 50);

        assertTrue(jogadorAgePrimeiro(jogador, oponente),
                "no caso de empate de spd, a regra do jogo é o jogador agir primeiro");
    }

    @Test
    @DisplayName("Ordem de ataque por velocidafe paralisia reduz a SPD efetiva pela metade e pode inverter a ordem")
    void testOrdemDeAtaquePorVelocidade_paralisiaAlteraOrdem() {
        Inicial jogadorParalisado = new Inicial("Jogador", Tipo.NORMAL, 100, 50, 50, 100);
        Inicial oponente = new Inicial("Oponente", Tipo.NORMAL, 100, 50, 50, 60);

        assertTrue(jogadorAgePrimeiro(jogadorParalisado, oponente));

        jogadorParalisado.setStatus(EfeitoStatus.PARALISADO);
        assertFalse(jogadorAgePrimeiro(jogadorParalisado, oponente),
                "Paralisia deveria reduzir a SPD efetiva o suficiente para inverter a ordem de ataque");
    }
}
