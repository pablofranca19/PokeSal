# PokeSal

A Java command-line Pokémon-inspired game. Choose a PokéSal, battle a wild opponent, or compete in a tournament.

## Requirements

- JDK 21 or later
- Maven 3.8 or later

Check your setup:

```bash
java -version
mvn -version
```

## Build and run

Clone the repository and enter the Maven project directory:

```bash
git clone https://github.com/pablofranca19/PokeSal.git
cd PokeSal/PokeSal-refactor
```

Build the executable JAR:

```bash
mvn clean package
```

Start the game:

```bash
java -jar target/pokesal-1.0-SNAPSHOT.jar
```

The Maven Shade Plugin packages the application and its runtime dependencies into the executable JAR. To run the tests instead, use `mvn test`.

## What changed in this version

The original implementation was highly coupled. The refactor applies SOLID principles to give classes clearer responsibilities, improve cohesion, and reduce coupling. This makes the code easier to test and extend.

Two design patterns support the terrain system:

- **Abstract Factory** creates terrain objects through a common factory interface.
- **Strategy** encapsulates terrain-selection behavior behind `ServicoTerreno`; `TerrenoService` provides the selection implementation, based on climate and time, and uses the factories to create the terrain.

## Game modes

- **Wild battle:** choose a starter and battle a randomly generated opponent.
- **Tournament:** compete in a tournament with 4 or 8 trainers.

The starter choices include six original PokéSal, PikaSal, and a random choice from tall grass. Battles include attacks, items, status effects, terrain, and turn-based play.

## Project structure

```text
PokeSal/
├── README.md
├── docs/                         # Project reports, requirements, and diagrams
└── PokeSal-refactor/             # Maven project
    ├── pom.xml
    └── src/
        ├── main/java/
        │   ├── application/      # CLI entry point
        │   ├── models/           # Pokémon, trainers, terrains, and enums
        │   │   ├── pokesal/      # PokéSal classes and factories
        │   │   └── terrains/     # Terrain types and Abstract Factories
        │   ├── services/         # Game services, including TerrenoService
        │   └── utils/            # Battles, damage, items, and tournaments
        └── test/java/            # Unit and Mockito tests
```

Build output and the executable artifact are generated under `PokeSal-refactor/target/` and are not source files.

## Main components

| Area | Responsibility |
|---|---|
| `application.Main` | Starts the CLI, collects choices, and launches game modes |
| `models` | Represents Pokémon, trainers, terrain, and game enums |
| `services` | Provides game services, including terrain selection and random generation |
| `utils.Batalha` | Runs battles and coordinates turns and actions |
| `utils.CalcDano` | Calculates battle damage |
| `utils.Torneio` | Runs tournaments |
| `src/test/java` | Tests game behavior and service implementations |

## License

See the repository for licensing and project information.
