package util.observer;

public interface IObserver<T> {
    void update(T data);
}