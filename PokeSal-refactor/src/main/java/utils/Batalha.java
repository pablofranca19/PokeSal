package utils;

import models.Pokemon;
import models.Treinador;
import models.enums.Clima;
import models.enums.EfeitoStatus;
import models.terrains.Terreno;
import services.GeradorDoubleAleatorio;
import services.ServicoTerreno;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Batalha {
    private static final int ESTAGIO_MAX = 6;
    private static final int ESTAGIO_MIN = -6;
    private static final int QTD_ITENS_MOCHILA = 4;
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final Pokemon pokemonJogador;
    private final Pokemon pokemonOponente;
    private final Treinador jogador;
    private final Treinador oponente;
    private final CalcDano calcDano;
    private final ServicoTerreno servicoTerreno;
    private final RegistroBatalha registro;
    private final RepositorioResultados repositorio;
    private final GeradorDoubleAleatorio gerador;
    private final Scanner sc;
    private final boolean podeFugir;
    private final String tagJogador;
    private final String tagOponente;

    private Terreno terreno;
    private Terreno terrenoAnterior;

    public Batalha(Treinador jogador, Treinador oponente, ServicoTerreno servicoTerreno, CalcDano calcDano,
                   RegistroBatalha registro, RepositorioResultados repositorio, GeradorDoubleAleatorio gerador,
                   Scanner sc, boolean podeFugir) {
        this.jogador = jogador;
        this.oponente = oponente;
        this.servicoTerreno = servicoTerreno;
        this.calcDano = calcDano;
        this.terreno = servicoTerreno.gerarTerreno();
        this.terrenoAnterior = this.terreno;
        this.registro = registro;
        this.repositorio = repositorio;
        this.gerador = gerador;
        this.pokemonJogador = jogador.getPokemons().getFirst();
        this.pokemonOponente = oponente.getPokemons().getFirst();
        this.sc = sc;
        this.podeFugir = podeFugir;
        this.tagJogador = jogador.isUsuario() ? "VOCÊ" : jogador.getNome();
        this.tagOponente = podeFugir ? "CPU" : oponente.getNome();
    }

    public Batalha(Treinador jogador, Treinador oponente, ServicoTerreno servicoTerreno, CalcDano calcDano,
                   RegistroBatalha registro, RepositorioResultados repositorio, GeradorDoubleAleatorio gerador, Scanner sc) {
        this(jogador, oponente, servicoTerreno, calcDano, registro, repositorio, gerador, sc, true);
    }

    public ResultadoBatalha iniciarBatalha(){
        Treinador vencedor = null;
        Treinador perdedor = null;
        int contadorTurnos = 0;

        List<Pokemon> pokemonsUsados = new ArrayList<>();
        pokemonsUsados.add(pokemonJogador);
        pokemonsUsados.add(pokemonOponente);

        System.out.println("\n========================================");
        if (podeFugir) {
            System.out.println(" Um " + pokemonOponente.getNome() + " selvagem apareceu!");
        } else if (jogador.isUsuario()) {
            System.out.println(" Treinador(a) " + oponente.getNome() + " quer batalhar!");
        } else {
            System.out.println(" " + jogador.getNome() + " VS " + oponente.getNome() + "!");
        }
        System.out.println("========================================\n");

        while (pokemonJogador.getHpAtual() > 0 && pokemonOponente.getHpAtual() > 0) {
            Clima climaAtual = servicoTerreno.getClima();
            LocalTime horarioAtual = servicoTerreno.getHorarioAtual();
            String horaFormatada = (horarioAtual != null) ? horarioAtual.format(FORMATO_HORA) : "--:--";

            System.out.println("\nClima: " + climaAtual + " | Hora: " + horaFormatada);
            System.out.println("Terreno: " + terreno);


            Terreno terrenoAtual = servicoTerreno.gerarTerreno();
            if (terrenoAtual.getClass() != terrenoAnterior.getClass()){
                this.terreno = terrenoAtual;
                this.terrenoAnterior = terrenoAtual;
            }

            int op;
            boolean turnoUsado = false;
            Item itemUsadoTurno = null;
            Ataque ataqueJogador = null;

            if (jogador.usuario()){
                while (!turnoUsado) {
                    System.out.println("\n-SEU TURNO-");
                    System.out.println("""
                            ---SELECIONE UMA AÇÃO---
                            1. Lutar
                            2. Mochila
                            3. Inspecionar
                            4. Terminar
                            """);
                    op = sc.nextInt();

                    switch (op) {
                        case 1:
                            System.out.println("---LUTAR---");
                            List<Ataque> golpes = pokemonJogador.getGolpes();
                            for (int i = 0; i < golpes.size(); i++) {
                                System.out.println((i + 1) + ". " + golpes.get(i));
                            }

                            int opSair = golpes.size() + 1;
                            System.out.println(opSair + ". Sair");

                            int opAtk = sc.nextInt();

                            if (!pokemonJogador.podeAgir(gerador)) {
                                System.out.println(pokemonJogador.getNome() + tagJogador + " está paralisado! Não consegue se mover!");
                                turnoUsado = true;
                                break;
                            }

                            if (opAtk >= 1 && opAtk <= golpes.size()) {
                                ataqueJogador = golpes.get(opAtk - 1);
                                turnoUsado = true;
                            } else if (opAtk != opSair) {
                                System.out.println("Opção inválida.");
                            }
                            break;
                        case 2:
                            System.out.println("""
                                    ---USAR ITEM---
                                    1. Poção (Cura 20HP)
                                    2. Antídoto (Cura 'ENVENENADO')
                                    3. Antiparalisia (Cura 'PARALISADO')
                                    4. Antiqueimadura (Cura 'QUEIMADO')
                                    5. Sair
                                    """);
                            int opItem = sc.nextInt();

                            if (opItem >= 1 && opItem <= QTD_ITENS_MOCHILA) {
                                Item escolhido = jogador.mochila().getItens().get(opItem - 1);
                                try {
                                    jogador.mochila().usarItem(escolhido, pokemonJogador);
                                    turnoUsado = true;
                                    itemUsadoTurno = escolhido;
                                    System.out.println();
                                } catch (IllegalStateException e) {
                                    System.out.println(e.getMessage());
                                }
                            } else {
                                System.out.println("Opção inválida.");
                            }
                            break;
                        case 3:
                            System.out.println("---INSPECIONAR---");
                            System.out.println("1. " + pokemonJogador.getNome() + "(" + tagJogador + ")");
                            System.out.println("2. " + pokemonOponente.getNome() + "(" + tagOponente + ")");
                            System.out.println("3. Sair");
                            int opInsp = sc.nextInt();

                            switch (opInsp) {
                                case 1:
                                    System.out.println("HP: " + pokemonJogador.getHpAtual() + "/" + pokemonJogador.getHpMax());
                                    break;
                                case 2:
                                    System.out.println("HP: " + pokemonOponente.getHpAtual() + "/" + pokemonOponente.getHpMax());
                                    break;
                                case 3:
                                    break;
                                default:
                                    System.out.println("Opção inválida.");
                            }
                            System.out.println();
                            break;
                        case 4:
                            if (!podeFugir) {
                                System.out.println("Não é possível fugir de uma batalha de treinador!");
                                break;
                            }
                            System.out.println("Escapou com sucesso.");
                            return null;
                        default:
                            System.out.println("Opção inválida.");
                    }
                }
            } else {
                if (!pokemonJogador.podeAgir(gerador)) {
                    System.out.println(pokemonJogador.getNome() + " (" + jogador.nome() + ") está paralisado! Não consegue se mover!");
                } else {
                    List<Ataque> golpesJogador = pokemonJogador.getGolpes();
                    ataqueJogador = golpesJogador.get((int) (gerador.gerarDouble() * golpesJogador.size()));
                }
            }

            contadorTurnos++;

            Ataque ataqueOponente = null;
            if (pokemonOponente.getHpAtual() > 0) {
                List<Ataque> golpesOponente = pokemonOponente.getGolpes();
                ataqueOponente = golpesOponente.get((int) (gerador.gerarDouble() * golpesOponente.size()));
            }

            boolean batalhaEncerrada = false;
            if (ataqueJogador != null && ataqueOponente != null) {
                if (pokemonJogador.getSpd() >= pokemonOponente.getSpd()) {
                    executarTurnoJogador(ataqueJogador);
                    if (pokemonOponente.getHpAtual() > 0) {
                        executarTurnoOponente(ataqueOponente);
                    }
                } else {
                    executarTurnoOponente(ataqueOponente);
                    if (pokemonJogador.getHpAtual() > 0) {
                        executarTurnoJogador(ataqueJogador);
                    }
                }
            } else if (ataqueJogador != null) {
                executarTurnoJogador(ataqueJogador);
            } else if (ataqueOponente != null) {
                executarTurnoOponente(ataqueOponente);
            }

            if (pokemonJogador.getHpAtual() == 0 || pokemonOponente.getHpAtual() == 0) {
                batalhaEncerrada = true;
            }

            if (!batalhaEncerrada) {
                pokemonJogador.aplicarDanoStatus();
                if (pokemonJogador.getStatus() == EfeitoStatus.QUEIMADO) {
                    System.out.println(pokemonJogador.getNome() + "(" + tagJogador + ") sofreu dano pela queimadura!" + "(HP: " + pokemonJogador.getHpAtual() + ")");
                } else if (pokemonJogador.getStatus() == EfeitoStatus.ENVENENADO) {
                    System.out.println(pokemonJogador.getNome() + "(" + tagJogador + ") sofreu dano pelo veneno!" + "(HP: " + pokemonJogador.getHpAtual() + ")");
                }

                pokemonOponente.aplicarDanoStatus();
                if (pokemonOponente.getStatus() == EfeitoStatus.QUEIMADO) {
                    System.out.println(pokemonOponente.getNome() + "(" + tagOponente + ") sofreu dano pela queimadura!" + "(HP: " + pokemonOponente.getHpAtual() + ")");
                } else if (pokemonOponente.getStatus() == EfeitoStatus.ENVENENADO) {
                    System.out.println(pokemonOponente.getNome() + "(" + tagOponente + ") sofreu dano pelo veneno!" + "(HP: " + pokemonOponente.getHpAtual() + ")");
                }

                terreno.curaTerreno(pokemonJogador);
                terreno.curaTerreno(pokemonOponente);

                System.out.println(pokemonJogador.getNome() + "(" + tagJogador + "): " + pokemonJogador.getHpAtual() + "/" + pokemonJogador.getHpMax() + " HP");
                System.out.println(pokemonOponente.getNome() + "(" + tagOponente + "): " + pokemonOponente.getHpAtual() + "/" + pokemonOponente.getHpMax() + " HP");
            }
            if (itemUsadoTurno != null){
                registro.registrarItem(itemUsadoTurno);
            }
        }
        jogador.mochila().resetarUso();

        if (pokemonJogador.getHpAtual() == 0) {
            System.out.println(pokemonJogador.getNome() + " (" + tagJogador + ") desmaiou! " + oponente.getNome() + " venceu!");
            vencedor = oponente;
            perdedor = jogador;
        } else if (pokemonOponente.getHpAtual() == 0) {
            System.out.println(pokemonOponente.getNome() + " (" + tagOponente + ") desmaiou! " + jogador.getNome() + " venceu!");
            vencedor = jogador;
            perdedor = oponente;
        }

        ResultadoBatalha resultado = new ResultadoBatalha(vencedor, perdedor, contadorTurnos, pokemonsUsados);

        try {
            repositorio.salvarResultados(resultado);
        } catch (Exception e){
            System.err.println("Não foi possível salvar o resultado " + e.getMessage());
        }

        return resultado;
    }

    private void executarTurnoJogador(Ataque ataqueJogador){
        executarAtaque(pokemonJogador, pokemonOponente, ataqueJogador);
    }

    private void executarTurnoOponente(Ataque ataqueOponente){
        System.out.println("\n-TURNO DO OPONENTE-");
        if (!pokemonOponente.podeAgir(gerador)){
            System.out.println(pokemonOponente.getNome() + "(" + tagOponente + ") está paralisado! Não consegue se mover!");
            return;
        }
        executarAtaque(pokemonOponente, pokemonJogador, ataqueOponente);
    }

    private void executarAtaque(Pokemon atacante, Pokemon alvo, Ataque ataqueSelec){
        System.out.println(atacante.getNome() + " usou " + ataqueSelec.nome() + ".");
        registro.registrarAtaque(ataqueSelec);

        if (ataqueSelec.ativaTerreno() != null){
            if (this.terreno.getClass() == ataqueSelec.ativaTerreno().getClass()){
                System.out.println("Mas falhou.");
            } else {
                this.terreno = ataqueSelec.ativaTerreno();
                System.out.println("O terreno mudou para " + terreno.getClass().getSimpleName());
            }
        }

        if (ataqueSelec.poder() > 0){
            int dano = calcDano.calcularDano(ataqueSelec, atacante, alvo, terreno);
            registro.registrarDano(dano);

            if (dano == 0) {
                System.out.println(alvo.getNome() + " desviou do ataque.");
                return;
            }

            alvo.receberDano(dano);

            double efet = CalcDano.calcEfet(ataqueSelec, alvo.getTipo());
            if (efet == CalcDano.SUPER_EFETIVO){
                System.out.println("É super efetivo!");
            } else if (efet == CalcDano.POUCO_EFETIVO){
                System.out.println("Não é muito efetivo...");
            }

            if(calcDano.foiCritico()){
                System.out.println("Foi um acerto crítico!");
            }

            System.out.println("Dano causado: " + dano + "HP.");
        }

        if (ataqueSelec.buffUsuario() != null){
            Buff buff = ataqueSelec.buffUsuario();
            aplicarBuff(atacante, "ataque", buff.getAtk());
            aplicarBuff(atacante, "defesa", buff.getDef());
            aplicarBuff(atacante, "velocidade", buff.getSpd());
        }

        if (ataqueSelec.buffAlvo() != null){
            Buff buff = ataqueSelec.buffAlvo();
            aplicarBuff(alvo, "ataque", buff.getAtk());
            aplicarBuff(alvo, "defesa", buff.getDef());
            aplicarBuff(alvo, "velocidade", buff.getSpd());
        }

        if (ataqueSelec.status() != EfeitoStatus.NENHUM){
            if (alvo.getStatus() != EfeitoStatus.NENHUM){
                if (ataqueSelec.poder() == 0){
                    System.out.println("Mas falhou.");
                }
            } else if (ataqueSelec.chanceStatus(gerador)){
                alvo.setStatus(ataqueSelec.status());
                EfeitoStatus statusAlvo = alvo.getStatus();

                if (statusAlvo == EfeitoStatus.PARALISADO){
                    System.out.println(alvo.getNome() + " está paralisado! Talvez não consiga se mover!");
                } else if (statusAlvo == EfeitoStatus.QUEIMADO){
                    System.out.println(alvo.getNome() + " foi queimado!");
                } else if (statusAlvo == EfeitoStatus.ENVENENADO){
                    System.out.println(alvo.getNome() + " foi envenenado!");
                }
                registro.registrarEfeitoStatus(statusAlvo);
            }
        }
        registro.registrarEfeitoTerreno(terreno);

        if (alvo.getHpAtual() == 0){
            System.out.println(alvo.getNome() + " desmaiou!");
        }
    }

    private void aplicarBuff(Pokemon alvo, String nomeAtributo, int delta){
        int estagioAtual = switch (nomeAtributo){
            case "ataque" -> alvo.getEstagioAtk();
            case "defesa" -> alvo.getEstagioDef();
            case "velocidade" -> alvo.getEstagioSpd();
            default -> 0;
        };

        if (delta > 0 && estagioAtual == ESTAGIO_MAX){
            System.out.println(nomeAtributo + " de " + alvo.getNome() + " não consegue aumentar mais!");
        } else if (delta < 0 && estagioAtual == ESTAGIO_MIN){
            System.out.println(nomeAtributo + " de " + alvo.getNome() + " não consegue diminuir mais!");
        } else if (delta != 0){
            switch (nomeAtributo){
                case "ataque" -> alvo.alterarAtk(delta);
                case "defesa" -> alvo.alterarDef(delta);
                case "velocidade" -> alvo.alterarSpd(delta);
            }
            System.out.println(nomeAtributo + " de " + alvo.getNome() + (delta > 0 ? " aumentou!" : " diminuiu!"));
        }
    }
}