package observing;

import java.util.HashSet;
import java.util.Set;

/**
 * A set of utilities for observed objects.
 * A class that extends this class will have access to its collection
 * of observers and will be able to notify all of them when this subject
 * changes state.
 *
 * There are some issues with the design because of the constraint that
 * type safety must be maintained.
 * <ul>
 *     <li>The type of information a subject can send to an observer
 *         is specified at compile time.</li>
 *     <li>A redundant field containing the identity of the subject
 *         must be set.</li>
 * </ul>
 *
 * @see AbstractSubject#setSelf(Object)
 *
 * @author James Heliotis
 */
public abstract class AbstractSubject< Subj, Info >
                          implements Subject< Subj, Info > {

    /** The set of observers currently subscribed to this subject */
    private Set< Observer< Subj, Info > > observers = new HashSet<>();

    /** A copy of the 'this' variable, but with its type corrected. */
    private Subj self = null;

    /**
     * Establish the identity of the subject.
     * This method must be called in the constructor of the Subject subclass.
     * Having this redundant method here keeps this class type safe.
     *
     * @param self the identity of the object being observed: this
     */
    protected void setSelf( Subj self ) {
        this.self = self;
    }

    /**
     * Add a new observer.
     *
     * @param obs the object that wants updates on state changes of this object
     */
    @Override
    public void subscribe( Observer< Subj, Info > obs ) {
        observers.add( obs );
        obs.notify( self );
    }

    /**
     * Remove an observer.
     *
     * @param obs the object that no longer
     *            wants updates on state changes of this object
     */
    @Override
    public void unsubscribe( Observer< Subj, Info > obs ) {
        observers.remove( obs );
    }

    /**
     * The state of this object has changed; notify the observers.
     * This method should be called by mutators in the subclass.
     */
    protected void reportChange() {
        for ( Observer< Subj, Info > obs: observers ) {
            obs.notify( self );
        }
    }

    /**
     * The state of this object has changed; notify the observers.
     * This method should be called by mutators in the subclass.
     */
    protected void reportChange( Info info ) {
        for ( Observer< Subj, Info > obs: observers ) {
            obs.notify( self, info );
        }
    }
}
