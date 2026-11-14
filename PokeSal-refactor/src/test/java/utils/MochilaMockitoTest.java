package utils;

import models.Pokemon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/** Mochila com Item e Pokemon mockados (a Mochila só repassa a chamada). */
@ExtendWith(MockitoExtension.class)
class MochilaMockitoTest {

    @Mock
    private Item item;
    @Mock
    private Item itemDeFora;
    @Mock
    private Pokemon pokemon;

    @Test
    @DisplayName("usarItem repassa o uso ao item da mochila")
    void testUsarItemChamaOItem() {
        Mochila mochila = new Mochila();
        mochila.adicionarItem(item);

        mochila.usarItem(item, pokemon);

        verify(item, times(1)).usar(pokemon);
    }

    @Test
    @DisplayName("Item que não está na mochila não é usado e não gasta o limite de usos")
    void testItemForaDaMochilaNaoEUsado() {
        Mochila mochila = new Mochila();
        mochila.adicionarItem(item);

        // 3 tentativas com item de fora: se contassem como uso, a 3ª lançaria IllegalStateException
        assertDoesNotThrow(() -> {
            mochila.usarItem(itemDeFora, pokemon);
            mochila.usarItem(itemDeFora, pokemon);
            mochila.usarItem(itemDeFora, pokemon);
        });
        verify(itemDeFora, never()).usar(any(Pokemon.class));
    }

    @Test
    @DisplayName("adicionarItem ignora null")
    void testAdicionarNullEIgnorado() {
        Mochila mochila = new Mochila();

        mochila.adicionarItem(null);

        assertTrue(mochila.getItens().isEmpty());
    }

    @Test
    @DisplayName("removerItem tira o item da mochila e ele deixa de ser usado")
    void testRemoverItem() {
        Mochila mochila = new Mochila();
        mochila.adicionarItem(item);

        mochila.removerItem(item);
        mochila.usarItem(item, pokemon);

        assertTrue(mochila.getItens().isEmpty());
        verify(item, never()).usar(any(Pokemon.class));
    }

    @Test
    @DisplayName("A lista de itens devolvida não pode ser alterada de fora")
    void testListaDeItensEImutavel() {
        Mochila mochila = new Mochila();

        assertThrows(UnsupportedOperationException.class, () -> mochila.getItens().add(item));
    }
}
