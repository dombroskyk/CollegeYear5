package observing;

/**
 * A description of what it means to be a subject, i.e., to be
 * observable.
 * For a standard implementation of this interface, see
 * AbstractSubject.
 *
 * @author James Heliotis
 */
public interface Subject< S, Info > {

    /**
     * Add a new observer.
     *
     * @param obs the object that wants updates on state changes of this object
     */
    public void subscribe( Observer< S, Info > obs );

    /**
     * Remove an observer.
     *
     * @param obs the object that no longer
     *            wants updates on state changes of this object
     */
    public void unsubscribe( Observer< S, Info > obs );

}