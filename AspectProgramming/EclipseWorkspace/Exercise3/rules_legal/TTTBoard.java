package rules_legal;

/**
 * Author: Kevin Dombrosky
 */
public class TTTBoard {

	private int numPlayers;
	private int currPlayer = 1;

	public TTTBoard( int dimension, int nPlayers ) {
		this.numPlayers = nPlayers;
	}

	public void markSquare( int row, int col, int player )
		throws setup.TTTBoard.WrongPlayerException {

		if( player != currPlayer ){
			throw new setup.TTTBoard.WrongPlayerException( player );
		} else {
			currPlayer++;
			if( currPlayer > this.numPlayers ){
				currPlayer = 1;
			}
		}
	}

	public int getNumPlayers() {
		return this.numPlayers;
	}
	

}
