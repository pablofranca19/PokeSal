# Relatório de Contribuição Individual — Fase 02
 Luis Henrique Lourenço das Mercês

---

## 1. Atividades realizadas

| Atividade | Descrição |
|---|---|
| Estudo do código-fonte | Análise das classes de produção envolvidas na lógica de batalha (`CalcDano`, `Terreno`, `Batalha`, `Mochila`, `Inicial`, `Ataque`, `Buff`) para entender as regras de negócio antes de escrever os testes. |
| Implementação da suíte JUnit | Criação de 5 classes de teste em `src/test/java`, cobrindo os 5 testes obrigatórios exigidos no enunciado e os 4 requisitos autorais definidos pela equipe. |
| Validação real dos testes | Configuração de um ambiente de execução (JDK 25 + JUnit Platform Console Standalone) para compilar e **rodar de fato** a suíte, em vez de apenas escrever o código sem verificar — garantindo que os 20 testes realmente passam. |
| Identificação de bugs | Durante a validação dos testes de valores-limite, foram identificados 2 bugs reais no código de produção (detalhados na seção 3). Por decisão da equipe, o código de produção **não foi alterado** nesta fase — os bugs foram documentados nos testes e no relatório. |
| Documentação | Elaboração do Relatório de Testes (`Relatorio_de_Testes_Fase02.md`), incluindo matriz de rastreabilidade dos testes obrigatórios e registro dos bugs encontrados. |

---

## 2. Testes implementados

**Classes de teste criadas:**
- `CalcDanoTest` — `testVantagemElemental()`, `testCalculoDanoBoundaryValues()`, `testBugDefZeroCausaDanoAbsurdo()`
- `TerrenoTest` — `testEfeitoTerrenoEstacionamentoUCSal()`
- `BatalhaOrdemTest` — `testOrdemDeAtaquePorVelocidade()`
- `MochilaTest` — `testUsoLimiteDeItensExcedido()`
- `RequisitosAutoraisTest` — testes dos 4 requisitos autorais da equipe:
  1. Dano crítico (1/16 de chance, dobro de dano)
  2. STAB — bônus de 50% quando o ataque é do mesmo tipo do PokéSal
  3. Buffs/debuffs válidos apenas na batalha ativa
  4. Ataques que alteram o terreno/clima (ex.: Dança da Chuva, Dia Ensolarado)

**Resultado da execução:** 20/20 testes passando (0 falhas).

---

## 3. Bugs encontrados durante os testes (não corrigidos nesta fase)

1. **Cura duplicada do terreno CANTEIRO** (`Terreno.curaTerreno`): a cura de 5% do HP máximo é aplicada em dobro (10% na prática), pois o método soma o valor de cura duas vezes.
2. **Defesa efetiva igual a 0 gera dano absurdo** (`CalcDano.calcularDano`): quando a DEF do alvo chega a 0 (base baixa + debuff no estágio mínimo), a divisão por zero gera `Infinity`, que ao ser convertido para `int` produz `Integer.MAX_VALUE` — um golpe fraco qualquer nocauteia o alvo.

Ambos os bugs foram comprovados por testes automatizados (que documentam o comportamento atual, com o bug) e ficam registrados para a equipe decidir se corrige em uma próxima fase.
