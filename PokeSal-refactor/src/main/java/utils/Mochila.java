package utils;


import models.Inicial;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Mochila {
    private static final int LIMITE_USO_ITENS = 2;

    private List<Item> itens;
    private int usados;

    public List<Item> getItens(){
        return Collections.unmodifiableList(itens);
    }

    public Mochila(){
        this.itens = new ArrayList<>();
        this.usados = 0;
    }

    /**
     * @param item
     */
    public void adicionarItem(Item item){
        if (item != null){
            this.itens.add(item);
        }
    }

    /**
     * @param item
     * @param inicial
     * @throws IllegalStateException
     */
    public void usarItem(Item item, Inicial inicial){
        if (this.usados >= LIMITE_USO_ITENS){
            throw new IllegalStateException("Você só pode usar 2 itens por batalha!");
        }

        if (this.itens.contains(item)){
            item.usar(inicial);
            this.usados++;
        }
    }

    public void resetarUso(){
        this.usados = 0;
    }
}
