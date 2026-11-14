# Registro de prompts — PokeSal, Fase 03 (refatoração)

- **Ferramenta:** Claude (Anthropic), pelo chat do claude.ai
- **Escopo deste arquivo:** apenas esta conversa. Prompts de outros chats não estão aqui e precisam ser acrescentados à mão.
- **Como foi gerado:** transcrição das mensagens do usuário, na ordem em que foram enviadas. Conferir contra o histórico real antes de entregar.
- **Convenção:** o texto digitado pelo usuário está em citação, copiado como foi escrito. Entre colchetes, `[colado: ...]` descreve código ou documento colado, e `[anexo: ...]` descreve arquivo enviado.

## Resumo por tipo de pedido

| Tipo | Quantidade nesta conversa |
| --- | --- |
| Correção ortográfica de texto de documentação | **0** |
| Identificação de erros de sintaxe apontados pelo compilador | **0** (nenhuma saída de compilador foi enviada) |
| Planejamento e ordem do trabalho | vários (ex.: 1, 2, 11, 19, 27, 28, 31, 50) |
| Dúvidas conceituais (getClass, toString, record, sorteios, constantes) | vários (ex.: 8, 12, 16, 17, 18, 42) |
| Revisão de código escrito pelo usuário | maioria (ex.: 5 a 7, 9, 13 a 15, 23 a 26, 29, 33 a 49, 52, 53) |
| Decisões de design e aderência ao diagrama de classes | vários (ex.: 4, 21, 27, 28, 31) |

## Natureza da ajuda recebida (resumo)

- Na **primeira resposta**, a IA escreveu duas versões completas da classe `TerrenoService`.
- A partir da mensagem 2, a pedido do usuário ("nao me de o codigo feito, me ajude a escreve-lo"), a IA passou a orientar em passos, com exemplos em outros domínios (Cachorro, Biblioteca, lista de cores), sem entregar o código final do projeto. Mesmo assim, em vários passos indicou nomes de métodos e recursos da linguagem a usar (por exemplo `getSimpleName()`, `List.copyOf`, `Math.max`).
- A IA revisou o código escrito pelo usuário e apontou bugs, entre eles: crítico 2.0 em vez de 1.5; limite do sorteio de 1 a 100; cura do `Canteiro`; bônus de terreno aplicado a qualquer tipo; texto corrompido no golpe do `ChikoSal`; `!` invertendo a lógica de `podeAgir`; `(int)` no lugar errado no sorteio do golpe; ordem dos registros no `RegistroBatalha`.
- A IA também listou inconsistências entre o diagrama de classes e o enunciado.
- **Requisitos autorais:** nenhum prompt pediu criação ou concepção dos requisitos autorais. Na mensagem 1 o usuário diz que a equipe já os criou, e na mensagem 51 que o requisito "explorar o mato" já está pronto.

## Prompts em ordem

### 1
> bom, ta na hora da segunda fase do projeto, aqui está o documento dizendo o que a gente tem que fazer:

`[colado: enunciado da Fase 03 do professor]`

> 1. eu fiquei com a parte de refatoração do código. tenho que fazer de acordo com o novo diagrama de classes (anexo)
> 2. os requisitos autorais já foram criados por nós: novo tipo de pokémon: elétrico. Pokémon: PikaSal; agora o player tem a opção de procurar um pokésal incial, podendo achar ele no mato alto (metaforico apenas) e ganhando um aleatório.
> 3. por favor me ajude

`[colado: classe TerrenoService]`

> como ficaria aqui?
>
> a regra é:
> 12 am - 8 am: asfalto
> 8 am - 16 pm: canteiro
> 16 pm - 12 am: poca

`[anexo: PDF do diagrama de classes]`

### 2
> vamos pular essa parte por agora e vamos para a próxima.
> eu mandei o arquivo do codigo.
> nao me de o codigo feito, me ajude a escreve-lo.

`[anexo: PokeSal-main.zip]`

### 3
> desculpa, mandei o arquivo errado, ta ai

`[anexo: novo_PokeSal1.zip]`

### 4
> nao sei qual seria o melhor, acho que o terreno mesmo

### 5
`[colado: classes ChikoSal, CharSal e SquirtSal, sem texto]`

### 6
> consertei poca e asfalto. tirei os magic numbers (lembre-se disso, magic numbers, code smells e coisas parecidas precisam ser evitados!!!)
>
> agora canteiro fica dificil, ja que o argumento so aceita int, nao double

`[colado: classes Poca, Asfalto e Canteiro]`

### 7
`[colado: classe Canteiro]`

> tá, preciso de ajuda na 3 e 4

### 8
> foi mal, eu nao entendi a parte do getClass(), como utilizar?

### 9
`[colado: trecho de Batalha com a comparação de terrenos por getClass]`

> assim?

### 10
> primeiro, tem certeza que ta atendendo todas as exigências do sonarqube, checkstyle, etc.? é necessario que sim, então quero que vc verifique isso em todo passo.
> segundo, como roda esse mvn compile?

### 11
> vamos refatorar o código primeiro, depois resolvemos isso. mantenha em mente que é necessario já refatorar com sonarqube, checkstyle, etc.

### 12
> ja fiz todos, menos o numero 2, nao entendi NADA

### 13
`[colado: classe Terreno, sem texto]`

### 14
`[colado: GeradorService, primeira versão, sem texto]`

### 15
`[colado: GeradorService, segunda versão, sem texto]`

### 16
> como que eu faria entao pra ir de 1 a 100 nesse caso?

### 17
> eu nao sou responsavel pelo javadoc nao po. pergunta: esse + 1 no nextInt() nao seria um magic number tbm?

`[colado: GeradorService, CalcDano e Ataque]`

### 18
> uma pergunta: por que vc utiliza essa convencao de criar uma constante apenas e comparar se o gerarDouble() é menor que ela?

### 19
> na real, vamos deixar pra resolver os code smells pro final, agora vamos focar em fazer o código certo.
> a coisa que eu percebi é que ainda nao temos a precisão dos ataques

`[colado: classe Ataque]`

> precisamos consertar isso

### 20
`[colado: classe Ataque]`

> nao entendi nada, sinceramente, me ajude e me explique como se eu fosse um primata, passo a passo

### 21
`[colado: classe Ataque]`

> um negocio que eu percebi é que nao faz sentido ter buff pro alvo pq todo ataque que causa efeito pro alvo é um nerf

### 22
> ja resolvi todos. pra sua informação, sou fascinado em pokémon

### 23
`[colado: classe CalcDano]`

> fiquei empacado

### 24
`[colado: classe CalcDano, sem texto]`

### 25
`[colado: classe CalcDano, sem texto]`

### 26
`[colado: classe CalcDano]`

> ps: gostei da piadinha kkkkk

### 27
> uma outra coisa, bem grande na real.
> eu PRECISO seguir o diagrama de classes, não posso apenas inventar e botar no codigo. me ajude, mas seguindo o diagrama

### 28
> vamos fazer o seguinte:
> já falei com o cara que fez o diagrama, vou tentar resolver as inconsistências com ele, mas, por enquanto, vamos tentar refatorar o QUE PODE ser refatorado, seguindo o diagrama e sem contradizer nada

### 29
`[colado: PokemonAbstractFactory e BulbaSalFactory]`

> deveria ficar assim né?

### 30
> feito, próximo

### 31
> ok, falei com o cara e ele concordou comigo e disse que posso mudar as coisas necessarias para o melhor funcionamento do código e que ele só mudará o diagrama depois.
> passe cada uma das mudanças necessarias até agora

### 32
> vamos com calma, a partir do numero 2 que foi onde eu empaquei

### 33
`[colado: classe Pokemon, sem texto]`

### 34
> próximo item

### 35
`[colado: classe Ataque, sem texto]`

### 36
> proximo

### 37
`[colado: classe Treinador, sem texto]`

### 38
> vamos começar

### 39
`[colado: classe RegistroBatalha, sem texto]`

### 40
> certo, próximo ponto

### 41
`[colado: RegistroBatalha, ResultadoBatalha e RepositorioResultados, sem texto]`

### 42
> como fazer uma classe record?

### 43
`[colado: ResultadoBatalha como record]`

> agora, nao sei como consertar RepositorioResultados

### 44
`[colado: ResultadoBatalha e RepositorioResultados, sem texto]`

### 45
`[colado: ResultadoBatalha com construtor compacto]`

> me diga algo, qual vc acha que está o nosso progresso, em porcentagem. considerando que seria ate o fim da minha parte

### 46
`[colado: classe Batalha, sem texto]`

### 47
`[colado: campos e construtor da Batalha, sem texto]`

### 48
`[colado: classe Batalha]`

> empaquei na 3 acho

### 49
`[colado: trecho do sorteio do golpe do oponente, sem texto]`

### 50
> podemos seguir, mas antes quero te informar de algo:
> há outro requisito funcional a ser implementado: a possibilidade de "explorar o mato" e receber um pokémon aleatorio. vc acha que a gente deveria focar em refatorar o código agora e implementar isso depois ou vice-versa?

### 51
> certo, o requisito já tá pronto, não preciso de nada seu além da ajuda.
> próximo

### 52
> tentei inserir já os registradores

`[colado: trechos de Batalha com as chamadas ao RegistroBatalha]`

### 53
`[colado: métodos executarAtaque, executarTurnoJogador e executarTurnoOponente, sem texto]`

### 54
> gere um arquivo markdown que armazena todos os meus prompts de correção ortográfica de documentação de texto e identificação de erros sintáticos apontados pelo compilador.

## Pendências antes de entregar

- Acrescentar os prompts de **outros chats** usados no projeto, se houver.
- Conferir a lista contra o histórico real da conversa.
- Preencher a data de cada uso e a versão do modelo, se o professor exigir.
- Manter o `AI_DECLARATION.md` coerente com este registro.
