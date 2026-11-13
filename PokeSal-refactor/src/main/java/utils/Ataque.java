package utils;

import models.enums.EfeitoStatus;
import models.terrains.Terreno;
import models.enums.Tipo;
import services.GeradorDoubleAleatorio;

public record Ataque(String nome, Tipo tipo, int poder, EfeitoStatus status, double chanceEfeito, int precisao,
                     Terreno ativaTerreno, Buff buffUsuario, Buff buffAlvo) {
    private static final int PRECISAO_PADRAO = 100;

    public Ataque(String nome, Tipo tipo, int poder, EfeitoStatus status, double chanceEfeito,
                  Terreno ativaTerreno, Buff buffUsuario, Buff buffAlvo, int precisao) {
        this(nome, tipo, poder, status, chanceEfeito, precisao, ativaTerreno, buffUsuario, buffAlvo);
    }

    public Ataque(String nome, Tipo tipo, int poder, EfeitoStatus status, double chanceEfeito, int precisao) {
        this(nome, tipo, poder, status, chanceEfeito, null, null, null, precisao);
    }

    public Ataque(String nome, Tipo tipo, int poder, EfeitoStatus status, double chanceEfeito, Terreno ativaTerreno) {
        this(nome, tipo, poder, status, chanceEfeito, ativaTerreno, null, null, PRECISAO_PADRAO);
    }

    public boolean chanceStatus(GeradorDoubleAleatorio geradorDouble) {
        return geradorDouble.gerarDouble() < chanceEfeito;
    }

    @Override
    public String toString() {
        return nome + " (" + tipo + " - " + poder + ")";
    }
}
