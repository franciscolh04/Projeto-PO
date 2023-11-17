package xxl.content;

import java.io.Serializable;

import xxl.observer.Observer;
import xxl.structures.Content;
import xxl.visitors.Visitor;

/**
 * Class representing a reference.
 */
public class Reference implements Content, Observer, Serializable {

    /** Reference's content */
    private Content _content = null;

    /** Reference's expression */
    private String _expression = "";

    /** Reference's value */
    private String _value = "";

    public Reference(Content content, String expression) {
        _content = content;
        _expression = expression;
    }

    public String accept(Visitor v) {
        return v.visitReference(this);
    }

    public Content clone() {
        Content content = new Reference(_content, _expression);
        return content;
    }

    public Content getContent() {
        return _content;
    }

    public String getValue() {
        return _value;
    }

    public String getString() {
        return _expression;
    }

    public String getRefValue() {
        return _value;
    }

    public void setContent(Content content) {
        _content = content;
    }

    public void setValue(String value) {
        _value = value;
    }

    /**
     * Updates the reference's value and content.
     */
    public void update(Content content, String value) {
        _content = content;
        _value = value;
        
    }

    @Override
    public String toString() {
        return _value + "=" + _expression;
    }

    
}
