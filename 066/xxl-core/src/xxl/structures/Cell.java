package xxl.structures;

import java.io.Serializable;
import java.util.ArrayList;
import xxl.observer.*;

/**
 * Class representing a cell in a spreadsheet.
 */
public class Cell implements Subject, Serializable {

    /** Cell's content */
    private Content _content = null;

    /** List of observers of the cell */
    private ArrayList<Observer> _observers = new ArrayList<Observer>();
    
    public Cell() {
        // construtor de célula vazia
    }

    /**
     * Inserts the specified content into the cell.
     * 
     * @param content the content to be inserted.
     */
    public void insertContent(Content content) {
        _content = content;
    }

    /**
     * Deletes the content of the cell.
     * 
     * @param content the content to be deleted.
     */
    public void deleteContent() {
        _content = null;
    }

    /**
     * @return the content of the cell.
     */
    public Content getContent(){
        return _content;
    }

    /**
     * @return the value of the cell.
     */
    public String getValue() {
        return _content.getValue();
    }

    /**
     * Sets the value of the cell.
     * 
     * @param value the value of the cell.
     */
    public void setValue(String value) {
        _content.setValue(value);
    }

    /**
     * Attaches an observer to the cell.
     * 
     * @param o the observer to be attached.
     */
    public void attachObserver(Observer o) {
        _observers.add(o);
    }

    /**
     * Detaches an observer from the cell.
     * 
     * @param o the observer to be detached.
     */
    public void detachObserver(Observer o) {
        int i = _observers.indexOf(o);
        if (i >= 0) {
            _observers.remove(i);
        }
    }

    /**
     * Notifies all observers of the cell that its content has changed.
     */
    public void notifyObservers() {
        for (Observer observer: _observers) {
            if(_content != null) {
                observer.update(_content, _content.getValue());
            }
            else {
                observer.update(_content, "");
            }
        }
    }

    @Override
    public String toString() {
        if (_content != null) {
            return _content.toString();
        }
        else {
            return "";
        }
    }

}
