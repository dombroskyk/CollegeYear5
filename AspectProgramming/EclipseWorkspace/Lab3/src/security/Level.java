package security;

/**
 * The discrete permission levels and their user ID ranges
 * @author James Heliotis
 */
public enum Level {

    NONE( Integer.MIN_VALUE ), USER( 0 ), MANAGER( 100 ), ADMIN( 1000 );

    /**
     * The minimum ID for this Level's range.
     * To be used by "secure" code to determine privilege levels.
     */
    private int idUnder;

    /**
     * Initialize a Level enum value.
     * @param idUnder the minimum ID in this level's range
     */
    private Level( int idUnder ) {
        this.idUnder = idUnder;
    }

    /**
     * What privilege level does a user have?
     * @param id the user's ID
     * @return the maximum privilege level for that user
     */
    static Level computeLevel( int id ) {
        Level result = NONE;
        for ( Level level: Level.values() ) {
            if ( id >= level.idUnder ) {
                result = level;
            }
        }
        return result;
    }
}
