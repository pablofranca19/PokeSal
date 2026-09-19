# Teste Estático de Requisitos

**Autor:** Pablo Melo Franca Filho
**Auxílio:** Luis Henrique Lourenço das Mercês

---

Fazendo uma analogia de que o PokeSal é uma paródia aos jogos MMORPG de Pokemon, podemos considerar alguns detalhes que foram omitidos durante a elaboração dos requisitos apresentados.

## Relatório Geral

### Mistura entre RF, RNF e RN

No texto descritivo, atributos relacionados à funcionalidades do jogo, não-funcionalidades (comportamentos) e características que regem o sistema (regras de negócio) estão dentro de uma única seção de regra de negócio: separar o que cada coisa faz, facilitará o desenvolvedor de desacoplar elementos do sistema.

### Omissão da criação personalizada de um treinador

O descritivo não informa se o jogador pode criar um personagem (treinador) próprio, com características próprias e um nome próprio, o que é comum em jogos de Pokemon. Ou se todos os jogadores jogam com um único genérico ao decorrer de toda a história.

### Possibilidade de ter mais Pokemons

O texto fala em ter a possibilidade de escolher um pokemon inicial, porém não diz em nenhum momento por exemplo, se pode ou não ter mais de um pokemon na mochila, ou então a possibilidade de trocar de pokemon também é omitido.

### Ambiguidade no sistema de torneio e batalhas

A proposta do jogo é um torneio com a presença de treinadores, entretanto, o texto omite quantos treinadores podem ter no torneio, como funciona a dinâmica de avanço de fases para vencer esse torneio, e quantos treinadores participam de uma batalha individual.

### Omissão em como a mochila funciona

O texto não possui um requisito informando que cada treinador têm a sua própria mochila, e o que pode ter nela, e até quanto (o máximo) de quantidade de um determinado item que pode ter nessa mochila.

### Questionamentos acerca dos efeitos de status

Como escrito no texto, a cada batalha, os efeitos de status são aplicados no final do turno. Porém, não é explícito se esses efeitos adquiridos em batalha são cumulativos ou presente apenas nas batalhas.

> **Exemplo:** um pokemon que enfrentou um outro pokemon que seu tipo elementar é planta e logo depois enfrentar um que o tipo elementar é fogo ficará queimado e envenenado ao mesmo tempo? Ou só é possível ter um status ativo por vez?

### Omissão do sistema de cura do pokemon

Mais uma vez, por analogia aos jogos de pokemon MMORPG, onde é presente uma estação de cura para os pokemons, o descritivo não informa se existe uma estação de cura. Muito menos a forma de como os pokemons se recuperam a cada batalha.

### Acerca dos efeitos no final de turno de batalha

O texto informa a existência dos efeitos para o final de turno, porém, não especifica o que cada efeito faz, tornando vago, visto que pode haver vários efeitos causados pelos tipos elementares de cada pokemon.

---

## Separação dos Requisitos

### Requisitos Funcionais

| ID | Descrição |
|------|-----------|
| RF-01 | O treinador só deve ser capaz de escolher 1 PokeSal inicial entre as opções permitidas |
| RF-02 | O sistema deve disponibilizar apenas os tipos Fogo, Água e Planta para escolha |

### Requisitos Não Funcionais

| ID | Descrição |
|------|-----------|
| RNF-01 | Todo PokeSal deve possuir os atributos base HP, ATK, DEF, SPD e tipoElemental |
| RNF-02 | Asfalto Quente (Dia): Aumenta o dano de golpes do tipo Fogo em 15% |
| RNF-03 | Poça de Chuva / Piso Escorregadio: Golpes de Água aplicam 10% adicionais de precisão ou dano |
| RNF-04 | Canteiro Central: pokesal do tipo Planta recuperam 5% do HP máximo ao final de cada turno |
| RNF-05 | Cada treinador pode usar no máximo 2 itens por batalha (ex: Potion, Super Potion, Antidote) |
| RNF-06 | Usar um item consome o turno do treinador |

### Regras de Negócio

| ID | Descrição |
|------|-----------|
| RN-01 | Fogo é super efetivo contra Planta (dano x2.0) e pouco efetivo contra Água (dano x0.5) |
| RN-02 | Água é super efetiva contra Fogo (dano x2.0) e pouca efetiva contra Planta (dano x0.5) |
| RN-03 | Planta é super efetiva contra Água (dano x2.0) e pouca efetiva contra Fogo (dano x0.5) |
| RN-04 | A ordem de ataque do turno é determinada estritamente pelo atributo SPD (Velocidade) |
