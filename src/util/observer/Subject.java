package util.observer;

import java.util.ArrayList;
import java.util.List;

public class Subject<T> implements ISubject<T> {
    private final List<IObserver<T>> observers;
    public Subject(){
        observers = new ArrayList<>();
    }
    @Override
    public void attach(IObserver<T> observer){
        observers.add(observer);
    }
    @Override
    public void detach(IObserver<T> observer){
        observers.remove(observer);
    }
    public void broadcast(T data){
        for(IObserver<T> observer : observers){
            observer.update(data);
        }
    }
}
