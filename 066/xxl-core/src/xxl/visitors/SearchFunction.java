package xxl.visitors;

import xxl.functions.BinFuncs.*;
import xxl.functions.IntFuncs.*;
import xxl.content.Reference;

import java.io.Serializable;
import xxl.content.IntLit;
import xxl.content.StringLit;
import xxl.search.FunctionCheck;
import xxl.search.SearchPredicate;

/*
 * Class representing a search function visitor for the references.
 */
public class SearchFunction implements Visitor, Serializable {
    
    private String _name = "";

    public SearchFunction(String name) {
        _name = name;
    }
   
    public String visitReference(Reference c) {
        return "";
    }

    public String visitADD(Add c) {
        String flag = "false";
        SearchPredicate _predicate = new FunctionCheck(_name);
        if (_predicate.check("ADD"))
            flag = "true";
        return flag;
    }

    public String visitSUB(Sub c) {
        String flag = "false";
        SearchPredicate _predicate = new FunctionCheck(_name);
        if (_predicate.check("SUB"))
            flag = "true";
        return flag;
    }

    public String visitMUL(Mul c) {
        String flag = "false";
        SearchPredicate _predicate = new FunctionCheck(_name);
        if (_predicate.check("MUL"))
            flag = "true";
        return flag;
    }

    public String visitDIV(Div c) {
        String flag = "false";
        SearchPredicate _predicate = new FunctionCheck(_name);
        if (_predicate.check("DIV"))
            flag = "true";
        return flag;
    }

    public String visitAVERAGE(Average c) {
        String flag = "false";
        SearchPredicate _predicate = new FunctionCheck(_name);
        if (_predicate.check("AVERAGE"))
            flag = "true";
        return flag;
    }

    public String visitPRODUCT(Product c) {
        String flag = "false";
        SearchPredicate _predicate = new FunctionCheck(_name);
        if (_predicate.check("PRODUCT"))
            flag = "true";
        return flag;
    }

    public String visitCONCAT(Concat c) {
        String flag = "false";
        SearchPredicate _predicate = new FunctionCheck(_name);
        if (_predicate.check("CONCAT"))
            flag = "true";
        return flag;
    }

    public String visitCOALESCE(Coalesce c) {
        String flag = "false";
        SearchPredicate _predicate = new FunctionCheck(_name);
        if (_predicate.check("COALESCE"))
            flag = "true";
        return flag;
    }

    public String visitIntLit(IntLit c) {
        return "";

    }

    public String visitStringLit(StringLit c) {
        return "";
    } 
}