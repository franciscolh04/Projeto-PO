package xxl.content;

import java.io.Serializable;

import xxl.structures.Content;
import xxl.visitors.Visitor;

/**
 * Class representing an integer literal.
 */
public class IntLit implements Content, Serializable {

    /** IntLit's value */
    private String _value = "";

    public IntLit(String content) {
        String _number = content.replaceAll("\\s", "");
        _value = _number;
    }

    public String accept(Visitor v) {
        return v.visitIntLit(this);
    }

    public Content clone() {
        Content content = new IntLit("" + _value);
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
