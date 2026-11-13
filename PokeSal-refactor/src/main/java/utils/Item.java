package utils;

import models.Pokemon;

public abstract class Item{
    protected String nome;

    public Item(String nome){
        this.nome = nome;
    }

    public abstract void usar(Pokemon pokemon);
}