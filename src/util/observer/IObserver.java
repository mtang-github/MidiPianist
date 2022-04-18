package util.observer;

/**
 * An {@code IObserver} receives messages of type T.
 *
 * @param <T> the type of message to receive.
 *
 * @see ISubject
 */
public interface IObserver<T> {
    /**
     * Pushes the given data to this observer.
     *
     * @param data the data to push to the observer.
     */
    void update(T data);
}