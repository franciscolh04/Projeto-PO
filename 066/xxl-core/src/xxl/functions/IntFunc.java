package xxl.functions;

import xxl.content.Function;
import xxl.structures.Content;

/**
 * Class representing an interval function.
 */
public abstract class IntFunc extends Function {

    public IntFunc(String funcName, Content arg1, Content arg2) {
        super( funcName + "(" + arg1.getString() + ":" + arg2.getString() + ")");
        
    }

    /**
     * Clones the content.
     * 
     * @return the cloned content.
     */
    public abstract Content clone();

    /**
     * Gets the arguments of the function.
     * 
     * @return the arguments of the function.
     */
    public abstract Content[] getArguments();

    /**
     * Verifies if the interval is valid.
     * 
     * @param interval the interval to check.
     * @return true if the interval is valid, false otherwise.
     */
    public Boolean validInterval(String[] interval) {
        for(String value: interval) {
            if ((!Character.isDigit(value.charAt(0)) && value.charAt(0) != '-')) {
                return false;
            }
        }
        return true;
    }

}
