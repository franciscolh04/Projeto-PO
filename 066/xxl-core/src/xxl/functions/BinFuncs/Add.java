package xxl.functions.BinFuncs;

import xxl.functions.BinFunc;
import xxl.structures.Content;
import xxl.visitors.Visitor;

/**
 * Class representing a ADD function.
 */
public class Add extends BinFunc {
    
    private Content _arg1 = null;
    private Content _arg2 = null;

    public Add( Content arg1, Content arg2) {
        super("ADD", arg1, arg2);
        _arg1 = arg1;
        _arg2 = arg2;
    }

    public String accept(Visitor v) {
        return v.visitADD(this);
    }

    public Content clone() {
        Content content = new Add(_arg1, _arg2);
        return content;
    }

    @Override
    public Content getArgument(int num){
        if(num == 1) {
            return _arg1;
        }
        else {
            return _arg2;
        }
    }

    @Override
    public String calculate(String value1, String value2) {
        return "" + (Integer.parseInt(value1) + Integer.parseInt(value2));
    }
}
