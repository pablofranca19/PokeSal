# Checklist de Teste Estático de Código — PokéSal

## 1. Todas as variáveis estão inicializadas antes do uso?
**Sim.** Todos os campos são inicializados nos construtores; variáveis locais são sempre atribuídas antes de qualquer leitura.

## 2. Há variáveis declaradas e nunca usadas?
**Não.** Todas as variáveis declaradas são utilizadas pelo menos uma vez no código.
 
## 3. Existe código inacessível?
**Não.** Não existe nenhum código "inútil" ou inutilizado por inacessibilidade.

## 4. Há código duplicado?
**Sim.**
- `Terreno.curaTerreno(Inicial)`: checa `this == CANTEIRO`, redundante com a mesma verificação já feita em `Batalha` antes de chamar o método.
- `Terreno.curaTerreno(Inicial)`: aplica a cura duas vezes (`curar(int)` seguido de `setHpAtual(...)` somando o mesmo valor).

## 5. Existem erros de sintaxe?
**Não.** O projeto compila corretamente.

## 6. Há erros de lógica que quebram regras do negócio?
**Sim.** A cura duplicada citada no item 4 faz o Canteiro Central curar o dobro do valor especificado (5% do HP máximo).

## 7. Há erros de tipagem?
**Não.**

## 8. O fluxo de controle é válido (sem loops infinitos, condições impossíveis)?
**Sim.** Todos os laços têm condições de parada alcançáveis; nenhuma condição logicamente impossível foi encontrada.

## 9. Outros erros identificados?
- `CalcDano`: usa um campo estático (`foiCrit`) para comunicar se houve crítico — cria estado compartilhado entre chamadas, o que pode gerar inconsistência em testes JUnit que chamem `calcularDano(...)` várias vezes em sequência.

