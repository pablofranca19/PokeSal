package models;

import models.Inicial;
import models.enums.EfeitoStatus;
import models.enums.Terreno;
import models.enums.Tipo;
import models.pokesal.CharSal;
import models.pokesal.SquirtSal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import utils.Ataque;
import utils.CalcDano;

import static org.junit.jupiter.api.Assertions.*;

class RequisitosAutoraisTest {

    @Test
    @DisplayName("Requisito Autoral 1: acerto crítico ocorre com probabilidade próxima de 1/16 (6,25%)")
    void testRequisitoAutoral1_DanoCriticoProbabilidade() {
        final int TENTATIVAS = 200_000;
        int acertosCriticos = 0;

        for (int i = 0; i < TENTATIVAS; i++) {
            if (CalcDano.calcCrit()) {
                acertosCriticos++;
            }
        }

        double taxaObservada = (double) acertosCriticos / TENTATIVAS;

        assertTrue(taxaObservada >= 0.04 && taxaObservada <= 0.09);
    }

    @Test
    @DisplayName("Requisito Autoral 1 - acerto crítico dobra o dano (multiplicador = 2.0)")
    void testRequisitoAutoral1_DanoCriticoDobraDano() {
        assertEquals(2.0, obterConstanteCrit(), 0.05);
    }

    private double obterConstanteCrit() {
        Inicial atacante = new Inicial("Atacante", Tipo.NORMAL, 100, 500, 50, 50);
        Inicial alvo = new Inicial("Alvo", Tipo.NORMAL, 100, 50, 50, 50);
        Ataque golpe = new Ataque("Golpe", Tipo.NORMAL, 150, EfeitoStatus.NENHUM, 0.0);

        Integer danoCritico = null;
        Integer danoNormal = null;

        for (int i = 0; i < 5000 && (danoCritico == null || danoNormal == null); i++) {
            alvo.setHpAtual(alvo.getHpMax());
            int dano = CalcDano.calcularDano(golpe, atacante, alvo, Terreno.ASFALTO);
            if (CalcDano.foiCritico() && danoCritico == null) {
                danoCritico = dano;
            } else if (!CalcDano.foiCritico() && danoNormal == null) {
                danoNormal = dano;
            }
        }

        assertNotNull(danoCritico, "Não foi possível observar um acerto crítico em 5000 tentativas");
        assertNotNull(danoNormal, "Não foi possível observar um acerto normal em 5000 tentativas");

        return (double) danoCritico / danoNormal;
    }

    @Test
    @DisplayName("Requisito Autoral 2 - STAB é ativado quando o tipo do PokéSal é igual ao tipo do ataque")
    void testRequisitoAutoral2_STABAtivaComMesmoTipo() {
        CharSal charSal = new CharSal(); // tipo FOGO
        Ataque brasa = charSal.getGolpes().stream()
                .filter(a -> a.getNome().equals("Brasa"))
                .findFirst()
                .orElseThrow();
        Ataque arranhao = charSal.getGolpes().stream()
                .filter(a -> a.getNome().equals("Arranhão"))
                .findFirst()
                .orElseThrow();

        assertTrue(CalcDano.calcSTAB(charSal, brasa),
                "STAB deveria ser ativado: CharSal (Fogo) usando Brasa (Fogo)");
        assertFalse(CalcDano.calcSTAB(charSal, arranhao),
                "STAB não deveria ser ativado: CharSal (Fogo) usando Arranhão (Normal)");
    }


    @Test
    @DisplayName("Requisito Autoral 3 - buffs/debuffs de estágio não persistem para um novo PokéSal (nova batalha)")
    void testRequisitoAutoral3_BuffsResetamEntreBatalhas() {
        Inicial pokesalBatalha1 = new CharSal();
        pokesalBatalha1.alterarAtk(+3);
        pokesalBatalha1.alterarDef(-2);
        pokesalBatalha1.alterarSpd(+1);

        assertEquals(3, pokesalBatalha1.getEstagioAtk());
        assertEquals(-2, pokesalBatalha1.getEstagioDef());
        assertEquals(1, pokesalBatalha1.getEstagioSpd());

        Inicial pokesalBatalha2 = new CharSal();

        assertEquals(0, pokesalBatalha2.getEstagioAtk());
        assertEquals(0, pokesalBatalha2.getEstagioDef());
        assertEquals(0, pokesalBatalha2.getEstagioSpd());
    }

    @Test
    @DisplayName("Requisito Autoral 3 - Rosnar diminui o ataque do alvo e Retirada aumenta a defesa do usuário")
    void testRequisitoAutoral3_MovimentosDeBuffEspecificos() {
        CharSal charSal = new CharSal();
        Ataque rosnar = charSal.getGolpes().stream()
                .filter(a -> a.getNome().equals("Rosnar"))
                .findFirst()
                .orElseThrow();

        assertNotNull(rosnar.getBuffAlvo());
        assertEquals(-1, rosnar.getBuffAlvo().getAtk());

        SquirtSal squirtSal = new SquirtSal();
        Ataque retirada = squirtSal.getGolpes().stream()
                .filter(a -> a.getNome().equals("Retirada"))
                .findFirst()
                .orElseThrow();

        assertNotNull(retirada.getBuffUsuario());
        assertEquals(1, retirada.getBuffUsuario().getDef());
    }

    @Test
    @DisplayName("Requisito Autoral 4 - Dança da Chuva ativa o terreno POÇA e Dia Ensolarado ativa o terreno ASFALTO")
    void testRequisitoAutoral4_AtaquesMudamTerreno() {
        SquirtSal squirtSal = new SquirtSal();
        Ataque dancaDaChuva = squirtSal.getGolpes().stream()
                .filter(a -> a.getNome().equals("Dança da Chuva"))
                .findFirst()
                .orElseThrow();

        assertEquals(Terreno.POCA, dancaDaChuva.getAtivaTerreno(),
                "Dança da Chuva (tipo Água) deveria ativar o terreno POÇA");

        CharSal charSal = new CharSal();
        Ataque diaEnsolarado = charSal.getGolpes().stream()
                .filter(a -> a.getNome().equals("Dia Ensolarado"))
                .findFirst()
                .orElseThrow();

        assertEquals(Terreno.ASFALTO, diaEnsolarado.getAtivaTerreno(),
                "Dia Ensolarado (tipo Fogo) deveria ativar o terreno ASFALTO");
    }
}