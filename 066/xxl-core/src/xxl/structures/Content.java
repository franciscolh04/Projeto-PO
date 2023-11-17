package xxl.structures;

import xxl.visitors.Visitor;

/**
 * Class representing the content of a cell.
 */
public interface Content {
    public String accept(Visitor v) ;
    public String getValue();
    public void setValue(String value);
    public Content clone();
    public String getString();
}
