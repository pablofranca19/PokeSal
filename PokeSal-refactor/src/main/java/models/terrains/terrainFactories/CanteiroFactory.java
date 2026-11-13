package models.terrains.terrainFactories;

import models.terrains.Canteiro;

public class CanteiroFactory implements TerrenoAbstractFactory {

    @Override
    public Canteiro gerarTerreno() {
        return new Canteiro();
    }
}