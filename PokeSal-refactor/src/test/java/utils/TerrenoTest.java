package utils;

import models.Pokemon;
import models.enums.Tipo;
import models.terrains.Asfalto;
import models.terrains.Canteiro;
import models.terrains.Poca;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TerrenoTest {

    private static final double DELTA = 0.0001;

    @Test
    @DisplayName("efeito do terreno asfalto bonifica fogo e POÇA bonifica Água; sem bônus fora dessas combinações")
    void testEfeitoTerrenoEstacionamentoUCSal_modificadorDeDano() {
        assertEquals(1.15, new Asfalto().getMod(Tipo.FOGO), DELTA);
        assertEquals(1.0, new Asfalto().getMod(Tipo.AGUA), DELTA);

        assertEquals(1.10, new Poca().getMod(Tipo.AGUA), DELTA);
        assertEquals(1.0, new Poca().getMod(Tipo.FOGO), DELTA);

        assertEquals(1.0, new Canteiro().getMod(Tipo.PLANTA), DELTA);
    }

    @Test
    @DisplayName("canteiro cura 5% do HP máximo de PokéSal tipo Planta (bug do 10% corrigido na refatoração)")
    void testEfeitoTerrenoEstacionamentoUCSal_curaCanteiroSoAfetaTipoPlanta() {
        Pokemon plantaFerida = new Pokemon("BulbaSal", Tipo.PLANTA, 100, 49, 49, 45);
        plantaFerida.receberDano(80); // HP 20

        new Canteiro().curaTerreno(plantaFerida);

        assertEquals(25, plantaFerida.getHpAtual(), "CANTEIRO deveria curar 5% de 100 = 5 HP");
    }

    @Test
    @DisplayName("efeito do terreno canteiro não cura PokéSal de outros tipos")
    void testEfeitoTerrenoEstacionamentoUCSal_curaCanteiroNaoAfetaOutrosTipos() {
        Pokemon aguaFerida = new Pokemon("SquirtSal", Tipo.AGUA, 100, 48, 65, 43);
        aguaFerida.receberDano(80);

        new Canteiro().curaTerreno(aguaFerida);

        assertEquals(20, aguaFerida.getHpAtual(), "CANTEIRO não deveria curar PokéSal que não sejam do tipo Planta");
    }

    @Test
    @DisplayName("cura do canteiro não ultrapassa o HP máximo")
    void testEfeitoTerrenoEstacionamentoUCSal_curaNaoUltrapassaHpMax() {
        Pokemon plantaQuaseCheia = new Pokemon("ChikoSal", Tipo.PLANTA, 100, 49, 65, 45);
        plantaQuaseCheia.receberDano(2); // HP 98

        new Canteiro().curaTerreno(plantaQuaseCheia);

        assertEquals(100, plantaQuaseCheia.getHpAtual(), "Cura do terreno não deveria ultrapassar o HP máximo");
    }
}
