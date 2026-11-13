package models.terrains.terrainFactories;

import models.terrains.Poca;

public class PocaFactory implements TerrenoAbstractFactory {

    @Override
    public Poca gerarTerreno() {
        return new Poca();
    }
}