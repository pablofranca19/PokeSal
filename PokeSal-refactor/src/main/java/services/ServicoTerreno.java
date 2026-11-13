package services;

import models.enums.Clima;
import models.terrains.Terreno;

import java.time.LocalTime;

public interface ServicoTerreno {
    Terreno gerarTerreno();
    Clima getClima();
    LocalTime getHorarioAtual();
}
