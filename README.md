# Weaver Game by 202118010402
This is a word transformation game implemented based on Java, including two versions: graphical interface (GUI) and Command line (CLI).

## Game rules

1. The game starts with a starting word, and the goal is to convert it into the target word through a series of steps 
2. Only one letter can be changed at each step
3. Each intermediate step must be a valid word
4. All words must consist of four letters

## Functional characteristics

- It supports both GUI and CLI interfaces 
- Display the progress of word transformation in real time 
- Support virtual keyboard input
- Supports random word mode
- Offer game reset and new game features
  Clear visual feedback

## System Requirements

- Java 21
- Maven >= 3.6

## How to operate

1. Compilation project:

```bash
mvn clean package
```

2. Run the GUI version:

```bash
mvn javafx:run
```

3. Run the CLI version:

```bash
java -cp target/weaver-game-1.0-SNAPSHOT.jar com.weaver.cli.WeaverCliApplication
```

## Project structure

- `src/main/java/com/weaver/model` - The core logic of the game
- `src/main/java/com/weaver/view` - GUI interface component
- `src/main/java/com/weaver/controller` - User input processing
- `src/main/java/com/weaver/cli` - Command line interface
- `src/main/java/com/weaver/gui` - Graphical interface startup class

## Configuration

Game use `dictionary.txt` file is used as a word dictionary. Make sure the file is located in the running directory.
