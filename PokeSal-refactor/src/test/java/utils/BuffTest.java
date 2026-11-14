package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuffTest {
    @Test
    @DisplayName("Buff guarda as alterações de ataque, defesa e velocidade")
    void testBuffGuardaOsTresValores() {
        Buff buff = new Buff(1, -2, 3);

        assertEquals(1, buff.getAtk());
        assertEquals(-2, buff.getDef());
        assertEquals(3, buff.getSpd());
    }
}
