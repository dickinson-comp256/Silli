import java.util.Map;

/**
 * Abstract class representing program statements. 
 *
 * There will be a sub-class of this class for each type of 
 * program statement in the langauge.  Each of these 
 * sub-classes will be able to:
 *   - recognize statements that it can execute
 *   - execute those statements
 */
public abstract class ProgramStatement {

    // The whole line as it appears in the program file
    protected String rawLine;              
    // The line number of this line in the program file
    protected int lineNumber;               
    // The statement on this line (rawLine with any label removed)
    protected String statement;             
    // Map from line label to line number
    protected Map<String,Integer> labelMap; 
    // Map from variable name to value
    protected Map<String,Integer> varMap;   

    /**
     * Factory method that creates and returns an object of 
     * the specific ProgramStatement subclass that will 
     * interpret the statement in the rawLine.
 
     * This method uses the static match(...) method in each 
     * subclass to find the one that is able to interpret the 
     * statement in the rawLine,
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

        /*
         * The rawLine contains the statement to be interpreted.  If that line
         * has a line label, then it will be in the rawLine varaible. Here,
         * we remove the line label so that we are just left with the Silli 
         * statement.  This makes it easier for the match(...) methods in the
         * subclass to determine if they can interpert the statement.
         */
        String statement = rawLine.trim();
        if (rawLine.contains(":")) {
            statement = rawLine.substring(rawLine.indexOf(":")+1).trim();
        }

        /* 
         * Use the static match(...) method in each ProgramStatement subclass 
         * to find the one that can interpret the Silli statement we are currently 
         * processing. When we do, create an instance of that subclass and return
         * it to the Interpreter. The Interpreter then uses the execute() method in
         * that subcalss to interpret the statement.
         */
        if (Blank.matches(statement)) {
            return new Blank(lineNumber, rawLine, labelMap, varMap);
        }
        else if (Comment.matches(statement)) {
            return new Comment(lineNumber, rawLine, labelMap, varMap);
        }
        else if (Declaration.matches(statement)) {
            return new Declaration(lineNumber, rawLine, labelMap, varMap);
        }
        // Add checks for additional statement types here...
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
