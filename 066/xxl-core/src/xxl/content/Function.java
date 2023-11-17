package xxl.content;

import java.io.Serializable;
import xxl.structures.Content;

/**
 * Class representing a function.
 */
public abstract class Function implements Content, Serializable {
    
    /** Function's value */
    private String _value = "";

    /** Function's expression */
    private String _expression = "";

    public Function(String content) {
        _expression = content;
    }
    
    public abstract Content clone();
    
    public String getValue() {
        return _value;
    }

    public String getString() {
        return _expression;
    }

    public void setValue(String value) {
        _value = value;
    }

    @Override
    public String toString() {
       return _value + "=" + _expression;
    }
    
}

