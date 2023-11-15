# Reversi
### Overview
This Reversi game project is a two-player game with a graphical interface. The goal is to implement
the classic Reversi game with a hexagonal grid. The codebase allows for human players to play
against each other or even the possibility of implementing AI players in future versions. For this
iteration of the homework, we have implemented a `Model`, a `View` and a `ReversiStrategy` pattern to develop certain
strategies for the game.

### Extra Credit
- Strategy 2 -- See `strategy/AvoidEdgesStrategy.java`.
- Strategy 3 -- See `strategy/GoCornerStrategy.java`.
- Strategy 4 -- See `strategy/MinimaxStrategy.java`.
- Composing Strategies -- See `AndStrategy.java`. (Allows recombination easily and efficiently).
- Hints for which moves are valid. (Green for valid, red for invalid) -- See `screenshots/CellSelected.png`.
- Shows how many will be captured when cell is clicked -- See `screenshots/ExtraCreditHint.png`.

### Changelog
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

### Quick start
The game can be interacted with by running the `main` method in `Reversi.java`. As the "player", you can click on
certain cells and they will be highlighted red (invalid move) or green (valid move). You can also use your keyboard to
do certain actions such as passing (`P` key) or playing a move at the selected cell (`ENTER` key). For now, these
would be logged to `System.out`. The view also converts your physical click coordinates to logical axial coordinates on
the board.

## Invariant
- The number of key-value pairs in `board` is equal to `3 * numRings * (numRings + 1) + 1`. This is a class variant
because it is a true logical statement about the instantaneous state of the class. It is preserved in all constructors
since it is either validated if a board is passed in or the board is created based on the given number of rings. It is 
also preserved by all the methods because `numRings` is final and the board is never added or removed from.

### Model
- `IROModel` -- A read-only interface that allows the user to make observations on the cs3500.reversi.model such as 
querying the game state.
- `IModel` -- A mutable interface that allows the user to make observations and operations on the model 
such as playing a move and switching turns.
- `Model` -- Concrete implementations of their `IModel` interface.
- `AxialPosn` -- An axial position represents the `q` and `r` coordinates in a hexagonal grid.
- `Direction` -- A direction represents the offset of each hexagonal cell. 

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

### View
- `TextualView` -- A light-weight text-based view to visualize the game of Reversi, mostly for testing purposes.
- `IView` -- A Swing-based graphical user interface for interacting with a Reversi model.

## Screenshots
| Initial State<br/><image src="./screenshots/InitialState.png">           | Cell Selected<br/><image src="./screenshots/CellSelected.png">        |
|--------------------------------------------------------------------------|-----------------------------------------------------------------------|
| Intermediary State<br/><image src="./screenshots/IntermediaryState.png"> | Extra Credit Hint<br/><image src="./screenshots/ExtraCreditHint.png"> |

## Source organization
```
├── README.md
├── screenshots
│   ├── CellSelected.png
│   ├── InitialState.png
│   └── IntermediaryState.png
├── src
│   └── cs3500
│       └── reversi
│           ├── StrategyRunner.java
│           ├── Reversi.java
│           ├── model
│           │   ├── AxialPosn.java
│           │   ├── Direction.java
│           │   ├── IModel.java
│           │   ├── IROModel.java
│           │   ├── ModelFeatures.java
│           │   ├── Model.java
│           │   └── PieceColor.java
│           ├── strategy
│           │   ├── AndStrategy.java
│           │   ├── AvoidEdgesStrategy.java
│           │   ├── CaptureMostStrategy.java
│           │   ├── GoCornerStrategy.java
│           │   ├── MinimaxStrategy.java
│           │   └── ReversiStrategy.java
│           └── view
│               ├── CartesianPosn.java
│               ├── ITextualView.java
│               ├── IView.java
│               ├── ReversiPanel.java
│               ├── TextualView.java
│               ├── ViewFeatures.java
│               └── View.java
└── test
    └── cs3500
        └── reversi
            ├── MockModelForStrategy.java
            ├── MockModelListener.java
            ├── ReversiModelTests.java
            └── StrategyTests.java
```

This is the organization of all the files in the codebase. All `Model` related files are in a 
package called `cs3500.reversi.model`. We have also implemented an interface for a `PlayerListener` along with a 
mock for the same in a package called `MockPlayer`. Finally, we have a simple `TextualView` in a 
package called `cs3500.reversi.view`. This ensures that the code is organized in a logical manner, ensuring 
suitable visibility and encapsulation of each of the components.

**Note:** This README file provides a high-level overview of the project. For detailed information 
about classes, interfaces, and methods, refer to the Javadoc comments within the code.

Enjoy playing Reversi!
