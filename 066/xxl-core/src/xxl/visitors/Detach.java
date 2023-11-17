package xxl.visitors;

import xxl.functions.BinFuncs.*;
import xxl.functions.IntFuncs.*;
import xxl.content.Reference;

import java.io.Serializable;
import xxl.content.IntLit;
import xxl.content.StringLit;

/*
 * Class representing a detach visitor for the references.
 */
public class Detach implements Visitor, Serializable {
    
    public String visitReference(Reference c) {
        return c.getString();
    }

    public String visitADD(Add c) {
        return "";
    } 


    public String visitSUB(Sub c) {
        return "";
    } 


    public String visitMUL(Mul c) {
        return "";
    } 


    public String visitDIV(Div c) {
        return "";
    }


    public String visitAVERAGE(Average c) {
        return "";
    } 


    public String visitPRODUCT(Product c) {
        return "";
    } 


    public String visitCONCAT(Concat c) {
        return "";
    }


    public String visitCOALESCE(Coalesce c) {
        return "";
    }


    public String visitIntLit(IntLit c) {
        return "";

    }

    public String visitStringLit(StringLit c) {
        return "";
    }
} 