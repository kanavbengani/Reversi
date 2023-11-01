# Reversi
## Overview
This Reversi game project is a two-player game with a graphical interface. The goal is to implement
the classic Reversi game with a hexagonal grid. The codebase allows for human players to play
against each other or even the possibility of implementing AI players in future versions. For this
iteration of the homework, we have implemented a `Model` that can be used by future implementations
of controllers and views.

## Quick start
The game can be played by instantiating a concrete class that implements `IModel` (such as `Model`) 
with the number of rings as a parameter. 

## Invariant
- The number of key-value pairs in `board` is equal to `3 * numRings * (numRings + 1) + 1`.

## Key Components

### Model
- `IROModel` -- A read-only interface that allows the user to make observations on the model such 
as querying the game state.
- `IModel` -- A mutable interface that allows the user to make observations and operations on the 
model such as playing a move and switching turns.
- `ROModel` and `Model` -- Concrete implementations of their respective interfaces. `Model` extends
the `ROModel` since `Model` adds operational functionality to the observation-only model.
- `AxialPosn` -- An axial position represents the `q` and `r` coordinates in a hexagonal grid.

**Coordinate System**

We decided to use the axial coordinate system, using `q` and `r` coordinates. The `q` represents
the horizontal axis of the hexagon. The `r` represents the top-left to bottom-right diagonal. The
center of the grid is the origin, or `(0, 0)`. We also use a direction enum which enumerates the
**6** neighbors of each cell according to the coordinate system. These include the following with
the given delta `q` and `r`: `UPLEFT` `(0, -1)`, `UPRIGHT` `(1, -1)`, `RIGHT` `(1, 0)`, 
`DOWNRIGHT` `(0, 1)`, `DOWNLEFT` `(-1, 1)`, `LEFT` `(-1, 0)`.

### Player
- The `PlayerListener` interface defines only one public method called `itsTheMoveOf` which takes 
in the currentPieceColor. This method will be implemented in various listener classes that act
based on this information given by the model. These classes include HumanPlayer, AI, and the views.
Currently, we have created a MockPlayer that simply logs a message for testing purposes. 

### View
- `TextualView` -- A light-weight text-based view to visualize the game of Reversi, mostly for
testing purposes.

## Source organization
```.
├── README.md
├── Reversi.iml 
├── src
│       ├── Main.java
│       ├── model
│       │       ├── AxialPosn.java
│       │       ├── Direction.java
│       │       ├── IModel.java
│       │       ├── IROModel.java
│       │       ├── Model.java
│       │       ├── PieceColor.java
│       │       └── ROModel.java
│       ├── player
│       │       ├── MockPlayer.java
│       │       └── PlayerListener.java
│       └── view
│               └── TextualView.java
└── test
    └── ReversiTests.java
```

This is the organization of all the files in the codebase. All `Model` related files are in a 
package called `model`. We have also implemented an interface for a `PlayerListener` along with a 
mock for the same in a package called `MockPlayer`. Finally, we have a simple `TextualView` in a 
package called `view`. This ensures that the code is organized in a logical manner, ensuring 
suitable visibility and encapsulation of each of the components.

**Note:** This README file provides a high-level overview of the project. For detailed information 
about classes, interfaces, and methods, refer to the Javadoc comments within the code.

Enjoy playing Reversi!
