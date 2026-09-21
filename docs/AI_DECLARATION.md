# Uso de inteligência artificial

Como previsto pela documentação do projeto, para quaisquer utilização de IA, deve-se colocar neste arquivo o txt do prompt.

## Prompts utilizados

### Prompt 1

- Formate este documento PDF e transforme-o em markdown. Além disso, estilize-o, sem alterar o conteúdo presente nele, para melhor visualização.

### Prompt 2

- Faça uma documentação das regras de negócio do projeto, e como rodar ele pelo terminal.

```
===============================================================================
```

`1. mostre os erros de sintaxe e compilação que tem` 

```
2. vamos consertar isso aqui, passo a passo
```

```
[código colado: classe Batalha]
```

```
3. quero adicionar os items e os efeitos deles
```

```
4. em questões de lógica DE PROGRAMAÇÃO APENAS, como eu faria tipo:
hp max = 40
hp atual = 15
se usar poção: hp atual = 35 (curou em 20)
...
```

```
hp max = 40
hp atual = 35
se usar poção: hp atual = 40 (curou em 5)
```

```
5. [código colado: Pocao.java]
dessa forma, então
```

```
6. existe outra forma de fazer isso? pois o Inicial não tem um set pra hpAtual,
somente um construtor que determina que o atual (quando começar a batalha) deve
ser igual ao hp máximo
```

```
7. deixei assim mesmo:
[código colado: Pocao.java e Inicial.java]
agora o seguinte: como não dá para usar itens em pokémons saudáveis, eu preciso
que o jogador receba uma mensagem caso ele tente
```

```
8. isso eu sei, porém, no fluxo de batalha (que não está terminado), o jogador
ainda poderia mudar a função no turno dele, ou ele acabaria perdendo?
```

```
[código colado: Batalha.java]
```

```
9. eu quero que ele não perca o turno, mas deixa pra lá isso por enquanto.
estou com dúvidas sobre como implementar essa parte aqui na parte dos itens:
[código colado: Mochila.java]
```

```
10. todas essas xd. porém tirei a parte de remover o item, não faz sentido
```

```
11. estava fazendo as outras classes de itens, porém todos os anti- são iguais,
eles só vão remover certo status do pokemon
```

```
12. criando uma classe comum para todos os anti-, onde eu crio os itens?
```

```
13. o método adicionar itens tem algo de errado
```

```
[código colado: Mochila.java]
```

```
14. [imagem anexada]
```

```
15. [imagem anexada] como eu deixo isso mais prático?
```

```
16. sim
```

- `[código colado: Batalha.java e Mochila.java]` 

```
17. adicionei o break no final do case 2.
```

```
qual vc acha que é a opção mais prática?
```

```
18. tem alguma coisa aí com o tal dos magic numbers que eu precisaria me
atentar?
```

```
19. por favor, nomeie todo pra mim
```

```
20. [código colado: CalcDano.java]
```

```
21. rapidão, tem alguma coisa aí com o tal dos magic numbers que eu precisaria
me atentar?
```

```
22. [código colado: CalcDano.java]
```

```
eu não sei o que fazer com os outros números da fórmula, eles são apenas números
estáticas
```

```
23. [código colado: CalcDano.java]
certo, sobre os magic numbers...
```

```
24. certo, o que falta?
```

```
25. sim, vamos focar nessa parte, mas apenas lembrando: na divisão do trabalho,
a minha única função é codar, então não precisa se preocupar com outras partes e
nem com a segunda fase
```

```
26. então, sobre a questão da lista e de escolher os ataques, como já vem a
lista inteira, não vai ter aquela estrutura enumerada que eu geralmente boto,
acho que vai ficar ruim se não
```

```
27. beleza, seguinte:
```

```
como a classe Batalha tem um método que cria as batalhas e tal, precisamos de um
método pra chamar na classe Main. porém, ainda precisamos de um pokémon pro
oponente, que não temos. acho que vale a tentativa de fazer um gerador aleatório
de de Iniciais. como exatamente?
```

```
28. ainda falta determinar o terreno, mas pensando nisso foi que eu bolei
algumas ideias, por favor, me confirme se OS REQUISITOS AUTORAIS QUE EU CRIEI
estão corretos
```

```
requisitos autorais pokésal:
```

`1. chance de dano crítico - cada ataque que dá dano tem uma chance de 6.25% (1/16) de ser um ataque crítico, que causa 2x o dano normal.` 

`2. STAB (same type attack bonus) - um pokémon que utiliza um ataque com a mesma tipagem que ele, recebe um bônus de dano de 1.5x` 

`3. movimentos settam o terreno - ataques como sunny day e rain dance criam os terrenos determinados, de acordo com a tipagem` 

`4. buffs e debuffs - ataques que não causam dano que aumentam ou diminuem algum atributo do jogador/oponente, como growl e leer.` 

```
29. aproveitei e já fiz o sistema de crítico e de STAB:
```

- `[código colado: CalcDano.java]` 

`30. não, você está correto, era isso mesmo` 

```
agora vamos pro próximo requisito
```

`31. o que vc achar melhor\` 

`32. [código colado: CalcDano.java]` 

`33. onde eu corrijo isso daq? public enum Terreno {` 

```
    ASFALTO,
    POCA,
    CANTEIRO;
```

```
    public double getMod(Tipo atk){
        if (atk == Tipo.FOGO && ASFALTO){
```

- `} } }` 

```
34. minha classe terreno ficou assim:
[código colado: Terreno.java]
adicionei esse método novo que acho que dá certo (o int é pq não tem porcentagem
na vida dos pokémons, são apenas números inteiros)
```

`35. [imagem anexada]` 

`36. [código colado: CalcDano.java]` 

`37. [código colado: CalcDano.java com erro de assinatura]` 

`38. onde eu colocaria essa linha de new Ataque` 

`39. já fiz [código colado: ChikoSal.java]` 

```
pegue o ChikoSal como exemplo
```

```
40. vamos recapitular para eu anotar aqui:
quais foram os requisitos autorais que eu criei até agora?
```

```
41. beleza, seguinte:
```

```
como a classe Batalha tem um método que cria as batalhas e tal, precisamos de um
método pra chamar na classe Main. porém, ainda precisamos de um pokémon pro
oponente, que não temos. acho que vale a tentativa de fazer um gerador aleatório
de de Iniciais. como exatamente?
```

```
42. ainda falta determinar o terreno, mas pensando nisso foi que eu bolei
algumas ideias, por favor, me confirme se OS REQUISITOS AUTORAIS QUE EU CRIEI
estão corretos
```

```
[repetição do prompt 28]
```

```
43. já consertei a parte do índice, porém sobre a o método curar, ao invés dessa
conta maluca aí, não poderia ser:
public void curar(int quant){
```

```
        int quantCura = Math.min(this.hpAtual + quant, hpMax);
```

```
        this.hpAtual = quantCura;
```

```
        IO.println(nome + " recuperou " + quantCura + "HP!");
    }
```

```
44. realmente, consertei e ainda botei pra mostrar o hp atual do bixo depois
próximo passo
```

# `45. sim` 

```
lembrando que o efeito de paralisia só conta pra ataques, como é o treinador que
comanda o pokémon, ele pode escolher atacar ou usar um item a vontade, sem ficar
paralisado
```

# `46. certo:` 

```
1. quantidades exatas:
```

`1. paralisia reduz a velocidade do pokémon pela metade e tem 25% de chance de impedir o pokémon de atacar` 

`2. queimadura reduz o ataque em 50% e dá 1/16 da vida total de dano no pokémon afetado` 

`3. envenenado dá 1/8 da vida total de dano no pokémon afetado` 

`2. os efeitos de status são permanentes até serem removidos com itens` 

`3. as reduções de atributos e chances de sofrer algo são EXCLUSIVAMENTE para os status ativos, ou seja, caso sejam curados, o pokémon volta a estaca zero` 

`47. [código colado: Inicial.java]` 

`48. revise, depois vamos integrar` 

# `49. sim` 

```
lembrando que o efeito de paralisia só conta pra ataques, como é o treinador que
comanda o pokémon, ele pode escolher atacar ou usar um item a vontade, sem ficar
paralisado
```

`50. [código colado: Batalha.java]` 

`51. antes de aplicar a cura do terreno, reveja isso:` 

`52. tirei a chave, fora isso, tem mais alguma coisa?` 

```
53. vamos seguir, mas só um adendo: é pq nos jogos de pokémon acontece algo
muito frustrante que é: ele deixa você escolher atacar, escolher o ataque e SÓ
depois ele falar que o pokémon esta paralisado, como se ele Tivesse ativamente
tentando te obedecer, mas não consegue por causa da paralisia, entendeu?
```

`54. sim` 

`55. [código colado: Batalha.java]` 

# `56. beleza,` 

```
como a classe Batalha tem um método que cria as batalhas e tal, precisamos de um
método pra chamar na classe Main. porém, ainda precisamos de um pokémon pro
oponente, que não temos. acho que vale a tentativa de fazer um gerador aleatório
de de Iniciais. como exatamente?
```

`57. quero por a lógica de não perder o turno` 

`58. calma, a parte dos ataques ainda não está completa` 

`59. então, já tá tudo basicamente definido:` 

```
1. são os ataques que causam o dano super efetivo ou não, não os pokémons em si
2. os efeitos de terreno não estão declarados ainda, porém irão influenciar no
desempenho do ataque
```

```
3. os ataques seguem uma fórmula específica que parece que deixa meio balanceado
[dano =(((2×Level+10)/250) × (Attack/Defense) × BasePower+2) x Modifier]
(modifier, nesse caso, sendo o terreno caso haja)
```

```
60. não pensei aonde botar ainda, queria que você me ajudasse nessa
quanto ao nível, estabeleci que todos serão do nível 5, já que esse é o nível
que os iniciais começam nos jogos
```

`61. pode ser a opção B mesmo` 

`62. 1. não sei xd` 

`2. acho melhor deixar o método terreno cuidando de tudo mesmo` 

`63. deixe eu ver o mapa` 

`64. como eu corrijo isso daq?` 

```
[código colado: Terreno.java com erro]
```

```
65. minha classe terreno ficou assim:
```

```
[código colado: Terreno.java]
```

```
adicionei esse método novo que acho que dá certo (o int é pq não tem porcentagem
na vida dos pokémons, são apenas números inteiros)
```

```
66. [imagem anexada]
```

```
67. [imagem anexada]
```

```
68. onde era pra deixar a matriz de vantagem mesmo?
```

```
69. rapidão, tem alguma coisa aí com o tal dos magic numbers que eu precisaria
me atentar?
```

```
70. por favor, nomeie todo pra mim
```

```
71. [código colado: CalcDano.java]
```

```
72. eu não sei o que fazer com os outros números da fórmula, eles são apenas
números estáticas
```

```
73. [código colado: CalcDano.java]
certo, sobre os magic numbers...
```

```
74. eu mudei a assinatura, está correto sem nenhum erro de compilação ou
sintaxe, porém sobre os magic numbers...
```

```
eu não sei o que fazer com os outros números da fórmula, eles são apenas números
estáticas
```

```
75. [código colado: CalcDano.java]
```

```
76. o que falta?
```

```
77. sim, vamos focar nessa parte, mas apenas lembrando: na divisão do trabalho,
a minha única função é codar, então não precisa se preocupar com outras partes e
nem com a segunda fase
```

```
78. então, sobre a questão da lista e de escolher os ataques, como já vem a
lista inteira, não vai ter aquela estrutura enumerada que eu geralmente boto,
acho que vai ficar ruim se não
```

```
79. vamos recapitular para eu anotar aqui:
quais foram os requisitos autorais que eu criei até agora?
```

```
80. beleza!
```

```
81. aproveitei e já fiz o sistema de crítico e de STAB:
[código colado: CalcDano.java]
```

```
82. não, você está correto, era isso mesmo
```

```
agora vamos pro próximo requisito
```

```
83. o que vc achar melhor\
```

```
84. [código colado: CalcDano.java]
```

```
85. onde eu corrijo isso daq?
```

```
[código colado: Terreno.java com erro]
```

```
86. minha classe terreno ficou assim:
```

```
[código colado: Terreno.java com curaTerreno]
```

```
87. [imagem anexada]
```

`88. [imagem anexada]` 

`89. onde era pra deixar a matriz de vantagem mesmo?` 

```
90. já fiz
[código colado: ChikoSal.java]
```

```
pegue o ChikoSal como exemplo
```

```
91. certo, seguinte:
como eu deixo isso mais prático?
```

```
92. sim
[código colado: Batalha.java]
```

```
93. adicionei o break no final do case 2.
qual vc acha que é a opção mais prática?
```

```
94. nos dois
```

```
95. [código colado: Batalha.java]
```

```
96. quero por a lógica de não perder o turno
```

```
97. calma, a parte dos ataques ainda não está completa
```

```
98. então, já tá tudo basicamente definido:
[repetição do prompt 59, fórmula de dano]
```

```
99. não pensei aonde botar ainda, queria que você me ajudasse nessa
quanto ao nível, estabeleci que todos serão do nível 5, já que esse é o nível
que os iniciais começam nos jogos
```

```
100. pode ser a opção B mesmo
```

```
101. 1. não sei xd
```

```
2. acho melhor deixar o método terreno cuidando de tudo mesmo
```

```
102. deixe eu ver o mapa
```

```
103. como eu corrijo isso daq?
[código colado: Terreno.java com erro]
```

```
104. minha classe terreno ficou assim:
[código colado: Terreno.java]
```

```
105. aproveitei e já fiz o sistema de crítico e de STAB:
[código colado: CalcDano.java]
```

```
106. eu quero que ele não perca o turno, mas deixa pra lá isso por enquanto.
[repetição do prompt 9]
```

```
107. sim, seguinte: eu já fiz os itens de cura de status, como eu adiciono eles
na mochila?
```

```
108. tem algo errado
```

```
[imagem anexada]
```

```
109. [imagem anexada] como eu deixo isso mais prático?
```

```
110. sim
[código colado: Batalha.java completo]
```

```
111. antes de aplicar a cura do terreno, reveja isso:
[código colado: Batalha.java]
```

```
112. tirei a chave, fora isso, tem mais alguma coisa?
```

```
113. vamos seguir, mas só um adendo: é pq nos jogos de pokémon...
[repetição do prompt 53]
```

```
114. sim
```

```
115. [código colado: Batalha.java]
```

```
116. ok, próximo
```

```
117. faz sentido sim, só um ponto:
no else if (opAtk == opSair), é meio redundante trocar turnoUsado pra false,
não? já que até lá, ele sempre vai continuar sendo falso
```

```
118. botar um break aí não ajudaria?
```

```
119. ok, próximo
```

```
120. beleza, agora vamos pro turno do oponente propriamente dito...
sim
```

```
121. mas e os textos, a lista de ataques que estao no case 1?
```

```
122. faz sim, só um ponto:
```

```
no else if (opAtk == opSair), é meio redundante trocar turnoUsado pra false,
não? já que até lá, ele sempre vai continuar sendo falso
```

```
===============================================================================
```

```
FIM DO REGISTRO
```

