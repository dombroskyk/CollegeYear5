package test;

import security.Level;
import security.Privilege;

/**
 * A test set of methods with different privilege levels.
 * Contents are nonsense.
 * This test is not designed to be thorough.
 * @see PrivilegeTest1
 */
public class Services1 {

    @Privilege( Level.ADMIN )
    public int admin_only( int x, int y ) {
        return x + y;
    }

    @Privilege( Level.MANAGER )
    public void mgr_admin( int q ) {
        System.out.println( "Quota set to " + q );
    }

    @Privilege( Level.USER )
    public void status() {
        System.out.println( "The system is up." );
    }

}
