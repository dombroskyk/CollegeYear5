package observing;

import model.Info;

/**
 * Any object that observes the state of another object can implement
 * this interface.
 *
 * The methods are defaulted so that they do not all have to be
 * implemented.
 *
 * @author James Heliotis
 */
public interface Observer< T, Info > {

    /**
     * A subject calls the update method on any object that is observing
     * its state.
     * @param subject the object whose state has changed
     */
    default void notify( T subject ) {}

    /**
     * A subject calls the update method on any object that is observing
     * its state.
     * @param subject the object whose state has changed
     */
    default void notify( T subject, Info info ) {}
}
