package xxl.visitors;

import xxl.functions.BinFuncs.*;
import xxl.functions.IntFuncs.*;
import xxl.content.IntLit;
import xxl.content.StringLit;
import xxl.content.Reference;

/*
 * Class representing a visitor for the references.
 */
public interface Visitor {
    String visitADD(Add c);
    String visitSUB(Sub c);
    String visitMUL(Mul c);
    String visitDIV(Div c);
    String visitAVERAGE(Average c);
    String visitPRODUCT(Product c);
    String visitCONCAT(Concat c);
    String visitCOALESCE(Coalesce c);
    String visitIntLit(IntLit c);
    String visitStringLit(StringLit c);
    String visitReference(Reference c);
}
