package xxl.content;

import java.io.Serializable;
import xxl.structures.Content;
import xxl.visitors.Visitor;

/**
 * Class representing a string literal.
 */
public class StringLit implements Content, Serializable {

    /** StringLit's value */
    private String _value = "";

    public StringLit(String content) {
        _value = content;
    }

    public String accept(Visitor v) {
        return v.visitStringLit(this);
    }

    public Content clone() {
        Content content = new StringLit(_value);
        return content;
    }

    
    public String getValue() {
        return _value;
    }

    public String getString() {
        return _value;
    }

    
    public void setValue(String value) {
        _value = value;
    }

    @Override
    public String toString() {
        return _value;
    }
}

