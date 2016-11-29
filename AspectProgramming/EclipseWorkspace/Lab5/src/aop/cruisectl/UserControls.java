package aop.cruisectl;

/**
 * The "user interface" to a vehicle's cruise control system
 *
 * @author James Heliotis
 * @version 1
 */
public class UserControls {

    enum Control { enable, set, resume, cancel, disable }

    private ThrottleControl control;

    /**
     * Initialize the system. Make sure cruise control is
     * totally off.
     * @param control the car cruise control simulator
     */
    public UserControls( ThrottleControl control ) {
        this.control = control;
        disable();
    }

    /**
     * Turn on the cruise control system. However, it is not
     * engaged, and no speed has been memorized. This "state
     * change" is local to the user controls.
     */
    public void enable() {
    }

    /**
     * Record the car's current speed as the memorized speed
     * and engage cruise control.
     */
    public void set() {
        control.simulateSpeedStored( true );
        control.simulateSpeedControl( true );
    }

    /**
     * Disengage the cruise control. Memorized speed is still valid.
     */
    public void cancel() {
        control.simulateSpeedControl( false );
    }

    /**
     * Re-engage the cruise control using the memorized speed.
     */
    public void resume() {
        control.simulateSpeedControl( true );
    }

    /**
     * Completely turn off the cruise control system. The speed memory
     * is not to be trusted at this point.
     */
    public final void disable() {
    }

    /**
     * Use this method to display cases when operating a particular
     * control is disallowed.
     */
    void disallowed( Control ctl ) {
        System.out.println( ctl + " operation disallowed at this time." );
    }

}
