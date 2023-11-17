package xxl.functions;

import xxl.content.Function;
import xxl.structures.Content;

/**
 * Class representing a binary function.
 */
public abstract class BinFunc extends Function {
    
    public BinFunc(String funcName, Content arg1, Content arg2) {
        super( funcName + "(" + arg1.getString() + "," + arg2.getString() + ")");
    }

    public abstract Content clone();

    /**
     * Gets the argument of the function.
     * 
     * @param num the number of the argument (1 or 2).
     * @return the argument of the function.
     */
    public abstract Content getArgument(int num);

    /**
     * Calculates the value of the function.
     * 
     * @param value1 the first value of the function.
     * @param value2 the second value of the function.
     * @return the value of the function.
     */
    public abstract String calculate(String value1, String value2);

    /**
     * Checks if the argument is valid.
     * 
     * @param arg the argument to check.
     * @return true if the argument is valid, false otherwise.
     */
    public Boolean validArgument(String arg) {
        return ((Character.isDigit(arg.charAt(0)) || arg.charAt(0) == '-'));
    }

}
