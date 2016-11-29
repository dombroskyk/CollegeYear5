package aop.cruisectl;

/**
 * Simulate the essentials of the car's cruise control system. For the
 * case of the state machine and legal/illegal actions, we really only
 * need to know about two things.
 * <ol>
 *     <li>Is there some speed setting stored?</li>
 *     <li>Is the control system engaged or not?</li>
 * </ol>
 * No state is actually stored here. There are only announcements of
 * events for simulation purposes. The reason for this is that we are
 * assuming that the software cannot query this object about its
 * state.
 *
 * @author James Heliotis
 * @version 1
 */
public class ThrottleControl {

    /**
     * Initialize the control system and state that everything is "off".
     */
    public ThrottleControl() {
        simulateSpeedStored( false );
        simulateSpeedControl( false );
    }

    /**
     * Simulate storing, or erasing, the vehicle's current speed in
     * cruise control memory. Note that in reality you can't erase
     * memory: something is always stored. This method is called
     * with stored=false only at the start of each simulation run,
     * and it is done by this class's constructor.
     *
     * @param stored whether to store a value(true) or erase the value(false)
     */
    public void simulateSpeedStored( boolean stored ) {
        if ( stored ) {
            System.out.println( "Current car speed is stored in memory." );
        }
        else {
            System.out.println( "Speed memory has been erased." );
        }
    }

    /**
     * Change whether cruise control is engaged, controlling the vehicle's
     * speed, or not.
     *
     * @param engaged true=&gt;engage; false=&gt;disengage
     */
    public void simulateSpeedControl( boolean engaged ) {
        if ( engaged ) {
            System.out.println( "Car cruise control engaged." );
        }
        else {
            System.out.println( "Car cruise control disengaged." );
        }
    }
}
