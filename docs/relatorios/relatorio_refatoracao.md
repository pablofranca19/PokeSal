
Link do diagrama refatorado: https://mermaid.ai/app/projects/a898962b-63f9-4dd2-8f40-c2e35f8474e3/diagrams/f7a0dde8-053a-48cb-8aed-314523bcfabd/share/invite/eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkb2N1bWVudElEIjoiZjdhMGRkZTgtMDUzYS00OGNiLThhZWQtMzE0NTIzYmNmYWJkIiwiYWNjZXNzIjoiRWRpdCIsInB1cnBvc2UiOiJzaGFyZS1pbnZpdGUiLCJpYXQiOjE3OTQwNjA3MjgsImV4cCI6MTc5NjY1MjcyOH0.M_RFSjRyxn1AwenMPBmrUcwGvl1MeDuP2yFlxBpmodA?entryPoint=share-modal

## Explicação geral

### Quais classes foram refatoradas?

- Inicial -> Pokemon (abstrato)
- Ataque -> Agrega GeradorService para geração de números aleatórios através de uma interface, e também possui TerrenoService, que gera novos terrenos de acordo com as condições impostas pelos requisitos.
- Terreno -> Vira abstrato: possui um campo de nome e métodos que são implementados pelas filhas Asfalto, Poca e Canteiro.
- EfeitoStatus -> O requisito autoral adiciona mais um efeito: ELETRECUTADO.
- Tipo -> Adição de mais um tipo elementar de PokeSal: o tipo ELÉTRICO.

### Adição de novas classes e princípios SOLID

- Devido ao alto acoplamento no código, muitas implementações causariam um problema em várias classes que possuem uma alta dependência do outro código, para isso, foi implementado alguns padrões de projeto para que seja mais fácil incrementar novas regras de negócio e manutenção da lógica existente.

#### Princípio da Inversão de Dependência

- Como citado nos requisitos e também por verificação por parte da equipe, para solucionarmos o problema do alto acoplamento nas classes de utilidades (como por exemplo, *CalcDano* que instanciava um *new Random()* para gerar um número aleatório) foram criados classes de serviço que auxiliam essas outras classes através da agregação.

| Classe         | Método        | Responsabilidade                                                                                            |
| -------------- | ------------- | ----------------------------------------------------------------------------------------------------------- |
| GeradorService | gerarInt()    | Tem como finalidade gerar números inteiros a serem implementados pelo programador, seja com bounds e seeds. |
| GeradorService | gerarDouble() | Gera números que possuem ponto flutuante, representado por casas decimais.                                  |

#### Princípio da Responsabilidade Única e Princípio Aberto/Fechado

- Criação de *AbstractFactories* para PokeSal e Terreno

OBS: Pelo fato dos terrenos terem uma lógica de clima e horário (definido por requisitos) foi criado um *TerrenoService* para as classes utilizarem como dependência.

- As *Factories* permitem separar a lógica de criação da manutenção dos objetos, fazendo com que as classes que a implementem nem conheçam o que está por trás da criação. Apenas passa o que quer, e já é construído.
- Além disso, permite que novos PokeSal que implementem a interface sejam criados sem alterar a lógica existente de criação em outras classes.
