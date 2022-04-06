package util.observer;

/**
 * An {@code ISubject} broadcasts messages of type T.
 *
 * @param <T> the type of message to broadcast.
 *
 * @see IObserver
 */
public interface ISubject<T> {
    /**
     * Adds the specified observer to this subject's broadcast list
     *
     * @param observer the observer to attach to this subject
     */
    void attach(IObserver<T> observer);

    /**
     * Removes the specified observer from this subject's broadcast list
     *
     * @param observer the observer to remove from this subject
     */
    void detach(IObserver<T> observer);
}
