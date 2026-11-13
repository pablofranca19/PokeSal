package models;

import utils.Mochila;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Treinador(String nome, Mochila mochila, List<Pokemon> pokemons, boolean usuario) {
    public Treinador(String nome, Mochila mochila, List<Pokemon> pokemons, boolean usuario) {
        this.nome = nome;
        this.mochila = mochila;
        this.pokemons = new ArrayList<>(pokemons);
        this.usuario = usuario;
    }

    public Treinador(String nome, Mochila mochila, List<Pokemon> pokemons) {
        this(nome, mochila, pokemons, true);
    }

    public List<Pokemon> getPokemons() {
        return Collections.unmodifiableList(pokemons);
    }

    public boolean isUsuario(){
        return this.usuario;
    }

    public String getNome() {
        return this.nome;
    }
}
