package xxl.search;

/**
 * Class representing a predicate for searching a value.
 */
public class ValueEqualsTo implements SearchPredicate {

    private String _value = "";

    public ValueEqualsTo(String value) {
        _value = value;
    }

    @Override
    public boolean check(String value) {
        return _value.equals(value);
    }
}
