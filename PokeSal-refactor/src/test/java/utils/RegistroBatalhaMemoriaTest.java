package utils;

import models.enums.EfeitoStatus;
import models.enums.Tipo;
import models.terrains.Asfalto;
import models.terrains.Terreno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RegistroBatalhaMemoriaTest {
    @Mock
    private Item item;

    private RegistroBatalhaMemoria registro;
    private final Ataque ataque = new Ataque("Brasa", Tipo.FOGO, 40, EfeitoStatus.QUEIMADO, 0.1, 100);
    private final Terreno terreno = new Asfalto();

    @BeforeEach
    void setUp() {
        registro = new RegistroBatalhaMemoria();
    }

    @Test
    @DisplayName("Guarda ataque, dano, efeito de status, terreno e itens usados")
    void testGuardaTodosOsEventos() {
        registro.registrarAtaque(ataque);
        registro.registrarDano(12);
        registro.registrarEfeitoStatus(EfeitoStatus.QUEIMADO);
        registro.registrarEfeitoTerreno(terreno);
        registro.registrarItem(item);

        assertSame(ataque, registro.getAtaqueRealizado());
        assertEquals(12, registro.getDano());
        assertEquals(EfeitoStatus.QUEIMADO, registro.getEfeitoStatus());
        assertSame(terreno, registro.getEfeitoDeTerreno());
        assertEquals(1, registro.getItensUsados().size());
        assertSame(item, registro.getItensUsados().getFirst());
    }

    @Test
    @DisplayName("Um novo ataque zera dano, status, terreno e itens do turno anterior")
    void testNovoAtaqueZeraOsDemaisEventos() {
        registro.registrarAtaque(ataque);
        registro.registrarDano(12);
        registro.registrarEfeitoStatus(EfeitoStatus.QUEIMADO);
        registro.registrarEfeitoTerreno(terreno);
        registro.registrarItem(item);

        Ataque outro = new Ataque("Bolha", Tipo.AGUA, 20, EfeitoStatus.NENHUM, 0.0, 100);
        registro.registrarAtaque(outro);

        assertSame(outro, registro.getAtaqueRealizado());
        assertEquals(0, registro.getDano());
        assertEquals(EfeitoStatus.NENHUM, registro.getEfeitoStatus());
        assertNull(registro.getEfeitoDeTerreno());
        assertTrue(registro.getItensUsados().isEmpty());
    }

    @Test
    @DisplayName("A lista de itens devolvida é uma cópia imutável")
    void testItensUsadosEImutavel() {
        registro.registrarItem(item);

        assertThrows(UnsupportedOperationException.class, () -> registro.getItensUsados().add(item));
    }
}
