package xxl.functions.IntFuncs;

import xxl.functions.IntFunc;
import xxl.structures.Content;
import xxl.visitors.Visitor;

/**
 * Class representing a CONCAT function.
 */
public class Concat extends IntFunc {

    private Content[] _contents = null;

    public Concat(Content[] contents) {
        super("CONCAT", contents[0], contents[contents.length - 1]);
        _contents = contents;
    }

    public String accept(Visitor v) {
        return v.visitCONCAT(this);
    }

    public Content clone() {
        Content content = new Concat(_contents);
        return content;
    }

    @Override
    public Content[] getArguments() {
        return _contents;
    }
}