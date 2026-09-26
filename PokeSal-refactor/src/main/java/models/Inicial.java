package models;

import models.enums.EfeitoStatus;
import models.enums.Tipo;
import utils.Ataque;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Inicial {
    protected String nome;
    protected Tipo tipo;
    protected int hpMax;
    protected int hpAtual;
    protected int atk;
    protected int def;
    protected int spd;
    protected int estagioAtk;
    protected int estagioDef;
    protected int estagioSpd;
    protected EfeitoStatus status = EfeitoStatus.NENHUM;
    protected List<Ataque> golpes;
    private static final Random RANDOM = new Random();

    public Inicial(String nome, Tipo tipo, int hpMax, int atk, int def, int spd){
        this.nome = nome;
        this.tipo = tipo;
        this.hpMax = hpMax;
        this.hpAtual = hpMax;
        this.atk = atk;
        this.def = def;
        this.spd = spd;
        this.estagioAtk = 0;
        this.estagioDef = 0;
        this.estagioSpd = 0;
        this.golpes = new ArrayList<>();
    }

    public void setStatus(EfeitoStatus status){
        this.status = status;
    }

    public void setHpAtual(int hpAtual){
        this.hpAtual = hpAtual;
    }

    private static final double DIVISOR_ESTAGIO = 2.0;

    private double calcMultEstagio(int estagio){
        if (estagio >= 0){
            return (DIVISOR_ESTAGIO + estagio) / DIVISOR_ESTAGIO;
        } else {
            return DIVISOR_ESTAGIO / (DIVISOR_ESTAGIO - estagio);
        }
    }

    public List<Ataque> getGolpes(){
        return Collections.unmodifiableList(golpes);
    }

    public EfeitoStatus getStatus(){
        return status;
    }

    public String getNome(){
        return nome;
    }

    public Tipo getTipo(){
        return tipo;
    }

    public int getHpAtual(){
        return hpAtual;
    }

    public int getHpMax(){
        return hpMax;
    }

    public int getAtk(){
        double base = atk;
        if (status == EfeitoStatus.QUEIMADO){
            base = base / 2;
        }
        base *= calcMultEstagio(estagioAtk);
        return (int) base;
    }

    public int getDef(){
        double base = def;
        base *= calcMultEstagio(estagioDef);
        return (int) base;
    }

    public int getSpd(){
        double base = spd;
        if (status == EfeitoStatus.PARALISADO){
            base = base / 2;
        }
        base *= calcMultEstagio(estagioSpd);
        return (int) base;
    }

    public int getEstagioAtk(){
        return estagioAtk;
    }

    public int getEstagioDef(){
        return estagioDef;
    }

    public int getEstagioSpd(){
        return estagioSpd;
    }

    public void curar(int quant){
        int hpAntes = this.hpAtual;
        this.hpAtual = Math.min(this.hpAtual + quant, hpMax);
        int curado = this.hpAtual - hpAntes;
        IO.println(nome + " recuperou " + curado + "HP! (HP atual: " + this.hpAtual + ")");
    }

    private static final int ESTAGIO_MIN = -6;
    private static final int ESTAGIO_MAX = 6;

    public void alterarAtk(int quant){
        this.estagioAtk = Math.clamp(this.estagioAtk + quant, ESTAGIO_MIN, ESTAGIO_MAX);
    }

    public void alterarDef(int quant){
        this.estagioDef = Math.clamp(this.estagioDef + quant, ESTAGIO_MIN, ESTAGIO_MAX);
    }

    public void alterarSpd(int quant){
        this.estagioSpd = Math.clamp(this.estagioSpd + quant, ESTAGIO_MIN, ESTAGIO_MAX);
    }

    public void curarStatus(){
        this.status = EfeitoStatus.NENHUM;
        IO.println(nome + " ficou saudável!");
    }

    public void receberDano(int dano){
        this.hpAtual -= dano;
        if (this.hpAtual <= 0){
            this.hpAtual = 0;
        }
    }

    private static final int DIVISOR_DANO_QUEIMADURA = 16;
    private static final int DIVISOR_DANO_VENENO = 8;

    public void aplicarDanoStatus(){
        if (status == EfeitoStatus.QUEIMADO){
            this.hpAtual = Math.max(hpAtual - hpMax/DIVISOR_DANO_QUEIMADURA, 0);
        }
        if (status == EfeitoStatus.ENVENENADO){
            this.hpAtual = Math.max(hpAtual - hpMax/DIVISOR_DANO_VENENO, 0);
        }
    }

    private static final double CHANCE_PARALISIA = 0.25;

    public boolean podeAgir(){
        if (status == EfeitoStatus.PARALISADO){
            return RANDOM.nextDouble() >= CHANCE_PARALISIA;
        }
        return true;
    }
}
