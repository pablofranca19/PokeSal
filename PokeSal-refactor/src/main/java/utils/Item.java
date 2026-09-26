package utils;

import models.Inicial;

public abstract class Item{
    protected String nome;

    public Item(String nome){
        this.nome = nome;
    }

    public abstract void usar(Inicial inicial);
}