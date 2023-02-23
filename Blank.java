import java.util.Map;

/**
 * Sub-class for representing and executing Blank statements. 
 *
 * A Blank statement is executed simply by going to the statement on the next line.
 */
public class Blank extends ProgramStatement {

    public static boolean matches(String statement) {
        return statement.length() == 0;
    }

    public Blank(int lineNumber, String rawLine, Map<String, Integer> labelMap, Map<String, Integer> varMap) {
        super(lineNumber, rawLine, labelMap, varMap);
    }

    public int execute() {
        // Nothing to do when executing a Blank line...

        // Just go to the next statement.
        int nextLine = lineNumber + 1;
        return nextLine;
    }
}