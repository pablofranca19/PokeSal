package utils;

import models.Inicial;
import models.enums.EfeitoStatus;
import models.enums.Terreno;

import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class Batalha {
    private final Inicial jogador;
    private final Inicial oponente;
    private final Mochila mochila;
    private Terreno terreno;

    public Batalha(Inicial jogador, Inicial oponente, Mochila mochila, Terreno terreno) {
        this.jogador = jogador;
        this.oponente = oponente;
        this.mochila = mochila;
        this.terreno = terreno;
    }

    public void iniciarBatalha(){
        Scanner sc = new Scanner(System.in);

        System.out.println("\n========================================");
        System.out.println(" Um " + oponente.getNome() + " selvagem apareceu!");
        System.out.println("========================================\n");

        while (jogador.getHpAtual() > 0 && oponente.getHpAtual() > 0) {
            int op;
            boolean turnoUsado = false;
            Ataque ataqueJogador = null;
            System.out.println("Terreno: " + terreno);

            while (!turnoUsado){
                IO.println("-SEU TURNO-");
                IO.println("""
                    ---SELECIONE UMA AÇÃO---
                    1. Lutar
                    2. Mochila
                    3. Inspecionar
                    4. Terminar
                    """);
                op = sc.nextInt();

                switch (op) {
                    case 1:
                        IO.println("---LUTAR---");
                        List<Ataque> golpes = jogador.getGolpes();
                        for (int i = 0; i < golpes.size(); i++){
                            IO.println((i+1) + ". " + golpes.get(i));
                        }

                        int opSair = golpes.size() + 1;
                        IO.println(opSair + ". Sair");

                        int opAtk = sc.nextInt();

                        if (!jogador.podeAgir()){
                            IO.println(jogador.getNome() + "(VOCÊ) está paralisado! Não consegue se mover!");
                            turnoUsado = true;
                            break;
                        }

                        if (opAtk >= 1 && opAtk <= golpes.size()){
                            ataqueJogador = golpes.get(opAtk - 1);
                            turnoUsado = true;
                        } else if (opAtk == opSair){
                            //sai do menu de luta sem consumir um turno
                        } else{
                            IO.println("Opção inválida.");
                        }
                        break;
                    case 2:
                        IO.println("""
                            ---USAR ITEM---
                            1. Poção (Cura 20HP)
                            2. Antídoto (Cura 'ENVENENADO')
                            3. Antiparalisia (Cura 'PARALISADO')
                            4. Antiqueimadura (Cura 'QUEIMADO')
                            5. Sair
                            """);
                        int opItem = sc.nextInt();

                        if (opItem >= 1 && opItem <= 4){
                            Item escolhido = mochila.getItens().get(opItem - 1);
                            try {
                                mochila.usarItem(escolhido, jogador);
                                turnoUsado = true;
                            } catch (IllegalStateException e){
                                IO.println(e.getMessage());
                            }
                        } else{
                            IO.println("Opção inválida.");
                        }
                        break;
                    case 3:
                        IO.println("---INSPECIONAR---");
                        IO.println("1. " + jogador.getNome() + "(VOCÊ)");
                        IO.println("2. " + oponente.getNome() + "(CPU)");
                        IO.println("3. Sair");
                        int opInsp = sc.nextInt();

                        switch (opInsp) {
                            case 1:
                                IO.println("HP: " + jogador.getHpAtual() + "/" + jogador.getHpMax());
                                break;
                            case 2:
                                IO.println("HP: " + oponente.getHpAtual() + "/" + oponente.getHpMax());
                                break;
                            case 3:
                                break;
                            default:
                                IO.println("Opção inválida.");
                        }
                        break;
                    case 4:
                        IO.println("Escapou com sucesso.");
                        return;
                    default:
                        IO.println("Opção inválida.");
                }
            }

            Ataque ataqueOponente = null;
            if (oponente.getHpAtual() > 0){
                List<Ataque> golpesOponente = oponente.getGolpes();
                ataqueOponente = golpesOponente.get(new Random().nextInt(golpesOponente.size()));
            }

            boolean batalhaEncerrada = false;
            if (ataqueJogador != null && ataqueOponente != null){
                if (jogador.getSpd() >= oponente.getSpd()){
                    executarTurnoJogador(ataqueJogador);
                    if (oponente.getHpAtual() > 0){
                        executarTurnoOponente(ataqueOponente);
                    }
                } else {
                    executarTurnoOponente(ataqueOponente);
                    if (jogador.getHpAtual() > 0){
                        executarTurnoJogador(ataqueJogador);
                    }
                }
            } else if (ataqueJogador != null){
                executarTurnoJogador(ataqueJogador);
            } else if (ataqueOponente != null){
                executarTurnoOponente(ataqueOponente);
            }

            if (jogador.getHpAtual() == 0 || oponente.getHpAtual() == 0){
                batalhaEncerrada = true;
            }

            if (!batalhaEncerrada){
                jogador.aplicarDanoStatus();
                if (jogador.getStatus() == EfeitoStatus.QUEIMADO){
                    IO.println(jogador.getNome() + "(VOCÊ) sofreu dano pela queimadura!" + "(HP: " + jogador.getHpAtual() + ")");
                } else if (jogador.getStatus() == EfeitoStatus.ENVENENADO){
                    IO.println(jogador.getNome() + "(VOCÊ) sofreu dano pelo veneno!" + "(HP: " + jogador.getHpAtual() + ")");
                }

                oponente.aplicarDanoStatus();
                if (oponente.getStatus() == EfeitoStatus.QUEIMADO){
                    IO.println(oponente.getNome() + "(CPU) sofreu dano pela queimadura!" + "(HP: " + oponente.getHpAtual() + ")");
                } else if (oponente.getStatus() == EfeitoStatus.ENVENENADO){
                    IO.println(oponente.getNome() + "(CPU) sofreu dano pelo veneno!" + "(HP: " + oponente.getHpAtual() + ")");
                }

                if (terreno == Terreno.CANTEIRO){
                    terreno.curaTerreno(jogador);
                    terreno.curaTerreno(oponente);
                }

                if (jogador.getHpAtual() == 0){
                    IO.println(jogador.getNome() + "(VOCÊ) desmaiou! Você perdeu!");
                } else if (oponente.getHpAtual() == 0){
                    IO.println(oponente.getNome() + "(CPU) desmaiou! Você venceu!");
                }

                System.out.println(jogador.getNome() + "(VOCÊ): " + jogador.getHpAtual() + "/" + jogador.getHpMax() + " HP");
                System.out.println(oponente.getNome() + "(CPU): " + oponente.getHpAtual() + "/" + oponente.getHpMax() + " HP");
            }
        }
        mochila.resetarUso();
    }

    private void executarTurnoJogador(Ataque ataqueJogador){
        executarAtaque(jogador, oponente, ataqueJogador);
        if (oponente.getHpAtual() == 0){
            IO.println("Você venceu!");
        }
    }

    private void executarTurnoOponente(Ataque ataqueOponente){
        IO.println("-TURNO DO OPONENTE-");
        if (!oponente.podeAgir()){
            IO.println(oponente.getNome() + "(CPU) está paralisado! Não consegue se mover!");
            return;
        }
        executarAtaque(oponente, jogador, ataqueOponente);
        if (jogador.getHpAtual() == 0){
            IO.println("Você perdeu!");
        }
    }

    private void executarAtaque(Inicial atacante, Inicial alvo, Ataque ataqueSelec){
        IO.println(atacante.getNome() + " usou " + ataqueSelec.getNome() + ".");

        if (ataqueSelec.getAtivaTerreno() != null){
            if (this.terreno == ataqueSelec.getAtivaTerreno()){
                IO.println("Mas falhou.");
            } else {
                this.terreno = ataqueSelec.getAtivaTerreno();
                IO.println("O terreno mudou para " + terreno);
            }
        }

        if (ataqueSelec.getPoder() > 0){
            int dano = CalcDano.calcularDano(ataqueSelec, atacante, alvo, terreno);

            if(CalcDano.foiCritico()){
                IO.println("Foi um acerto crítico!");
            }

            alvo.receberDano(dano);

            double efet = CalcDano.calcEfet(ataqueSelec, alvo.getTipo());
            if (efet == CalcDano.SUPER_EFETIVO){
                IO.println("É super efetivo!");
            } else if (efet == CalcDano.POUCO_EFETIVO){
                IO.println("Não é muito efetivo...");
            }

            IO.println("Dano causado: " + dano + "HP.");
        }

        if (ataqueSelec.getBuffUsuario() != null){
            Buff buff = ataqueSelec.getBuffUsuario();
            aplicarBuff(atacante, "ataque", buff.getAtk());
            aplicarBuff(atacante, "defesa", buff.getDef());
            aplicarBuff(atacante, "velocidade", buff.getSpd());
        }

        if (ataqueSelec.getBuffAlvo() != null){
            Buff buff = ataqueSelec.getBuffAlvo();
            aplicarBuff(alvo, "ataque", buff.getAtk());
            aplicarBuff(alvo, "defesa", buff.getDef());
            aplicarBuff(alvo, "velocidade", buff.getSpd());
        }

        if (ataqueSelec.getStatus() != EfeitoStatus.NENHUM){
            if (alvo.getStatus() != EfeitoStatus.NENHUM){
                if (ataqueSelec.getPoder() == 0){
                    IO.println("Mas falhou.");
                }
            } else if (Ataque.chanceStatus(ataqueSelec.getChanceEfeito())){
                alvo.setStatus(ataqueSelec.getStatus());
                EfeitoStatus statusAlvo = alvo.getStatus();

                if (statusAlvo == EfeitoStatus.PARALISADO){
                    IO.println(alvo.getNome() + " está paralisado! Talvez não consiga se mover!");
                } else if (statusAlvo == EfeitoStatus.QUEIMADO){
                    IO.println(alvo.getNome() + " foi queimado!");
                } else if (statusAlvo == EfeitoStatus.ENVENENADO){
                    IO.println(alvo.getNome() + " foi envenenado!");
                }
            }
        }

        if (alvo.getHpAtual() == 0){
            IO.println(alvo.getNome() + " desmaiou!");
        }
    }

    private void aplicarBuff(Inicial alvo, String nomeAtributo, int delta){
        int estagioAtual = switch (nomeAtributo){
            case "ataque" -> alvo.getEstagioAtk();
            case "defesa" -> alvo.getEstagioDef();
            case "velocidade" -> alvo.getEstagioSpd();
            default -> 0;
        };

        if (delta > 0 && estagioAtual == 6){
            IO.println(nomeAtributo + " de " + alvo.getNome() + " não consegue aumentar mais!");
        } else if (delta < 0 && estagioAtual == -6){
            IO.println(nomeAtributo + " de " + alvo.getNome() + " não consegue diminuir mais!");
        } else if (delta != 0){
            switch (nomeAtributo){
                case "ataque" -> alvo.alterarAtk(delta);
                case "defesa" -> alvo.alterarDef(delta);
                case "velocidade" -> alvo.alterarSpd(delta);
            }
            IO.println(nomeAtributo + " de " + alvo.getNome() + (delta > 0 ? " aumentou!" : " diminuiu!"));
        }
    }
}