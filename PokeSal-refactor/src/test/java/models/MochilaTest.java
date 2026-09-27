package models;

import models.Inicial;
import models.enums.Tipo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import utils.Item;
import utils.Mochila;
import utils.Pocao;

import static org.junit.jupiter.api.Assertions.*;

class MochilaTest {

    @Test
    @DisplayName("lança IllegalStateException ao exceder o limite de 2 itens por batalha")
    void testUsoLimiteDeItensExcedido() {
        Mochila mochila = new Mochila();
        Item pocao1 = new Pocao("Poção");
        Item pocao2 = new Pocao("Poção");
        Item pocao3 = new Pocao("Poção");
        mochila.adicionarItem(pocao1);
        mochila.adicionarItem(pocao2);
        mochila.adicionarItem(pocao3);

        Inicial pokesal = new Inicial("Teste", Tipo.NORMAL, 100, 50, 50, 50);
        pokesal.receberDano(50);

        assertDoesNotThrow(() -> mochila.usarItem(pocao1, pokesal));
        assertDoesNotThrow(() -> mochila.usarItem(pocao2, pokesal));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> mochila.usarItem(pocao3, pokesal));
        assertTrue(ex.getMessage().contains("2 itens"));
    }

    @Test
    @DisplayName("resetarUso() permite usar itens novamente na próxima batalha")
    void testResetarUsoPermiteNovosUsos() {
        Mochila mochila = new Mochila();
        Item pocao1 = new Pocao("Poção");
        Item pocao2 = new Pocao("Poção");
        mochila.adicionarItem(pocao1);
        mochila.adicionarItem(pocao2);

        Inicial pokesal = new Inicial("Teste", Tipo.NORMAL, 100, 50, 50, 50);
        pokesal.receberDano(50);

        mochila.usarItem(pocao1, pokesal);
        mochila.usarItem(pocao2, pokesal);
        assertThrows(IllegalStateException.class, () -> mochila.usarItem(pocao1, pokesal));

        mochila.resetarUso();

        assertDoesNotThrow(() -> mochila.usarItem(pocao1, pokesal));
    }
}
