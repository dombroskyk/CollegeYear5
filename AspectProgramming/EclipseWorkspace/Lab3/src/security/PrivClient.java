package security;

/**
 * An interface that must be implemented by any client accessing
 * services with privilege protection
 * 
 * @author James Heliotis
 */
public interface PrivClient {

    /**
     * Find out the privilege level of a client attempting
     * a call annotated with Privilege.
     * @return the ticket this client has been given
     * @see Privilege
     */
    Ticket getClientTicket();
}
