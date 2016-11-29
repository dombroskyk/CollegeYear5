package aop.cruisectl;

/**
 * The states of the cruise control system as represented by the
 * ThrottleControl class.
 * This type may only be useful in its documentation. It tells you what
 * state transitions are legal. However, students are welcome to use it.
 *
 * @see ThrottleControl
 * @author James Heliotis
 * @version 2
 */
public enum State {

    /**
     * Cruise control system is completely off.
     * No previous settings are assumed to be remembered.
     * The only allowed user operation is <em>enable</em>.
     */
    disabled,

    /**
     * Cruise control system is on, ready to have
     * speed set, and enabled and disabled.
     * Allowed user operations are <em>set</em> and <em>disable</em>.
     */
    enabled,

    /**
     * Cruise control system is on, and a speed setting
     * has already taken place.
     * Allowed user operations are <em>set</em>, <em>resume</em>, and
     *                             <em>disable</em>.
     */
    enabledWMem,

    /**
     * Cruise control system is actually engaged, regulating
     * the speed of the vehicle.
     * Allowed user operations are <em>cancel</em>, <em>set</em>, and
     *                             <em>disable</em>.
     */
    active

}
