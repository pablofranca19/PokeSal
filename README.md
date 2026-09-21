# PokeSal

## What is "PokeSal"?

- Welcome to your brand new adventure! PokeSal is a Java-based game where you can have a pokemon trainer experience using your own CLI.

 # PokeSal Documentation

## 1. Requirements

The code uses recent Java features:

- `IO.println(...)`;
- simplified `main` method (`void main()`);
- `Math.clamp(...)`;
- `switch` expressions and text blocks.

**JDK 25 or higher** is recommended.

Check the installed version:

```bash
java -version
javac -version
```

If the commands don't exist, install a JDK. It's important to install the **JDK**, not just the JRE, since the `javac` command is required to compile the project.

---

# 2. How to run from the terminal

## Linux and macOS

Clone the project:

```bash
git clone https://github.com/pablofranca19/PokeSal.git
cd PokeSal
```

Compile the `.java` files:

```bash
mkdir -p out
javac -d out PokeSal/src/*.java
```

Run the game:

```bash
java -cp out Main
```

## Windows PowerShell

```powershell
git clone https://github.com/pablofranca19/PokeSal.git
cd PokeSal
mkdir out
javac -d out PokeSal/src/*.java
java -cp out Main
```

The main class is `Main.java`, located at:

```text
PokeSal/src/Main.java
```

Compiled files will be placed in the `out` folder.

To run again after changing the code:

```bash
javac -d out PokeSal/src/*.java
java -cp out Main
```

The `out` directory is already ignored by `.gitignore`.

---

## 3. Project structure

```text
PokeSal/
├── README.md
├── docs/
└── PokeSal/
    ├── .idea/
    ├── PokeSal.iml
    └── src/
        ├── Main.java
        ├── Batalha.java
        ├── Inicial.java
        ├── Ataque.java
        ├── CalcDano.java
        ├── Terreno.java
        ├── Mochila.java
        ├── Item.java
        ├── Pocao.java
        ├── CuraStatus.java
        ├── Buff.java
        ├── Tipo.java
        ├── EfeitoStatus.java
        └── PokéSal classes
```

Main responsibilities:

| Class | Responsibility |
|---|---|
| `Main` | Controls the main menu and starts new battles |
| `Batalha` (Battle) | Controls turns, actions, attacks, and battle end |
| `Inicial` (Starter) | Base class for Pokémon |
| `Ataque` (Attack) | Represents moves and their effects |
| `CalcDano` (DamageCalc) | Calculates effectiveness, critical hits, STAB, and damage |
| `Terreno` (Terrain) | Defines the current terrain and its modifiers |
| `GerarInicial` (GenerateStarter) | Randomly generates the opponent |
| `GerarTerreno` (GenerateTerrain) | Randomly generates the initial terrain |
| `Mochila` (Bag) | Controls available items and usage limit |
| `Item` | Base class for items |
| `Pocao` (Potion) | Restores HP |
| `CuraStatus` (StatusHeal) | Removes a specific status effect |
| `Buff` | Represents changes to attack, defense, and speed |
| `Tipo` (Type) | Enumeration of Pokémon and attack types |
| `EfeitoStatus` (StatusEffect) | Enumeration of status effects |

---

# 4. Main game flow

When the program starts:

1. The player chooses a starter PokéSal.
2. The player can confirm or cancel the start of the battle.
3. The opponent is chosen randomly.
4. The initial terrain is chosen randomly.
5. The player receives a bag with four items:
   - Potion;
   - Antidote;
   - Burn Heal;
   - Paralyze Heal.
6. The battle begins.
7. At the end of the battle, the program returns to the main menu.
8. The player can start another battle or choose to exit.

---

# 5. Choosing the starter PokéSal

The player can choose from six characters:

| Option | PokéSal | Type | HP | Attack | Defense | Speed |
|---:|---|---|---:|---:|---:|---:|
| 1 | BulbaSal | Grass | 45 | 49 | 49 | 45 |
| 2 | CharSal | Fire | 39 | 52 | 43 | 65 |
| 3 | SquirtSal | Water | 44 | 48 | 65 | 43 |
| 4 | ChikoSal | Grass | 45 | 49 | 65 | 45 |
| 5 | CyndaSal | Fire | 39 | 52 | 43 | 65 |
| 6 | TotoSal | Water | 50 | 65 | 64 | 43 |

Stats are set at the moment the PokéSal is created and can be temporarily modified by buffs and debuffs during battle.

---

# 6. Battle rules

During the player's turn, there are four main actions:

```text
1. Fight
2. Bag
3. Inspect
4. Finish
```

## 6.1 Fight

When choosing `Fight`, the player views the moves available for the current Pokémon.

The player can:

- choose an attack;
- exit the attack menu without using the turn.

If the Pokémon is paralyzed, there is a 25% chance it will fail to act.

## 6.2 Bag

The player can use an item during the turn.

Using an item consumes the turn, even when the item has no effect.

Examples:

- using a Potion at full HP;
- using an Antidote when the Pokémon is not poisoned.

The bag allows a maximum of **two item uses per battle**.

When the limit is reached, the game displays:

```text
You can only use 2 items per battle!
```

The item counter resets at the end of the battle.

## 6.3 Inspect

The inspect option allows checking:

- the player's current HP;
- the opponent's current HP.

Inspecting does not consume the turn.

## 6.4 Finish

The `Finish` option immediately ends the battle and displays:

```text
Successfully escaped.
```

---

# 7. Turn order

When both Pokémon choose an attack:

1. The Pokémon with higher speed attacks first.
2. In case of a tie, the player attacks first.
3. If the first attack defeats the opponent, the second attack is not executed.
4. If the opponent is still alive, it executes its chosen attack.
5. After the attacks, burn and poison damage are applied.
6. Then, the terrain may apply its healing effects.

If only one side has selected an attack, only that attack is executed.

---

# 8. Damage rules

Damage is calculated considering:

- a fixed level of `5`;
- the attacker's attack stat;
- the target's defense stat;
- the move's power;
- type effectiveness;
- terrain modifier;
- critical hit chance;
- STAB bonus.

The implemented formula is equivalent to:

```text
base damage =
(((2 × level + 10) / 250)
 × (attacker's attack / target's defense)
 × move power
 + 2)
```

Then, the result is multiplied by:

```text
type effectiveness
× terrain modifier
× critical multiplier
× STAB multiplier
```

The final result is converted to an integer.

## 8.1 Type effectiveness

| Attack | Target | Multiplier |
|---|---|---:|
| Grass | Water | 2.0 |
| Grass | Fire | 0.5 |
| Fire | Grass | 2.0 |
| Fire | Water | 0.5 |
| Water | Fire | 2.0 |
| Water | Grass | 0.5 |
| Other cases | Any target | 1.0 |

When the multiplier is `2.0`, the game displays:

```text
It's super effective!
```

When the multiplier is `0.5`, it displays:

```text
It's not very effective...
```

## 8.2 STAB

STAB stands for *Same Type Attack Bonus*.

When the attack's type matches the attacker's type, the damage receives a multiplier of:

```text
1.5x
```

## 8.3 Critical hit

Each attack has a chance of:

```text
1 in 16
```

of being a critical hit.

A critical hit receives a multiplier of:

```text
2.0x
```

When it occurs, the game displays:

```text
It was a critical hit!
```

---

# 9. Status effects

The project has four states:

```text
NONE
BURNED
PARALYZED
POISONED
```

A Pokémon can only have one status effect at a time. If it already has a status, another effect cannot be applied.

## 9.1 Burned

Effects:

- reduces the Pokémon's attack by half;
- causes damage at the end of the turn;
- the damage is equivalent to:

```text
Max HP / 16
```

## 9.2 Paralyzed

Effect:

- there is a 25% chance the Pokémon will fail to act during the turn.

The game displays:

```text
is paralyzed! It can't move!
```

## 9.3 Poisoned

Causes damage at the end of the turn:

```text
Max HP / 8
```

## 9.4 Status healing

Status-healing items only remove the corresponding effect:

| Item | Status removed |
|---|---|
| Antidote | Poisoned |
| Paralyze Heal | Paralyzed |
| Burn Heal | Burned |

If the status doesn't match the item, the game displays:

```text
There will be no effect.
```

---

# 10. Terrains

The initial terrain is randomly generated among:

```text
ASPHALT
PUDDLE
FLOWERBED
```

The terrain can be changed by specific attacks.

## 10.1 Asphalt

Fire-type attacks receive a bonus of:

```text
1.15x
```

## 10.2 Puddle

Water-type attacks receive a bonus of:

```text
1.10x
```

## 10.3 Flowerbed

At the end of the turn, Grass-type Pokémon recover:

```text
5% of max HP
```

The heal does not exceed max HP.

## 10.4 Terrain change

Some attacks can change the terrain:

| Attack | New terrain |
|---|---|
| Sunny Day | Asphalt |
| Rain Dance | Puddle |
| Grassy Terrain | Flowerbed |

If an attack attempts to activate the terrain that is already active, the change fails.

---

# 11. Buffs and debuffs

Some attacks temporarily increase or reduce stats:

- attack;
- defense;
- speed.

Stat stages range between:

```text
-6 and +6
```

When a stat is already at `+6`, it cannot increase further.

When a stat is already at `-6`, it cannot decrease further.

Stage multipliers are calculated based on the current stage. For example:

- stage `0`: normal stat;
- positive stages: increase the stat;
- negative stages: reduce the stat.

---

# 12. PokéSal attacks

## BulbaSal

| Attack | Type | Power | Effect |
|---|---|---:|---|
| Tackle | Normal | 35 | Normal damage |
| Vine Whip | Grass | 35 | Grass-type damage |
| Poison Powder | Poison | 0 | Poisons with 100% chance |

## CharSal

| Attack | Type | Power | Effect |
|---|---|---:|---|
| Scratch | Normal | 40 | Normal damage |
| Ember | Fire | 40 | 10% chance to burn |
| Growl | Normal | 0 | Reduces the opponent's attack |
| Sunny Day | Fire | 0 | Changes terrain to Asphalt |

## SquirtSal

| Attack | Type | Power | Effect |
|---|---|---:|---|
| Tackle | Normal | 35 | Normal damage |
| Bubble | Water | 20 | Water-type damage |
| Withdraw | Water | 0 | Increases the user's defense |
| Rain Dance | Water | 0 | Changes terrain to Puddle |

## ChikoSal

| Attack | Type | Power | Effect |
|---|---|---:|---|
| Body Slam | Normal | 30 | 30% chance to paralyze |
| Razor Leaf | Grass | 55 | Grass-type damage |
| Poison Powder | Poison | 0 | Poisons with 100% chance |
| Grassy Terrain | Grass | 0 | Changes terrain to Flowerbed |

## CyndaSal

| Attack | Type | Power | Effect |
|---|---|---:|---|
| Tackle | Normal | 35 | Normal damage |
| Ember | Fire | 40 | 10% chance to burn |
| Flame Wheel | Fire | 60 | Increases the user's speed |

## TotoSal

| Attack | Type | Power | Effect |
|---|---|---:|---|
| Scratch | Normal | 40 | Normal damage |
| Water Gun | Water | 40 | Water-type damage |
| Leer | Normal | 0 | Reduces the opponent's defense |

---

# 13. Random opponent and terrain

For each new battle:

- the opponent is randomly chosen among the six PokéSal;
- the initial terrain is randomly chosen among the three terrains;
- the attacks used by the opponent are chosen randomly;
- critical hit, status, and paralysis chances are also random.

Because of this, two runs of the game can produce different battles.

---

# 14. Win and loss conditions

The battle ends when:

- the player's HP reaches `0`;
- the opponent's HP reaches `0`;
- the player chooses to finish the battle.

Possible messages:

```text
You won!
```

```text
You lost!
```

```text
Successfully escaped.
```

After the battle, the item usage limit is reset, and a new battle can be started from the main menu.

---

# 15. Troubleshooting

## Error: `javac: command not found`

The Java compiler is not installed or not configured in the `PATH`.

Check:

```bash
javac -version
```

Install a JDK and set up the `PATH` variable.

## Error related to `IO`

The project uses the `IO` API, available in recent Java versions. Use JDK 25 or higher.

## Error related to `Math.clamp`

Update the JDK to a version compatible with the project's code.

## Error `Could not find or load main class Main`

Check whether:

1. the files have been compiled;

2. the command was run from the repository root;

3. the `out` folder was used as the classpath:

```bash
java -cp out Main
```

## Compilation errors using an older version

The project has no backward-compatibility configuration for older Java versions. The recommended approach is to update the environment to JDK 25 or higher.
