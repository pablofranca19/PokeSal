package utils;

import models.enums.EfeitoStatus;
import models.terrains.Terreno;

import java.util.List;

public interface RegistroBatalha{

    void registrarAtaque(Ataque ataqueRealizado);

    void registrarDano(int danoCausado);

    void registrarEfeitoStatus(EfeitoStatus efeitoStatus);

    void registrarEfeitoTerreno(Terreno efeitoTerreno);

    void registrarItem(Item item);

    Ataque getAtaqueRealizado();

    int getDano();

    EfeitoStatus getEfeitoStatus();

    Terreno getEfeitoDeTerreno();

    List<Item> getItensUsados();
}

