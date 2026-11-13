package models.terrains.terrainFactories;

import models.terrains.Asfalto;

public class AsfaltoFactory implements TerrenoAbstractFactory {

    @Override
    public Asfalto gerarTerreno() {
        return new Asfalto();
    }
}