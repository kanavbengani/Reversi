package cs3500.reversi;

import cs3500.reversi.model.IModel;

/**
 * A main runner for a program of Reversi.
 */
public final class Reversi {
  /**
   * Runs the main program using the Model and the View.
   * @param args String command line arguments
   */
  public static void main(String[] args) {
    if (args.length < 2 || args.length > 4) {
      System.err.println("Missing/too many game configuration options.");
      System.exit(0);
      // The below statement will never be reached. This is only for type-checker.
      return;
    }
    ReversiFactory.GameType gt1;
    ReversiFactory.GameType gt2;
    int gt1Depth = 0;
    int gt2Depth = 0;
    
    if (args.length == 2) {
      try {
        gt1 = Reversi.parseGameType(args[0]);
        gt2 = Reversi.parseGameType2(args[1]);
      } catch (IllegalArgumentException ia) {
        System.err.println(ia.getMessage());
        System.exit(0);
        // The below statement will never be reached. This is only for type-checker.
        return;
      }
    }
    else if (args.length == 3) {
      try {
        gt1 = Reversi.parseGameType(args[0]);
        try {
          gt2 = Reversi.parseGameType2(args[1]);
          gt2Depth = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
          System.err.println("Minimax depth must be an integer greater than 0.");
          System.exit(0);
          // The below statement will never be reached. This is only for type-checker.
          return;
        } catch (IllegalArgumentException ia) {
          try {
            gt1Depth = Integer.parseInt(args[1]);
            try {
              gt2 = Reversi.parseGameType2(args[2]);
            } catch (IllegalArgumentException i) {
              System.err.println(ia.getMessage());
              System.exit(0);
              // The below statement will never be reached. This is only for type-checker.
              return;
            }
          } catch (NumberFormatException e) {
            System.err.println("Minimax depth must be an integer greater than 0.");
            System.exit(0);
            // The below statement will never be reached. This is only for type-checker.
            return;
          }
        }
      } catch (IllegalArgumentException ia) {
        System.err.println(ia.getMessage());
        System.exit(0);
        // The below statement will never be reached. This is only for type-checker.
        return;
      }
    }
    else {
      try {
        gt1 = Reversi.parseGameType(args[0]);
        gt1Depth = Integer.parseInt(args[1]);
        gt2 = Reversi.parseGameType2(args[2]);
        gt2Depth = Integer.parseInt(args[3]);
      } catch (NumberFormatException e) {
        System.err.println("Minimax depth must be an integer greater than 0.");
        System.exit(0);
        // The below statement will never be reached. This is only for type-checker.
        return;
      } catch (IllegalArgumentException ia) {
        System.err.println(ia.getMessage());
        System.exit(0);
        // The below statement will never be reached. This is only for type-checker.
        return;
      }
    }
    
    IModel model = ReversiFactory.makeModel(5, gt1, gt1Depth, gt2, gt2Depth);
    model.startGame();
  }

  // Parses the passed-in string to return enum variants of the appropriate player types.
  private static ReversiFactory.GameType parseGameType(String arg) {
    switch (arg) {
      case "human":
        return ReversiFactory.GameType.OUR_HUMAN;
      case "strategy1":
        return ReversiFactory.GameType.OUR_STRATEGY1;
      case "strategy2":
        return ReversiFactory.GameType.OUR_STRATEGY3;
      case "strategy3":
        return ReversiFactory.GameType.OUR_STRATEGY2;
      case "strategy4":
        return ReversiFactory.GameType.OUR_STRATEGY4;
      default:
        throw new IllegalArgumentException("Unsupported game type found: " + arg);
    }
  }
  
  private static ReversiFactory.GameType parseGameType2(String arg) {
    switch (arg) {
      case "providerHuman":
        return ReversiFactory.GameType.PROVIDER_HUMAN;
      case "providerStrategy1":
        return ReversiFactory.GameType.PROVIDER_STRATEGY1;
      case "providerStrategy2":
        return ReversiFactory.GameType.PROVIDER_STRATEGY2;
      case "providerStrategy3":
        return ReversiFactory.GameType.PROVIDER_STRATEGY3;
      default:
        return Reversi.parseGameType(arg);
    }
  }
}