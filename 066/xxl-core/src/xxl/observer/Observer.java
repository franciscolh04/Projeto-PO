package xxl.observer;

import xxl.structures.Content;

/**
 * Interface for the Observer design pattern.
 * 
 * @see Subject
 */
public interface Observer {
    public void update(Content content, String value);
}
