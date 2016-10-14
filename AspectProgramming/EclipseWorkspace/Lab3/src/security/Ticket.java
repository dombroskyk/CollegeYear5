package security;

/**
 * A simulated capability, or identity token, used to access system services
 * @author James Heliotis
 */
public class Ticket implements Comparable< Level > {

    private final int tag;

    /**
     * Create a ticket given a user's integer ID.
     * Tickets are used as the first argument to any
     * method that has restrictions on it, as specified
     * in its Privilege annotation.
     * Note that a real ticket issue would require authentication
     * and possibly further authorization.
     * @param user_id the ID of the user requesting the ticket
     * @see Privilege
     */
    public Ticket( int user_id ) {
        this.tag = user_id + 1000000; // stand-in for encrypted info
    }

    /**
     * Extract user ID from this ticket.
     * @return the ID presented at construction time
     */
    private int getID() {
        return this.tag - 1000000;
    }

    /**
     * Is this ticket of high enough privilege level?
     * @param level the minimum Level to which this Ticket is to be compared
     * @return a negative number if this Ticket doesn't qualify,
     *         zero if this Ticket and the argument level match, and
     *         a positive number if this Ticket exceeds the minimum privilege
     *         specified by the argument level
     */
    @Override
    public int compareTo( Level level ) {
        int id = this.getID();
        Level minLevel = Level.computeLevel( id );
        return minLevel.compareTo( level );
    }

    /**
     * Represent this Ticket in terms of its internal code.
     * Mostly for debug purposes.
     * @return the stored Ticket tag (top secret code)
     */
    @Override
    public String toString() {
        return "Ticket[" + this.tag + ']';
    }

}
