// $Id: TTTBoard.java,v 1.3 2003/02/17 23:48:30 jeh Exp $

package setup;

/**
 * This class represents a generalized Tic Tac Toe board. It may
 * be any size (but always square), and allows more players than
 * two. Included is a simple textual output method.
 * The class is aspect-ready. It contains no rules of its own.
 *
 * @author James Heliotis
 */

public class TTTBoard {

   /**
    * The most players allowed in the game. This limit is
    * largely dictated by how the board is displayed.
    *
    * @see TTTBoard#display
    */
   public static int MAX_PLAYERS = 9;

   /**
    * Create a tic tac toe board of the given size
    * with the given number of players. Names are digits,
    * starting with '1'. The board is initialized to
    * be empty, i.e., none of the squares are marked
    * with a player's number.
    *
    * @param dimension height and width of board
    * @param nPlayers number of players
    * @pre nPlayers <= MAX_PLAYERS
    */
   public TTTBoard( int dimension, int nPlayers ) {
      if ( nPlayers > MAX_PLAYERS ) {
         throw new PlayerLimitFault( nPlayers );
      }
      dim = dimension;
      numPlayers = nPlayers;
      board = new int[dim][dim];
      for ( int i=0; i<dim; i++ ) {
         for ( int j=0; j<dim; j++ ) {
            board[j][i] = 0;
         }
      }
   }

   /**
    * Answer the size of the board.
    *
    * @return size, i.e. the height or width (same number)
    */
   public int getDimension() {
      return dim;
   }

   /**
    * Check legitimacy of column number.
    *
    * @param col the column number to be tested
    * @return true iff column # &quot;col&quot; is out of bounds
    * @see TTTBoard#TTTBoard
    * @post result == col &gt;= 1 and col &lt;= getDimension()
    */
   public boolean badColumn( int col ) {
      return col < 1 || col > getDimension();
   }

   /**
    * Check legitimacy of row number.
    *
    * @param row the row number to be tested
    * @return true iff row # &quot;row&quot; is out of bounds
    * @see TTTBoard#TTTBoard
    * @post result == row &gt;= 1 and row &lt;= getDimension()
    */
   public boolean badRow( int row ) {
      return row < 1 || row > getDimension();
   }

   /**
    * Answer the number of players in the game. The number
    * returned is also the highest legal player number.
    *
    * @return the number of players set up for this game
    */
   public int getNumPlayers() {
      return numPlayers;
   }

   /**
    * Check legitimacy of player number.
    *
    * @param player the player number to be tested
    * @return true iff player # &quot;player&quot; is out of bounds
    * @see TTTBoard#TTTBoard
    * @post result == player &gt;= 1 and player &lt;= getNumPlayers()
    */
   public boolean badPlayer( int player ) {
      return player < 1 || player > getNumPlayers();
   }

   /**
    * Mark a square with a player's number
    *
    * @param row the row number of the square to mark
    * @param col the column number of the square to mark
    * @param player the player's number
    * @pre not badRow(row) and not badColumn(col)
    * @pre not badPlayer(player)
    * @post getMark(row,col) == player
    */
   public void markSquare( int row, int col, int player )
    throws WrongPlayerException, GameOverException {
      if ( badRow( row ) || badColumn( col ) ) {
         throw new BoardBoundsFault( row, col );
      }
      if ( badPlayer( player ) ) {
         throw new IllegalPlayerFault( player );
      }
      board[row-1][col-1] = player;
   }

   /**
    * Answer the number of the player who has marked this square.
    *
    * @pre not badRow(row) and not badColumn(col)
    * @return player number in square whose coordinates are given,
    *         or 0 if no one has marked it.
    */
   public int getMark( int row, int col ) {
      if ( badRow( row ) || badColumn( col ) ) {
         throw new BoardBoundsFault( row, col );
      }
      return board[row-1][col-1];
   }

   /**
    * Has any player has marked this square?
    *
    * @pre not badRow(row) and not badColumn(col)
    * @return false iff no one has marked it.
    * @post result == getMark(row,col) != 0
    */
   public boolean occupied( int row, int col ) {
      return getMark( row, col ) != 0;
   }

   /**
    * Display the contents of the board on standard output.
    * The player numbers' marks are displayed in a grid that
    * is outlined in this manner:
    * <pre>
    *  | |
    * -+-+-
    *  | |
    * -+-+-
    *  | |
    * </pre>
    * Position (1,1) is in the upper left.
    * Unmarked squares are left blank.
    */
   public void display() {
      for ( int i=1; i<=getDimension(); i++ ) {
         if ( i != 1 ) {
            for ( int j=1; j<=getDimension()-1; j++ ) {
               System.out.print( "-+" );
            }
            System.out.println( "-" );
         }
         for ( int j=1; j<=getDimension(); j++ ) {
            String mark = ( occupied(i,j) ? ( "" + getMark(i,j) ): " " );
            if ( j != 1 ) {
               System.out.print( "|" );
            }
            System.out.print( mark );
         }
         System.out.println();
      }
      System.out.println();
   }

   /**
    * The 2-D array for the board
    */
   private int[][] board;

   /**
    * Height and width of the board
    */
   private int dim;

   /**
    * Number of players
    */
   private int numPlayers;

   /**
    * An assertion violation for when the game is set up with
    * too many players
    */
   private static class PlayerLimitFault extends RuntimeException {
      /**
       * Initialize the exception with the number of players. The
       * exception message is set to explain what value was attempted
       * and what the maximum is.
       */
      PlayerLimitFault( int badNPlayers ) {
         super( "Too many players: " + badNPlayers +
                "; maximum is " + MAX_PLAYERS );
      }
   }

   /**
    * An assertion violation for an illegal row/column pair provided
    * as an argument to various methods
    */
   private static class BoardBoundsFault extends RuntimeException {
      /**
       * Initialize the exception with the coordinates. The
       * exception message is set to explain what coordinates
       * were attempted.
       */
      BoardBoundsFault( int row, int col ) {
         super( "Location (" + row + ',' + col + ") is out of bounds" );
      }
   }

   /**
    * An assertion violation for an illegal player number provided
    * as an argument to various methods.
    */
   private static class IllegalPlayerFault extends RuntimeException {
      /**
       * Initialize the exception with the player number. The
       * exception message is set to explain what player number
       * was attempted.
       */
      IllegalPlayerFault( int player ) {
         super( "Player #" + player + " is illegal" );
      }
   }

   /**
    * An exception that is raised when the rules of the game
    * are violated with respect to which player's turn it is.
    * This exception is raised by a rules concern that is not
    * part of this class by itself.
    */
   public static class WrongPlayerException extends Exception {
      /**
       * Create the exception instance and record what player
       * was attempting to mark a square.
       */
      public WrongPlayerException( int player ) {
         super();
         this.player = player;
      }
      /**
       * The number of the player that tried to mark a square
       */
      private int player;
      /**
       * Answer the number of the player that tried to mark a square.
       * @return number of player
       */
      public int getPlayer() {
         return player;
      }
   }

   /**
    * An exception that is raised when the rules of the game
    * indicate that someone has won tic tac toe.
    * This exception is raised by a rules concern that is not
    * part of this class by itself.
    */
   public static class GameOverException extends Exception {
      /**
       * Create the exception instance and record the provided
       * message.
       * @param msg the message to be stored in the exception
       * @post getMessage() == msg
       */
      public GameOverException( String msg ) {
         super( msg );
      }
   }

} // TTTBoard
