package utils;

import models.enums.EfeitoStatus;
import models.terrains.Terreno;

import java.util.ArrayList;
import java.util.List;

import static models.enums.EfeitoStatus.NENHUM;

public class RegistroBatalhaMemoria implements RegistroBatalha{
    private Ataque ataqueRealizado;
    private int dano;
    private EfeitoStatus efeitoStatus;
    private Terreno efeitoDeTerreno;
    private List<Item> itensUsados;

    public RegistroBatalhaMemoria() {
        itensUsados = new ArrayList<>();
    }

    @Override
    public void registrarAtaque(Ataque ataqueRealizado) {
        this.ataqueRealizado = ataqueRealizado;
        dano = 0;
        efeitoStatus = NENHUM;
        efeitoDeTerreno = null;
        itensUsados.clear();
    }

    @Override
    public void registrarDano(int danoCausado) {
        this.dano = danoCausado;
    }

    @Override
    public void registrarEfeitoStatus(EfeitoStatus efeitoStatus) {
        this.efeitoStatus = efeitoStatus;
    }

    @Override
    public void registrarEfeitoTerreno(Terreno efeitoTerreno) {
        this.efeitoDeTerreno = efeitoTerreno;
    }

    @Override
    public void registrarItem(Item item) {
        itensUsados.add(item);
    }

    @Override
    public Ataque getAtaqueRealizado() {
        return ataqueRealizado;
    }

    @Override
    public int getDano() {
        return dano;
    }

    @Override
    public EfeitoStatus getEfeitoStatus() {
        return efeitoStatus;
    }

    @Override
    public Terreno getEfeitoDeTerreno() {
        return efeitoDeTerreno;
    }

    @Override
    public List<Item> getItensUsados() {
        return List.copyOf(itensUsados);
    }
}
