import java.util.Map;

/*
 * Sub-class for representing and executing Comment statements. 
 *
 * A Comment statement is executed simply by going to the statement on the next line.
 */
public class Comment extends ProgramStatement {

    public static boolean matches(String statement) {
        // Any line starting with a # is a comment.
        return statement.startsWith("#");
    }

    public Comment(int lineNumber, String rawLine, Map<String, Integer> labelMap, Map<String, Integer> varMap) {
        super(lineNumber, rawLine, labelMap, varMap);
    }

    public int execute() {
        // Nothing to do when executing a Comment line...

        // Just go to the next statement.
        int nextLine = lineNumber + 1;
        return nextLine;
    }
}
