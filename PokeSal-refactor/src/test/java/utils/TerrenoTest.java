package utils;

import models.Inicial;
import models.enums.Terreno;
import models.enums.Tipo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class TerrenoTest {

    private static final double DELTA = 0.0001;

    @Test
    @DisplayName("efeito do terreno alfasto bonifica fogo e POÇA bonifica Água; sem bônus fora dessas combinações")
    void testEfeitoTerrenoEstacionamentoUCSal_modificadorDeDano() {
        assertEquals(1.15, Terreno.ASFALTO.getMod(Tipo.FOGO), DELTA);
        assertEquals(1.0, Terreno.ASFALTO.getMod(Tipo.AGUA), DELTA);

        assertEquals(1.10, Terreno.POCA.getMod(Tipo.AGUA), DELTA);
        assertEquals(1.0, Terreno.POCA.getMod(Tipo.FOGO), DELTA);

        assertEquals(1.0, Terreno.CANTEIRO.getMod(Tipo.PLANTA), DELTA);
    }

    @Test
    @DisplayName("bug encontrado só que nao corrigido: canteiro ta curando o dobro do valor pretendido (10% em vez de 5%)")
    void testEfeitoTerrenoEstacionamentoUCSal_curaCanteiroSoAfetaTipoPlanta() {
        Inicial plantaFerida = new Inicial("BulbaSal", Tipo.PLANTA, 100, 49, 49, 45);
        plantaFerida.receberDano(80);

        Terreno.CANTEIRO.curaTerreno(plantaFerida);

        assertEquals(30, plantaFerida.getHpAtual(),
                "BUG: CANTEIRO está curando 10% (o dobro do 5% esperado) devido à soma duplicada em curaTerreno()");
    }

    @Test
    @DisplayName("efeito do terreno canteiro não cura PokéSal de outros tipos")
    void testEfeitoTerrenoEstacionamentoUCSal_curaCanteiroNaoAfetaOutrosTipos() {
        Inicial aguaFerida = new Inicial("SquirtSal", Tipo.AGUA, 100, 48, 65, 43);
        aguaFerida.receberDano(80);

        Terreno.CANTEIRO.curaTerreno(aguaFerida);

        assertEquals(20, aguaFerida.getHpAtual(), "CANTEIRO não deveria curar PokéSal que não sejam do tipo Planta");
    }

    @Test
    void testEfeitoTerrenoEstacionamentoUCSal_curaNaoUltrapassaHpMax() {
        Inicial plantaQuaseCheia = new Inicial("ChikoSal", Tipo.PLANTA, 100, 49, 65, 45);
        plantaQuaseCheia.receberDano(2);

        Terreno.CANTEIRO.curaTerreno(plantaQuaseCheia);

        assertEquals(100, plantaQuaseCheia.getHpAtual(), "Cura do terreno não deveria ultrapassar o HP máximo");
    }
}
