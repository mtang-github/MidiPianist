package util.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@code Subject} implements {@link ISubject}.
 *
 * @param <T> the type of message to broadcast.
 */
public class Subject<T> implements ISubject<T> {
    private final List<IObserver<T>> observers;

    /**
     * Constructs a new {@code Subject}.
     */
    public Subject(){
        observers = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     *
     * @param observer the observer to attach to this subject.
     */
    @Override
    public void attach(IObserver<T> observer){
        observers.add(observer);
    }

    /**
     * {@inheritDoc}
     *
     * @param observer the observer to remove from this subject.
     */
    @Override
    public void detach(IObserver<T> observer){
        observers.remove(observer);
    }

    /**
     * Broadcasts the given data to all subscribed observers.
     *
     * @param data the data to push to all subscribed observers.
     */
    public void broadcast(T data){
        for(IObserver<T> observer : observers){
            observer.update(data);
        }
    }
}
