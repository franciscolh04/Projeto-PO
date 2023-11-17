package xxl.functions.IntFuncs;

import xxl.functions.IntFunc;
import xxl.structures.Content;
import xxl.visitors.Visitor;

/**
 * Class representing a PRODUCT function.
 */
public class Product extends IntFunc {
    
    private Content[] _contents = null;

    public Product(Content[] contents) {
        super("PRODUCT", contents[0], contents[contents.length - 1]);
        _contents = contents;
    }

    public String accept(Visitor v) {
        return v.visitPRODUCT(this);
    }

    public Content clone() {
        Content content = new Product(_contents);
        return content;
    }

    @Override
    public Content[] getArguments() {
        return _contents;
    }

}