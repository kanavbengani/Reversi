# Reversi
## Overview
This Reversi game project is a two-player game with a graphical interface. The goal is to implement
the classic Reversi game with a hexagonal grid. The codebase allows for human players to play
against each other or even the possibility of implementing AI players in future versions. For this
iteration of the homework, we have implemented a `Model`, a `View` and a `ReversiStrategy` pattern to develop certain
strategies for the game. We have also implemented a `Controller` that synchronizes the `Model` and the `View`.

## Extra Credit
- Strategy 2 -- See `strategy/AvoidEdgesStrategy.java`.
- Strategy 3 -- See `strategy/GoCornerStrategy.java`.
- Strategy 4 -- See `strategy/MinimaxStrategy.java` (one layer) and `strategy/MinimaxStrategyDepth.java` (recursion - 
can take in a depth greater than 1).
- Composing Strategies -- See `AndStrategy.java`. (Allows recombination easily and efficiently).
- Hints for which moves are valid. (Green for valid, red for invalid) -- See `screenshots/CellSelected.png`.
- Shows how many will be captured when cell is clicked -- See `screenshots/ExtraCreditHint.png`.

## Quick start
### Command Line Arguments
- "human" -- Allows you to create a human player
- "strategy1" -- Minimax
  - Required argument: `depth` -- Must be passed in as a number greater than 0 right after "strategy1".
- "strategy2" -- Go Corners Strategy (no additional arguments needed)
- "strategy3" -- Avoid Edges Strategy (no additional arguments needed)
- "strategy4" -- Capture Most Strategy (no additional arguments needed)

For all of the above strategies, refer to the `strategy` package.

Example usage: 
- `human human` -- Human (Black) v/s Human (White)
- `human strategy1 3` -- Human (Black) v/s Minimax w/ Depth 3 (White)
- `strategy4 human` -- Capture Most Strategy (Black) v/s Human (White)
- `strategy1 2 strategy1 4` -- Minimax w/ Depth 2 (Black) v/s Minimax w/ Depth 4 (White)

### How to interact with the game as a human
As a human, you can click on unoccupied cells, and they will be highlighted red (invalid move) or green (valid move) 
with the number of capturable pieces as a hint. You can use your keyboard to do certain actions such as passing 
(`p` key) or playing a move at the selected cell (`ENTER` key). To know when it is your turn, refer to the top of your 
view where it will prompt you whether it is your turn. 

## Changelog

### Changes for Part 2
The following are changes made to the `model` package from the previous assignment (Assignment 5). 
- Removed the concrete class of the read-only model since the concrete `Model` class was able to outline
all the read-only observation methods by simply implementing `IModel` (which in-turn extended `IROModel`). 
- Added an extra constructor that accepts a board (possibly in an intermediary state of the game) and sets the game
up with that board.
- Added a method called `copy()` to the `IROModel` interface, which returns a copy of a model instance. Due to this,
we also had to add a constructor that takes in certain parameters of the model, so a copy can be generated.
- Fixed the logic of validating a move since it failed to handle a specific corner case where a line of pieces of the
same color leading up to the edge of the board results in a valid move when it is not.
- Added a method called `getAllPosn()` to get all the possible positions on the board.
- Moved the `addListener()` method to the `IROModel` interface because the view needs to be able to call it, but it
only should have access to the read-only model. 
- Added a method called `getAllCapturedPieces()` which returns all the captured pieces if a given color places a move 
on the given axial coordinate. 
- Refactored the `PlayerListener` interface to the `ModelFeatures` interface in the `model` package as this interface 
could be implemented by a non-player. Also, these features are events that are triggered by the model, hence the 
refactoring of the package. 

### Changes for Part 3
The following are changes made to the code from the previous assignment (Assignment 6).
- Added a package-private method to the view to test the behavior of the mouse and the keyboard listeners.
- Added a `disableInput` method to the view to not allow any input on an AI's view.
- Removed old implementation of single-depth minimax and refactored recursive, multiple-depth minimax.
- Split notification of turn and prompting of player move in `ModelFeatures` and added a `itsGameOver` method.

## Invariant
- The number of key-value pairs in `board` is equal to `3 * numRings * (numRings + 1) + 1`. This is a class variant
because it is a true logical statement about the instantaneous state of the class. It is preserved in all constructors
since it is either validated if a board is passed in or the board is created based on the given number of rings. It is 
also preserved by all the methods because `numRings` is final and the board is never added or removed from.

## Code Explanations
- `ReversiFactory` -- A factory class used to configure and create a game of Reversi.

### Model
- `IROModel` -- A read-only interface that allows the user to make observations on the cs3500.reversi.model such as 
querying the game state.
- `IModel` -- A mutable interface that allows the user to make observations and operations on the model 
such as playing a move and switching turns.
- `Model` -- Concrete implementations of their `IModel` interface.
- `AxialPosn` -- An axial position represents the `q` and `r` coordinates in a hexagonal grid.
- `Direction` -- A direction represents the offset of each hexagonal cell.
- `ModelFeatures` -- A set of features that the model will be triggering to its listeners. All listeners to the model
should implement the above interface. 

**Coordinate System**

We decided to use the axial coordinate system, using `q` and `r` coordinates. The `q` represents
the horizontal axis of the hexagon. The `r` represents the top-left to bottom-right diagonal. The
center of the grid is the origin, or `(0, 0)`. We also use a direction enum which enumerates the
**6** neighbors of each cell according to the coordinate system. These include the following with
the given delta `q` and `r`: `UPLEFT` `(0, -1)`, `UPRIGHT` `(1, -1)`, `RIGHT` `(1, 0)`, 
`DOWNRIGHT` `(0, 1)`, `DOWNLEFT` `(-1, 1)`, `LEFT` `(-1, 0)`.

### Strategy
- `ReversiStrategy` -- A general interface for developing strategies for a game of Reversi.
- `AndStrategy` -- A class that composes two passed-in `ReversiStrategy` consecutively similar to an **AND** operation
rather than the conventional **OR-ELSE** operation used in the strategy pattern.
- `GoCornerStrategy` -- A class that prioritizes placing a move on a corner. Returns empty if no corner is valid. In 
case of a tie, the list that is being returned is sorted by topmost, leftmost.
- `AvoidEdgesStrategy` -- A class that prioritizes placing a move on a non-edge. Returns empty if no non-edges are 
valid. In case of a tie, the list that is being returned is sorted by topmost, leftmost. 
- `CaptureMostStrategy` -- A class that leverages a greedy approach for choosing a move that captures the most number of
pieces. In case of a tie, the list that is being returned is sorted by topmost, leftmost.
- `MinimaxStrategy` -- A class that finds a list of moves that minimize the chances of the opponent playing their best
move. A best move is calculated by the difference of scores between the two players. In case of a tie, the list that is 
being returned is sorted by topmost, leftmost.
  - `MinimaxStrategyDepth` -- Our implementation of Minimax also has a `depth` value taken in the constructor that 
  defines the search depth of the algorithm. In order make an implementation of Minimax that matches the specification 
  in the assignment, we can set the `depth` to be `1`. For now, passing moves are considered worse for an opponent than 
  playing (may be improved upon in later iterations). The best move for the opponent is a move that is a winning move or
  a move that leads to a winning outcome.

### View
- `TextualView` -- A light-weight text-based view to visualize the game of Reversi, mostly for testing purposes.
- `IView` -- A Swing-based graphical user interface for interacting with a Reversi model.

### Controller
- No interface is needed for the controller because there are no publicly facing methods in the Controller. Although
there is a logical interface for the controller which surrounds the implementations for `ModelFeatures` and
`PlayerFeatures`, there is no Java interface because the behavior of the above features classes cannot be promised
through an 'IController'. The above is also a case because of the way our controller is designed where it 'has-a' 
features rather than is one. 
- `Controller` -- A standalone class that is responsible for aggregating all the listeners to be used to allow the user 
to play the game or to set up an AI. The constructor of the controller adds these listeners to the `model`, `player`, 
and `view`.
- `ModelFeaturesImpl` -- Defines the actions to take when the model triggers certain events. 
- `PlayerFeatures` -- Defines the actions to take when the player/view triggers certain events.

### Player
- Defines the representation of a player in the game of Reversi
- `PlayerFeatures` -- A set of features that the player/view will be triggering to its listeners. 
All listeners to the view or player should implement the above interface.
- `HumanPlayer` -- Represents a human player. The `playAMove()` method does nothing since the view can emit that the 
user wants to play a move. This deals with the asynchronous nature of the human interaction. 
- `AIPlayer` -- Represents an AI player. The `playAMove()` method uses the given strategy to synchronously play a 
move. 
- As a result, the Player interface bridges the gap present between the asynchronous nature of the human interaction
and the synchronous nature of the AI interaction.

## Screenshots
| Initial State<br/><image src="./screenshots/InitialState.png">           | Cell Selected<br/><image src="./screenshots/CellSelected.png">        |
|--------------------------------------------------------------------------|-----------------------------------------------------------------------|
| Intermediary State<br/><image src="./screenshots/IntermediaryState.png"> | Extra Credit Hint<br/><image src="./screenshots/ExtraCreditHint.png"> |

## Source organization
```
├── README.md
├── Reversi.jar
├── strategy-transcript.txt
├── screenshots
│    ├── CellSelected.png
│    ├── ExtraCreditHint.png
│    ├── InitialState.png
│    └── IntermediaryState.png
├── src
│    └── cs3500
│         └── reversi
│              ├── Reversi.java
│              ├── ReversiFactory.java
│              ├── controller
│              │    ├── Controller.java
│              │    ├── ModelFeaturesImpl.java
│              │    └── PlayerFeaturesImpl.java
│              ├── model
│              │    ├── AxialPosn.java
│              │    ├── Direction.java
│              │    ├── IModel.java
│              │    ├── IROModel.java
│              │    ├── Model.java
│              │    ├── ModelFeatures.java
│              │    └── PieceColor.java
│              ├── player
│              │    ├── AIPlayer.java
│              │    ├── HumanPlayer.java
│              │    ├── Player.java
│              │    └── PlayerFeatures.java
│              ├── strategy
│              │    ├── AndStrategy.java
│              │    ├── AvoidEdgesStrategy.java
│              │    ├── CaptureMostStrategy.java
│              │    ├── GoCornerStrategy.java
│              │    ├── MinimaxStrategyDepth.java
│              │    └── ReversiStrategy.java
│              └── view
│                   ├── CartesianPosn.java
│                   ├── IView.java
│                   ├── ReversiPanel.java
│                   ├── TextualView.java
│                   └── View.java
└── test
     └── cs3500
          └── reversi
               ├── MockModel.java
               ├── MockModelListener.java
               ├── MockPlayer.java
               ├── MockPlayerListener.java
               ├── MockStrategy.java
               ├── MockView.java
               ├── ReversiControllerTests.java
               ├── ReversiModelTests.java
               ├── ReversiPlayerTests.java
               ├── ReversiStrategyTests.java
               ├── controller
               │    └── PackagePrivateListenersTests.java
               └── view
                    └── PackagePrivateViewTests.java
```

This is the organization of all the files in the codebase. 
The way it is currently organized ensures that the code is organized in a logical manner, 
ensuring suitable visibility and encapsulation of each of the components.

**Note:** This README file provides a high-level overview of the project. For detailed information 
about classes, interfaces, and methods, refer to the Javadoc comments within the code.

Enjoy playing Reversi!
