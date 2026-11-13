package services;

import java.time.Clock;
import java.time.LocalTime;

import models.enums.Clima;
import models.terrains.Terreno;
import models.terrains.terrainFactories.AsfaltoFactory;
import models.terrains.terrainFactories.PocaFactory;
import models.terrains.terrainFactories.CanteiroFactory;

public class TerrenoService implements ServicoTerreno{
    private final Clima clima;
    private final Clock clock;

    private static final LocalTime INICIO_DIA = LocalTime.of(6, 0);
    private static final LocalTime FIM_DIA = LocalTime.of(18, 0);

    public TerrenoService(Clima clima, Clock clock) {
        this.clima = clima;
        this.clock = clock;
    }

    @Override
    public Terreno gerarTerreno() {
        LocalTime horaAtual = LocalTime.now(clock);

        boolean isDia = !horaAtual.isBefore(INICIO_DIA) && horaAtual.isBefore(FIM_DIA);
        boolean isNoite = !isDia;

        if (isDia && clima == Clima.SOL){
            return new AsfaltoFactory().gerarTerreno();
        } else if (isNoite && clima == Clima.CHUVA){
            return new PocaFactory().gerarTerreno();
        } else{
            return new CanteiroFactory().gerarTerreno();
        }
    }

    @Override
    public Clima getClima() {
        return clima;
    }

    @Override
    public LocalTime getHorarioAtual() {
        return LocalTime.now(clock);
    }
}