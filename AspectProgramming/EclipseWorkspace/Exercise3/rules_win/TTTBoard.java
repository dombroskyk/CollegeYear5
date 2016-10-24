package rules_win;

/**
 * Author: Kevin Dombrosky
 */
public class TTTBoard {

	public TTTBoard( int dimension, int nPlayers ) {}

	private boolean nInARow( int player ) {
		boolean result = false;
		int dim = getDimension();
		//Look for winning row
		for ( int row=1; row<=dim; ++row ) {
		        boolean winning = true;
        		for ( int col=1; col<=dim; ++col ) {
				winning = winning && ( getMark( row, col ) == player );
			}
			result = result || winning;
		}

			// Look for winning column.
		for ( int col=1; col<=dim; ++col ) {
			boolean winning = true;
			for ( int row=1; row<=dim; ++row ) {
				winning = winning && ( getMark( row, col ) == player );
			}
			result = result || winning;
		}

	        // Look for winning diagonal.
	        boolean posSlope = true;
		boolean negSlope = true;
		for ( int i=1; i<=dim; ++i ) {
	        	negSlope = negSlope && ( getMark( i, i ) == player );
	         	posSlope = posSlope && ( getMark( i, 1+dim-i ) == player );
      		}
		result = result || posSlope;
		result = result || negSlope;

		return result;

	}

	public void markSquare( int row, int col, int player )  throws GameOverException {
		if( nInARow( player ) ) {
			throw new GameOverException( "Player " + Integer.toString(player) + " won! Game over." );
		}
	}

	public int getMark( int row, int col ) {
		return -1;
	}

	public int getDimension() {
		return -1;
	}

	public static class GameOverException extends Exception {
		public GameOverException( String msg ) {}
	}

}
