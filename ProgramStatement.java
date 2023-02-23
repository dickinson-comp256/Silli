import java.util.Map;

/**
 * Abstract class representing program statements. 
 *
 * There will be a sub-class of this class for each type of program statement in the langauge.  
 * Each of these sub-classes will be able to:
 *   - recognize statements that it can execute
 *   - execute those statements
 */
public abstract class ProgramStatement {

	protected String rawLine;               // The whole line as it appears in the program file
    protected int lineNumber;               // The line number of this line in the program file
    protected String statement;             // The statement on this line (rawLine with any label removed)
    protected Map<String,Integer> labelMap; // Map from line label to line number
    protected Map<String,Integer> varMap;   // Map from variable name to value

    /**
     * Factory method that returns an object of the specific ProgramStatement sub-class
     * that matches the statement in the rawLine.
     * 
     * @param lineNumber the line number on which this statement occurs in the program.
     * @param rawLine the raw text of the line as it was read from the source code.
     * @param labelMap the current map that provides the line number of a label based on its name. 
     * @param varMap the current map that provides the value of a variable based on its name. 
     * @return a ProgramStatement subclass object for the given line of code.
     */
    public static ProgramStatement getStatement(
        int lineNumber, 
        String rawLine, 
        Map<String,Integer> labelMap, 
        Map<String,Integer> varMap) {

        // If the line contains a label remove it before matching it to a statement type.
        String statement = rawLine.trim();
        if (rawLine.contains(":")) {
            statement = rawLine.substring(rawLine.indexOf(":")+1).trim();
        }

        // Find and return the type of statement that we have...
        if (Blank.matches(statement)) {
            return new Blank(lineNumber, rawLine, labelMap, varMap);
        }
        else if (Comment.matches(statement)) {
            return new Comment(lineNumber, rawLine, labelMap, varMap);
        }
        else if (Declaration.matches(statement)) {
            return new Declaration(lineNumber, rawLine, labelMap, varMap);
        }
        // Add more statement types here...
        else {
            return null;    // No matching statement type was found.
        }
    }

    /**
     * Construct a new ProgramStatement.
     * 
     * @param lineNumber the line number on which this statement occurs in the program.
     * @param rawLine the raw text of the line as it was read from the source code.
     * @param labelMap the current map that provides the line number of a label based on its name.  
     * @param varMap the current map that provides the value of a variable based on its name. 
     */
    public ProgramStatement(
        int lineNumber, 
        String rawLine, 
        Map<String,Integer> labelMap, 
        Map<String,Integer> varMap) {

        this.lineNumber = lineNumber;
        this.rawLine = rawLine;
        this.labelMap = labelMap;
        this.varMap = varMap;

        // If the line starts with a label, extract the label and add its value to the varMap.
		if (rawLine.contains(":")) {
			String lineLabel = rawLine.substring(0, rawLine.indexOf(':'));
            
            if (labelMap.containsKey(lineLabel)) {
                /* 
                 * Each line label must be unique.  If the label is already in the 
                 * varMap, it must also have been defined earlier in the program.  If there
                 * are two lines with the saame label, then a conditional would not
                 * know which one to GOTO, so this is an error.
                 */
                printError("The label " + lineLabel + " is already defined.");
                System.exit(-1);
            }
            else {
                // The label is not already defined so add it to the varMap.
                labelMap.put(lineLabel, lineNumber);
                // The line had a label, so everything after the label is the statement.
                statement = rawLine.substring(rawLine.indexOf(':')+1).trim();
            }
		}
        else {
            // There is no label on this line so the entire line is the statement.
            statement = rawLine.trim();
        }
    }

    /**
     * Print an error message.
     * 
     * @param message the error message to be printed.
     */
    protected void printError(String message) {
        System.err.println("Error on Line " + lineNumber);
        System.err.println("  " + rawLine);
        System.err.println("  " + message);
        System.err.println();
        System.exit(-1);
    }

    /*
     * Generate a string representation of the line.
     * 
     * @return a string containing the line number and the text of the line.
     */
    public String toString() {
        return lineNumber + "\t| " + rawLine;
    }

    /**
     * Execute the statement using the varMap.
     *
     * @return the line number of the next statement to be executed.
     */
    public abstract int execute();   
}
