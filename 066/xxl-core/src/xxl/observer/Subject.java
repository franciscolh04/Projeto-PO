package xxl.observer;

/**
 * Interface for the Subject in the Observer design pattern.
 * 
 * @see Observer
 */
public interface Subject {
    public void attachObserver(Observer o);
    public void detachObserver(Observer o);
    public void notifyObservers();
}
