package application;

import models.Pokemon;
import models.Treinador;
import models.enums.Clima;
import models.enums.EfeitoStatus;
import models.pokesal.*;
import services.GeradorInteiroAleatorio;
import services.GeradorService;
import services.ServicoTerreno;
import services.TerrenoService;
import utils.*;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        run();
    }

    private static void run(){
        Scanner sc = new Scanner(System.in);
        GeradorService gerador = new GeradorService();
        GerarInicial gerarInicial = new GerarInicial(gerador);
        ServicoTerreno servicoTerreno = new TerrenoService(sortearClima(gerador), Clock.systemDefaultZone());
        CalcDano calcDano = new CalcDano(gerador, gerador);
        RegistroBatalha registro = new RegistroBatalhaMemoria();
        RepositorioResultados repositorio = new RepositorioResultadosMemoria();

        boolean sair = false;

        while (!sair) {
            Pokemon pokemonInicial = escolherInicial(sc, gerarInicial);
            if (pokemonInicial == null) {
                sair = true;
                continue;
            }

            System.out.println("""
                    --- ESCOLHA O MODO DE JOGO ---
                    1. Batalha Selvagem
                    2. Torneio (4 ou 8 treinadores)
                    3. Sair
                    """);

            int modo = sc.nextInt();
            if (modo == 3){
                sair = true;
                continue;
            }
            System.out.println();

            System.out.println("Digite o nome do jogador: ");
            Treinador jogador = new Treinador(sc.next(), criarMochilaPadrao(), List.of(pokemonInicial));
            System.out.println();

            if (modo == 1){
                Treinador selvagem = new Treinador("Selvagem", criarMochilaPadrao(), List.of(gerarInicial.gerar()));
                Batalha bat = new Batalha(jogador, selvagem, servicoTerreno, calcDano, registro, repositorio, gerador, sc);
                bat.iniciarBatalha();
            } else if (modo == 2){
                executarTorneio(sc, jogador, gerador, gerarInicial, servicoTerreno, calcDano, registro, repositorio);
            }
            System.out.println();
        }
    }

    private static Pokemon escolherInicial(Scanner sc, GerarInicial gerarInicial){
        System.out.println("""
                --- ESCOLHA O SEU POKÉSAL INICIAL ---
                1. BulbaSal (Planta)
                2. CharSal (Fogo)
                3. SquirtSal (Água)
                4. ChikoSal (Planta)
                5. CyndaSal (Fogo)
                6. TotoSal (Água)
                7. PikaSal (Elétrico)
                8. Procurar na Grama Alta (Aleatório)
                9. Sair
                """);
        System.out.println();

        return switch (sc.nextInt()) {
            case 1 -> new BulbaSal();
            case 2 -> new CharSal();
            case 3 -> new SquirtSal();
            case 4 -> new ChikoSal();
            case 5 -> new CyndaSal();
            case 6 -> new TotoSal();
            case 7 -> new PikaSal();
            case 8 -> {
                System.out.println("Explorando a grama alta...");
                Pokemon aleatorio = gerarInicial.gerar();
                System.out.println("Boa! " + aleatorio.getNome() + " foi capturado!");
                yield aleatorio;
            }
            default -> null;

        };

    }

    private static void executarTorneio(Scanner sc, Treinador jogador, GeradorService gerador,
                                        GerarInicial gerarInicial, ServicoTerreno servicoTerreno,
                                        CalcDano calcDano, RegistroBatalha registro,
                                        RepositorioResultados repositorio) {
        System.out.println("""
                --- ESCOLHA O TAMANHO DO TORNEIO --- "
                - 4 Participantes
                - 8 Participantes
                """);
        int qtd = sc.nextInt();

        GerarTreinador gerarTreinador = new GerarTreinador(gerador, gerarInicial);
        List<Treinador> participantes = new ArrayList<>();
        participantes.add(jogador);

        for (int i = 1; i < qtd; i++) {
            participantes.add(gerarTreinador.gerar(criarMochilaPadrao()));
        }

        ExecutorBatalha executor = (t1, t2) ->
                new Batalha(t1, t2, servicoTerreno, calcDano, registro, repositorio, gerador, sc, false)
                        .iniciarBatalha();

        Torneio torneio = new Torneio(participantes, executor, repositorio);
        Treinador campeao = torneio.iniciarTorneio();

        System.out.println("\n=== CAMPEÃO DO TORNEIO: " + campeao.nome() + " ===");
        repositorio.exibirResultados();
        System.out.println();
    }

    private static Mochila criarMochilaPadrao(){
        Mochila mochila = new Mochila();
        mochila.adicionarItem(new Pocao("Poção"));
        mochila.adicionarItem(new CuraStatus("Antídoto", EfeitoStatus.ENVENENADO));
        mochila.adicionarItem(new CuraStatus("Antiparalisia", EfeitoStatus.PARALISADO));
        mochila.adicionarItem(new CuraStatus("Antiqueimadura", EfeitoStatus.QUEIMADO));
        return mochila;
    }

    private static Clima sortearClima(GeradorInteiroAleatorio gerador) {
        Clima[] climas = Clima.values();
        return climas[gerador.gerarInt(climas.length)];
    }
}
