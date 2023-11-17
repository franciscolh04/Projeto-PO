package xxl.visitors;

import xxl.functions.BinFuncs.*;
import xxl.functions.IntFuncs.*;
import xxl.content.Reference;

import java.io.Serializable;
import xxl.content.IntLit;
import xxl.content.StringLit;
import xxl.structures.Content;

/*
 * Class representing an evaluator visitor for the references.
 */
public class Evaluator implements Visitor, Serializable {

    public String visitADD(Add c) {
        String _value = "#VALUE";
        String _arg1 = "";
        String _arg2 = "";

        _arg1 = c.getArgument(1).accept(this);
        _arg2 = c.getArgument(2).accept(this);

        if (c.validArgument(_arg1) && c.validArgument(_arg2)){
            _value =  "" + (Integer.parseInt(_arg1) + Integer.parseInt(_arg2));
        }

        c.setValue(_value);
        return _value;
    }

    public String visitSUB(Sub c) {
        String _value = "#VALUE";
        String _arg1 = "";
        String _arg2 = "";

        _arg1 = c.getArgument(1).accept(this);
        _arg2 = c.getArgument(2).accept(this);

        if (c.validArgument(_arg1) && c.validArgument(_arg2)){
            _value =  "" + (Integer.parseInt(_arg1) - Integer.parseInt(_arg2));
        }
        c.setValue(_value);

        return _value;
    }

    public String visitMUL(Mul c) {
        String _value = "#VALUE";
        String _arg1 = "";
        String _arg2 = "";

        _arg1 = c.getArgument(1).accept(this);
        _arg2 = c.getArgument(2).accept(this);

        if (c.validArgument(_arg1) && c.validArgument(_arg2)){
            _value =  "" + (Integer.parseInt(_arg1) * Integer.parseInt(_arg2));
        }
        c.setValue(_value);
  
        return _value;
    } 

    public String visitDIV(Div c) {
        String _value = "#VALUE";
        String _arg1 = "";
        String _arg2 = "";

        _arg1 = c.getArgument(1).accept(this);
        _arg2 = c.getArgument(2).accept(this);

        if (c.validArgument(_arg1) && c.validArgument(_arg2)){
            if(!_arg2.equals("0")) {
                _value =  "" + (Integer.parseInt(_arg1) / Integer.parseInt(_arg2));
            }
        }

        c.setValue(_value);
        return _value;
    }

    public String visitAVERAGE(Average c) {
        Content[] _range = c.getArguments();
        int average = 0;
        String[] values = new String[_range.length];

        for(int i = 0; i < _range.length; i++) {
            values[i] = _range[i].accept(this);
        }

        if(!c.validInterval(values)){
            c.setValue("#VALUE");
            return "#VALUE";
        }

        for(int i = 0; i < _range.length; i++) {
            average += Integer.parseInt(values[i]);
        }

        int _value =  average / _range.length;
        c.setValue("" + _value);
        return "" + _value;
    } 

    public String visitPRODUCT(Product c) {
        Content[] _range = c.getArguments();
        int _product = 1;
        String[] values = new String[_range.length];

        for(int i = 0; i < _range.length; i++) {
            values[i] = _range[i].accept(this);
        }

        if(!c.validInterval(values)){
            c.setValue("#VALUE");
            return "#VALUE";
        }

        for(int i = 0; i < _range.length; i++) {
            _product *= Integer.parseInt(values[i]);
        }

        c.setValue("" + _product);
        return "" + _product;
    } 

    public String visitCONCAT(Concat c) {
        Content[] _range = c.getArguments();
        String concat = "'";
        String[] values = new String[_range.length];

        for(int i = 0; i < _range.length; i++) {
            values[i] = _range[i].accept(this);
        }

        for(String value: values) {
            if (value.charAt(0) == '\'') {
                concat += value.substring(1);
            }
        }

        c.setValue(concat);
        return concat;
    } 

    public String visitCOALESCE(Coalesce c) {
        Content[] _range = c.getArguments();
        String[] values = new String[_range.length];
        String coalesce = "'";

        for(int i = 0; i < _range.length; i++) {
            values[i] = _range[i].accept(this);
        }

        for (String value: values) {
            if (value.charAt(0) == '\'') {
                coalesce = value;
                c.setValue(coalesce);
                return coalesce;
            }
        }
        c.setValue(coalesce);
        return coalesce;
    }

    public String visitIntLit(IntLit c) {
        return c.getValue();

    }

    public String visitStringLit(StringLit c) {
        return c.getValue();
    }

    public String visitReference(Reference  c) {
        String _value = "#VALUE";

        if(c.getContent()!= null) {
        _value =  c.getContent().accept(this);
        }

        c.setValue(_value);
        return _value;
    }
}