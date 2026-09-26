package application;

import models.Inicial;
import models.enums.EfeitoStatus;
import models.enums.Terreno;
import models.pokesal.*;
import utils.*;

import java.util.Scanner;

public class Main {
    static void main() {
        run();
    }

    private static void run(){
        Scanner sc = new Scanner(System.in);
        Inicial inicial = null;
        boolean sair = false;
        int numeroBatalha = 1;
        do {
            IO.println("Batalha " + numeroBatalha +"-");
            IO.println("""
                --- ESCOLHA O SEU POKÉSAL INICIAL ---
                1. BulbaSal (Planta)
                2. CharSal (Fogo)
                3. SquirtSal (Água)
                4. ChikoSal (Planta)
                5. CyndaSal (Fogo)
                6. TotoSal (Água)
                7. Sair
                """);

            int op = sc.nextInt();

            switch (op) {
                case 1 -> inicial = new BulbaSal();
                case 2 -> inicial = new CharSal();
                case 3 -> inicial = new SquirtSal();
                case 4 -> inicial = new ChikoSal();
                case 5 -> inicial = new CyndaSal();
                case 6 -> inicial = new TotoSal();
                case 7 -> sair = true;
                default -> {
                    IO.println("Opção inválida.");
                    continue;
                }
            }

            if (!sair){
                IO.println("COMEÇAR A BATALHA? (S/N)");
                char conf = sc.next().toLowerCase().charAt(0);

                if (conf == 's') {
                    Mochila mochila = new Mochila();
                    mochila.adicionarItem(new Pocao("Poção"));
                    mochila.adicionarItem(new CuraStatus("Antídoto", EfeitoStatus.ENVENENADO));
                    mochila.adicionarItem(new CuraStatus("Antiqueimadura", EfeitoStatus.QUEIMADO));
                    mochila.adicionarItem(new CuraStatus("Antiparalisia", EfeitoStatus.PARALISADO));
                    IO.println("A BATALHA COMEÇA!");

                    Inicial oponente = GerarInicial.gerar();
                    Terreno terreno = GerarTerreno.gerar();

                    Batalha bat = new Batalha(inicial, oponente, mochila, terreno);
                    bat.iniciarBatalha();
                    numeroBatalha++;
                } else if (conf != 'n'){
                    IO.println("Opção inválida.");
                }
            }

        } while (!sair);
    }
}
