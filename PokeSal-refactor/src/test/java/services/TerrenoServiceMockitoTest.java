package services;

import models.enums.Clima;
import models.terrains.Asfalto;
import models.terrains.Canteiro;
import models.terrains.Poca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TerrenoServiceMockitoTest {
    private static final Instant MEIO_DIA = Instant.parse("2026-11-14T12:00:00Z");
    private static final Instant MEIA_NOITE = Instant.parse("2026-11-14T00:00:00Z");

    @Mock
    private Clock clock;

    @BeforeEach
    void setUp() {
        lenient().when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    }

    @Test
    @DisplayName("Asfalto Quente só é ativado durante o dia (clima SOL)")
    void testAsfaltoQuenteApenasDuranteODia() {
        when(clock.instant()).thenReturn(MEIO_DIA, MEIA_NOITE);
        TerrenoService servico = new TerrenoService(Clima.SOL, clock);

        assertInstanceOf(Asfalto.class, servico.gerarTerreno(), "de dia com sol deveria ser Asfalto");
        assertInstanceOf(Canteiro.class, servico.gerarTerreno(), "de noite o Asfalto não deveria ser ativado");

        verify(clock, times(2)).instant();
    }

    @Test
    @DisplayName("Limites do dia: 06:00 já é dia; 18:00 já é noite")
    void testLimitesDoDia() {
        when(clock.instant()).thenReturn(
                Instant.parse("2026-11-14T06:00:00Z"),
                Instant.parse("2026-11-14T17:59:00Z"),
                Instant.parse("2026-11-14T18:00:00Z"));
        TerrenoService servico = new TerrenoService(Clima.SOL, clock);

        assertInstanceOf(Asfalto.class, servico.gerarTerreno());
        assertInstanceOf(Asfalto.class, servico.gerarTerreno());
        assertInstanceOf(Canteiro.class, servico.gerarTerreno());
    }

    @Test
    @DisplayName("Poça só aparece à noite com chuva")
    void testPocaApenasDeNoiteComChuva() {
        when(clock.instant()).thenReturn(MEIA_NOITE, MEIO_DIA);
        TerrenoService servico = new TerrenoService(Clima.CHUVA, clock);

        assertInstanceOf(Poca.class, servico.gerarTerreno());
        assertInstanceOf(Canteiro.class, servico.gerarTerreno());
    }

    @Test
    @DisplayName("Horário atual vem do relógio injetado")
    void testHorarioAtualVemDoRelogio() {
        when(clock.instant()).thenReturn(MEIO_DIA);

        assertEquals(LocalTime.NOON, new TerrenoService(Clima.NUBLADO, clock).getHorarioAtual());
    }
}
