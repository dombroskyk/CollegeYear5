package test;

import security.PrivClient;
import security.SecurityBreach;
import security.Ticket;

/**
 * Privilege test program 1
 *
 * @author James Heliotis
 */
public class PrivilegeTest1 implements PrivClient {

    /**
     * A non-privileged client class that is hard for AspectJ to detect.
     * (Can you?)
     */
    private static class BadGuy1 {
        {
            new Services1().status();
        }
    }

    private Services1 serv = null;
    private Ticket tick = null;

    private PrivilegeTest1( Ticket t, Services1 services ) {
        this.serv = services;
        this.tick = t;
    }

    @Override
    public Ticket getClientTicket() {
        return tick;
    }

    /**
     * Call code in Services1 with various tickets.
     */
    private void doServices() {
        try {
            System.out.println( "Using ticket " + this.getClientTicket() );
            serv.status();
            System.out.println( "status() has been called." );
            serv.mgr_admin( 1000 );
            System.out.println( "mgr_admin() has been called." );
            int dummy = serv.admin_only( 3, 11 );
            System.out.println( "admin_only() returned " + dummy );
        }
        catch( SecurityBreach sb ) {
            System.out.println( sb.getMessage() );
            System.out.println(
                    "Function " + sb.getServiceOp() +
                    "\n that required clearance " +
                    sb.getNeededClearance() + "\n was called from " +
                    sb.getClient() + "\n with " +
                    sb.getCallerTicket() + '.'
            );
        }
    }

    public static void main( String[] args ) {
        Services1 services = new Services1();
        PrivilegeTest1 jim =
                new PrivilegeTest1( new Ticket( 6 ), services );
        PrivilegeTest1 paul =
                new PrivilegeTest1( new Ticket( 106 ), services );
        PrivilegeTest1 linus =
                new PrivilegeTest1( new Ticket( 2000 ), services );
        for ( PrivilegeTest1 user : new PrivilegeTest1[]{ jim, paul, linus } ) {
            user.doServices();
        }

        try {
            @SuppressWarnings( "unused" )
            BadGuy1 dummy = new BadGuy1();
            System.out.println(
                    "The program should have failed before getting here." );
        }
        catch( SecurityBreach sb ) {
            System.out.println( sb.getMessage() );
            System.out.println(
                    sb.getClient() +
                    "\n attempted to call " + sb.getServiceOp() + '.'
            );
        }
    }

}


// class BadGuy2 {
//     {
//         new Services1().status();
//     }
// }

