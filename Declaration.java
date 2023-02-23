import java.util.Map;

/*
 * Sub-class for representing and executing Declaration statements. 
 *
 * A Declaration statement is executed by adding the declared variable
 * and its value to the varMap. The program execution then continues on 
 * the next line.
 * 
 * @param lineNumber the line number on which this statement occurs in the program.
 * @param rawLine the raw text of the line as it was read from the source code.
 * @param varMap the current map that provides the value of a variable based on its name. 
 */
public class Declaration extends ProgramStatement {

    public static boolean matches(String statement) {
        // Any line starting "LET" is a Declaration statement.
        return statement.startsWith("LET");
    }

    public Declaration(int lineNumber, String rawLine, Map<String, Integer> labelMap, Map<String, Integer> varMap) {
        super(lineNumber, rawLine, labelMap, varMap);
    }

    public int execute() {
        // Find the variable name
        String var = statement.substring(4,5);
        // Find the value assigned to the varaible.
        String val = statement.substring(6,7);

        // Do some error checking...

        // A variable can only be declared once...
        if (varMap.containsKey(var)) {
            printError("The variable " + var + " is already defined.");
        }

        // The value must be an integer...
        try {
            int intVal = Integer.parseInt(val);

            // Everything looks good so execute the Declaration statement 
            // by adding the variable and its value to the varMap.
            varMap.put(var, intVal);
        }
        catch(NumberFormatException e) {
            printError("The variable " + var + " must have an integer value.");
        }

        // Now go to the next statement.
        int nextLine = lineNumber + 1;
        return nextLine;
    }
}
