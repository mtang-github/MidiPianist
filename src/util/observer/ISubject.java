package util.observer;

public interface ISubject<T> {
    void attach(IObserver<T> observer);
    void detach(IObserver<T> observer);
}
