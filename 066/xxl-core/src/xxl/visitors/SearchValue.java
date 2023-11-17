package xxl.visitors;

import xxl.functions.BinFuncs.*;
import xxl.functions.IntFuncs.*;
import xxl.content.Reference;

import java.io.Serializable;
import xxl.content.IntLit;
import xxl.content.StringLit;
import xxl.search.ValueEqualsTo;
import xxl.search.SearchPredicate;

/*
 * Class representing a search value visitor for the references.
 */
public class SearchValue implements Visitor, Serializable {
    private String _value = "";

    public SearchValue(String value) {
        _value = value;
    }

   
    public String visitReference(Reference c) {
        String flag = "false";
        SearchPredicate _predicate = new ValueEqualsTo(_value);
        if (_predicate.check(c.getRefValue()))
            flag = "true";
        return flag;
    }

    public String visitADD(Add c) {
        String flag = "false";
        SearchPredicate _predicate = new ValueEqualsTo(_value);
        if (_predicate.check(c.getValue()))
            flag = "true";
        return flag;
    } 


    public String visitSUB(Sub c) {
        String flag = "false";
        SearchPredicate _predicate = new ValueEqualsTo(_value);
        if (_predicate.check(c.getValue()))
            flag = "true";
        return flag;
    } 


    public String visitMUL(Mul c) {
        String flag = "false";
        SearchPredicate _predicate = new ValueEqualsTo(_value);
        if (_predicate.check(c.getValue()))
            flag = "true";
        return flag;
    } 


    public String visitDIV(Div c) {
        String flag = "false";
        SearchPredicate _predicate = new ValueEqualsTo(_value);
        if (_predicate.check(c.getValue()))
            flag = "true";
        return flag;
    }


    public String visitAVERAGE(Average c) {
        String flag = "false";
        SearchPredicate _predicate = new ValueEqualsTo(_value);
        if (_predicate.check(c.getValue()))
            flag = "true";
        return flag;
    } 


    public String visitPRODUCT(Product c) {
        String flag = "false";
        SearchPredicate _predicate = new ValueEqualsTo(_value);
        if (_predicate.check(c.getValue()))
            flag = "true";
        return flag;
    } 


    public String visitCONCAT(Concat c) {
        String flag = "false";
        SearchPredicate _predicate = new ValueEqualsTo(_value);
        if (_predicate.check(c.getValue()))
            flag = "true";
        return flag;
    }


    public String visitCOALESCE(Coalesce c) {
        String flag = "false";
        SearchPredicate _predicate = new ValueEqualsTo(_value);
        if (_predicate.check(c.getValue()))
            flag = "true";
        return flag;
    }


    public String visitIntLit(IntLit c) {
        String flag = "false";
        SearchPredicate _predicate = new ValueEqualsTo(_value);
        if (_predicate.check(c.getValue()))
            flag = "true";
        return flag;

    } 


    public String visitStringLit(StringLit c) {
        String flag = "false";
        SearchPredicate _predicate = new ValueEqualsTo(_value);
        if (_predicate.check(c.getValue()))
            flag = "true";
        return flag;
    } 
}