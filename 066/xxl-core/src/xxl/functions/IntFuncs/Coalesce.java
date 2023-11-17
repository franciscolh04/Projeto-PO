package xxl.functions.IntFuncs;

import xxl.functions.IntFunc;
import xxl.structures.Content;
import xxl.visitors.Visitor;

/**
 * Class representing a COALESCE function.
 */
public class Coalesce extends IntFunc {

    private Content[] _contents = null;

    public Coalesce(Content[] contents) {
        super("COALESCE", contents[0], contents[contents.length - 1]);
        _contents = contents;
    }

    public String accept(Visitor v) {
        return v.visitCOALESCE(this);
    }

    public Content clone() {
        Content content = new Average(_contents);
        return content;
    }
    
    @Override
    public Content[] getArguments() {
        return _contents;
    }
}