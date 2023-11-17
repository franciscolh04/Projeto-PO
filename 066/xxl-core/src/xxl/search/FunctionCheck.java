package xxl.search;

/**
 * Class representing a predicate for searching a function.
 */
public class FunctionCheck implements SearchPredicate {

    private String _function = "";

    public FunctionCheck(String function) {
        _function = function;
    }

    @Override
    public boolean check(String function) {
        return function.contains(_function);
    }
}