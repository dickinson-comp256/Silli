import java.util.Map;

/**
 * Sub-class for representing and executing Blank 
 * statements. 
 *
 * A Blank statement is executed simply by going to 
 * the statement on the next line.
 */
public class Blank extends ProgramStatement {

    /**
     * Determine if the statement provided can be 
     * interpreted by this ProgramStatement subclass.
     * 
     * In this case, that means determinig if the 
     * steatement is a Blank statement. 
     * 
     * @param statement a Silli languge statemet
     * @return true if this ProgramStatement subclass can 
     * interptet the statement and false otherwise.
     */
    public static boolean matches(String statement) {
        return statement.length() == 0;
    }

    /**
     * Construct a new Blank object that will be used to
     * interpret the Blank Silli statement in the rawLine.
     * 
     * @param lineNumber the line number on which this statement occurs in the program.
     * @param rawLine the raw text of the line as it was read from the source code.
     * @param labelMap the current map that provides the line number of a label based on its name.  
     * @param varMap the current map that provides the value of a variable based on its name. 
     */
    public Blank(
        int lineNumber, 
        String rawLine, 
        Map<String, Integer> 
        labelMap, Map<String, Integer> varMap) {
  
        /*
         * Call the super class constructor to assign the
         * Silli statement in the rawLine to be interpreted
         * by this object.
         */
        super(lineNumber, rawLine, labelMap, varMap);
    }

    /**
     * Perform the interpretation of the statement that 
     * has been assigned to this object.
     * 
     * Note that this object is a subclass of ProgramStatement
     * and thus may use the fields in its parent class.  
     * Those fields provide access to the:
     *    statement 
     *    lineNumber
     *    varMap
     *    labelMap
     * 
     * Those fields will be useful in performing the
     * interpretation of the statement.
     */
    public int execute() {
        // Nothing to do when executing a Blank line...

        // Just go to the next statement.
        int nextLine = lineNumber + 1;
        return nextLine;
    }
}