package cs3500.reversi;

import cs3500.reversi.model.IModel;
import cs3500.reversi.view.ViewType;


/**
 * A main runner for a program of Reversi.
 */
public final class Reversi {
  private static int NUM_RINGS = 5;
  private static ViewType VIEW_TYPE;
  
  /**
   * Runs the main program using the HexModel and the View.
   * @param args String command line arguments
   */
  public static void main(String[] args) {
    if (args.length < 4 || args.length > 6) {
      System.err.println("Missing/too many game configuration options.");
      System.exit(0);
      // The below statement will never be reached. This is only for type-checker.
      return;
    }
    
    IModel model;
    
    VIEW_TYPE = Reversi.parseViewType(args[0]);
    
    NUM_RINGS = Reversi.parseNumRings(args[1]);
    
    if (args.length == 4) {
      model = Reversi.parseLengthTwo(args);
    }
    else if (args.length == 5) {
      model = Reversi.parseLengthThree(args);
    }
    else {
      model = Reversi.parseLengthFour(args);
    }
    
    model.startGame();
  }
  
  private static ViewType parseViewType(String arg) {
    switch (arg) {
      case "square":
        return ViewType.SQUARE;
      case "hex":
        return ViewType.HEX;
      default:
        System.err.println("Unsupported view type found: " + arg);
        System.exit(-1);
        // The below statement will never be reached. This is only for type-checker.
        return null;
    }
  }
  
  private static int parseNumRings(String arg) {
    int numRings;
    try {
      numRings = Integer.parseInt(arg);
      if (numRings < 1) {
        System.err.println("Number of rings cannot be less than 1");
        System.exit(-1);
        // The below statement will never be reached. This is only for type-checker.
        return -1;
      }
      return numRings;
    } catch (NumberFormatException nfe) {
      System.err.println(nfe.getMessage());
      System.exit(-1);
      // The below statement will never be reached. This is only for type-checker.
      return -1;
    }
  }
  
  // Parses the command line arguments when two arguments are passed in.
  private static IModel parseLengthTwo(String[] args) {
    ReversiFactory.GameType gt1;
    ReversiFactory.GameType gt2;
    try {
      gt1 = Reversi.parseGameType(args[2]);
      gt2 = Reversi.parseGameType(args[3]);
    } catch (IllegalArgumentException ia) {
      System.err.println(ia.getMessage());
      System.exit(-1);
      // The below statement will never be reached. This is only for type-checker.
      return null;
    }
    
    return ReversiFactory.makeModel(Reversi.NUM_RINGS, Reversi.VIEW_TYPE,
        gt1, 0, gt2, 0);
  }
  
  // Parses the command line arguments when three arguments are passed in.
  private static IModel parseLengthThree(String[] args) {
    ReversiFactory.GameType gt1;
    ReversiFactory.GameType gt2;
    int gt1Depth = 0;
    int gt2Depth = 0;
    
    try {
      gt1 = Reversi.parseGameType(args[2]);
      try {
        gt2 = Reversi.parseGameType(args[3]);
        gt2Depth = Integer.parseInt(args[4]);
      } catch (NumberFormatException e) {
        System.err.println("Minimax depth must be an integer greater than 0.");
        System.exit(0);
        // This code below will never be reached. Only there for type checker.
        return null;
      } catch (IllegalArgumentException ia) {
        try {
          gt1Depth = Integer.parseInt(args[3]);
          try {
            gt2 = Reversi.parseGameType(args[4]);
          } catch (IllegalArgumentException i) {
            System.err.println(ia.getMessage());
            System.exit(0);
            // This code below will never be reached. Only there for type checker.
            return null;
          }
        } catch (NumberFormatException e) {
          System.err.println("Minimax depth must be an integer greater than 0.");
          System.exit(0);
          // This code below will never be reached. Only there for type checker.
          return null;
        }
      }
    } catch (IllegalArgumentException ia) {
      System.err.println(ia.getMessage());
      System.exit(0);
      // This code below will never be reached. Only there for type checker.
      return null;
    }
    
    return ReversiFactory.makeModel(Reversi.NUM_RINGS, Reversi.VIEW_TYPE,
        gt1, gt1Depth, gt2, gt2Depth);
  }
  
  // Parses the command line arguments when four arguments are passed in.
  private static IModel parseLengthFour(String[] args) {
    ReversiFactory.GameType gt1;
    ReversiFactory.GameType gt2;
    int gt1Depth;
    int gt2Depth;
    
    try {
      gt1 = Reversi.parseGameType(args[2]);
      gt1Depth = Integer.parseInt(args[3]);
      gt2 = Reversi.parseGameType(args[4]);
      gt2Depth = Integer.parseInt(args[5]);
    } catch (NumberFormatException e) {
      System.err.println("Minimax depth must be an integer greater than 0.");
      System.exit(0);
      // The below statement will never be reached. This is only for type-checker.
      return null;
    } catch (IllegalArgumentException ia) {
      System.err.println(ia.getMessage());
      System.exit(0);
      // The below statement will never be reached. This is only for type-checker.
      return null;
    }
    
    return ReversiFactory.makeModel(Reversi.NUM_RINGS, Reversi.VIEW_TYPE,
        gt1, gt1Depth, gt2, gt2Depth);
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
}