package security;

import java.io.PrintWriter;

/**
 * An exception used by PrivilegeChecker to indicate a protection violation.
 * It is under RuntimeException since the original methods being checked
 * do not claim to throw it; the aspect adds in that possibility.
 */
@SuppressWarnings( "serial" )
public class SecurityBreach extends RuntimeException {
    private final String client;
    private final String serviceOp;
    private final Level neededClearance;
    private final Ticket callerTicket;

    /**
     * Create a SecurityBreach exception object that explains the breach.
     *
     * @param neededClearance the minimum clearance that a called function
     *                        required
     * @param callerTicket    the actual Ticket presented by the caller.
     */
    SecurityBreach(
            Level neededClearance,
            Ticket callerTicket,
            String caller, String callee ) {
        super( "Unauthorized call" );
        this.client = caller;
        this.serviceOp = callee;
        this.neededClearance = neededClearance;
        this.callerTicket = callerTicket;
    }

    /**
     * Create a default SecurityBreach exception object that
     * only stores the calling method's name.
     */
    public SecurityBreach( String caller, String callee ) {
        super( "Unticketed call to privileged code" );
        this.neededClearance = Level.NONE;
        this.callerTicket = null;
        this.client = caller;
        this.serviceOp = callee;
    }

    public Level getNeededClearance() {
        return this.neededClearance;
    }

    public Ticket getCallerTicket() {
        return this.callerTicket;
    }

    public String getClient() {
        return client;
    }

    public String getServiceOp() {
        return serviceOp;
    }

    public void explain( PrintWriter outStream ) {
        Ticket ticket = this.getCallerTicket();
        String ticketStr = ticket == null ? "" : ( "/" + ticket );
        Level clearance = this.getNeededClearance();
        String clearanceStr =
                clearance == Level.NONE ? "" : ( "!" + clearance );
        outStream.println(
                this.getClient() + ticketStr + " --> " +
                this.getServiceOp() + clearanceStr
        );
    }
}
