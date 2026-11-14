package models;

import models.enums.EfeitoStatus;
import models.enums.Tipo;
import models.pokesal.CharSal;
import models.pokesal.SquirtSal;
import models.terrains.Asfalto;
import models.terrains.Canteiro;
import models.terrains.Poca;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.GeradorDoubleAleatorio;
import services.GeradorInteiroAleatorio;
import services.GeradorService;
import utils.Ataque;
import utils.CalcDano;

import static org.junit.jupiter.api.Assertions.*;

/** Requisitos autorais das Fases 01/02, adaptados à arquitetura da Fase 03 (sem Mockito). */
class RequisitosAutoraisTest {

    private static GeradorInteiroAleatorio sempreAcerta() {
        return new GeradorInteiroAleatorio() {
            @Override public int gerarInt() { return 0; }
            @Override public int gerarInt(int limite) { return 0; }
        };
    }

    private Ataque buscar(Pokemon p, String nome) {
        return p.getGolpes().stream().filter(a -> a.nome().equals(nome)).findFirst().orElseThrow();
    }

    @Test
    @DisplayName("Requisito Autoral 1: acerto crítico ocorre com probabilidade próxima de 1/16 (6,25%)")
    void testRequisitoAutoral1_DanoCriticoProbabilidade() {
        final int TENTATIVAS = 200_000;
        GeradorService gerador = new GeradorService();
        CalcDano calcDano = new CalcDano(gerador, gerador);
        Pokemon atacante = new Pokemon("Atacante", Tipo.NORMAL, 100, 500, 50, 50);
        Pokemon alvo = new Pokemon("Alvo", Tipo.NORMAL, 100, 50, 50, 50);
        Ataque golpe = new Ataque("Golpe", Tipo.NORMAL, 150, EfeitoStatus.NENHUM, 0.0, 100);

        int acertosCriticos = 0;
        for (int i = 0; i < TENTATIVAS; i++) {
            calcDano.calcularDano(golpe, atacante, alvo, new Canteiro());
            if (calcDano.foiCritico()) {
                acertosCriticos++;
            }
        }

        double taxaObservada = (double) acertosCriticos / TENTATIVAS;
        assertTrue(taxaObservada >= 0.04 && taxaObservada <= 0.09, "taxa observada: " + taxaObservada);
    }

    @Test
    @DisplayName("Requisito Autoral 1 - acerto crítico multiplica o dano por 1.5")
    void testRequisitoAutoral1_DanoCriticoMultiplicaPor1_5() {
        Pokemon atacante = new Pokemon("Atacante", Tipo.NORMAL, 100, 500, 50, 50);
        Pokemon alvo = new Pokemon("Alvo", Tipo.NORMAL, 100, 50, 50, 50);
        Ataque golpe = new Ataque("Golpe", Tipo.FOGO, 150, EfeitoStatus.NENHUM, 0.0, 100);

        GeradorDoubleAleatorio sempreCritico = () -> 0.0;
        GeradorDoubleAleatorio nuncaCritico = () -> 0.99;

        int danoCritico = new CalcDano(sempreCritico, sempreAcerta()).calcularDano(golpe, atacante, alvo, new Canteiro());
        int danoNormal = new CalcDano(nuncaCritico, sempreAcerta()).calcularDano(golpe, atacante, alvo, new Canteiro());

        assertEquals(1.5, (double) danoCritico / danoNormal, 0.05);
    }

    @Test
    @DisplayName("Requisito Autoral 2 - STAB é ativado quando o tipo do PokéSal é igual ao tipo do ataque")
    void testRequisitoAutoral2_STABAtivaComMesmoTipo() {
        CharSal charSal = new CharSal(); // tipo FOGO
        Ataque brasa = buscar(charSal, "Brasa");
        Ataque arranhao = buscar(charSal, "Arranhão");

        assertTrue(CalcDano.calcSTAB(charSal, brasa),
                "STAB deveria ser ativado: CharSal (Fogo) usando Brasa (Fogo)");
        assertFalse(CalcDano.calcSTAB(charSal, arranhao),
                "STAB não deveria ser ativado: CharSal (Fogo) usando Arranhão (Normal)");
    }

    @Test
    @DisplayName("Requisito Autoral 3 - buffs/debuffs de estágio não persistem para um novo PokéSal (nova batalha)")
    void testRequisitoAutoral3_BuffsResetamEntreBatalhas() {
        Pokemon pokesalBatalha1 = new CharSal();
        pokesalBatalha1.alterarAtk(+3);
        pokesalBatalha1.alterarDef(-2);
        pokesalBatalha1.alterarSpd(+1);

        assertEquals(3, pokesalBatalha1.getEstagioAtk());
        assertEquals(-2, pokesalBatalha1.getEstagioDef());
        assertEquals(1, pokesalBatalha1.getEstagioSpd());

        Pokemon pokesalBatalha2 = new CharSal();

        assertEquals(0, pokesalBatalha2.getEstagioAtk());
        assertEquals(0, pokesalBatalha2.getEstagioDef());
        assertEquals(0, pokesalBatalha2.getEstagioSpd());
    }

    @Test
    @DisplayName("Requisito Autoral 3 - Rosnar diminui o ataque do alvo e Retirada aumenta a defesa do usuário")
    void testRequisitoAutoral3_MovimentosDeBuffEspecificos() {
        Ataque rosnar = buscar(new CharSal(), "Rosnar");
        assertNotNull(rosnar.buffAlvo());
        assertEquals(-1, rosnar.buffAlvo().getAtk());

        Ataque retirada = buscar(new SquirtSal(), "Retirada");
        assertNotNull(retirada.buffUsuario());
        assertEquals(1, retirada.buffUsuario().getDef());
    }

    @Test
    @DisplayName("Requisito Autoral 4 - Dança da Chuva ativa o terreno POÇA e Dia Ensolarado ativa o terreno ASFALTO")
    void testRequisitoAutoral4_AtaquesMudamTerreno() {
        Ataque dancaDaChuva = buscar(new SquirtSal(), "Dança da Chuva");
        assertInstanceOf(Poca.class, dancaDaChuva.ativaTerreno(),
                "Dança da Chuva (tipo Água) deveria ativar o terreno POÇA");

        Ataque diaEnsolarado = buscar(new CharSal(), "Dia Ensolarado");
        assertInstanceOf(Asfalto.class, diaEnsolarado.ativaTerreno(),
                "Dia Ensolarado (tipo Fogo) deveria ativar o terreno ASFALTO");
    }
}
