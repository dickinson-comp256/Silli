import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * An interpreter for the SILLI language.
 */
public class Interpreter {

    private String sourceFile;
    private List<ProgramStatement> theProgram;
    private Map<String,Integer> varMap;
    private Map<String,Integer> labelMap;

    private static boolean DEBUG = false;

    public static void main(String[] args) {

		if (args.length == 0 || 
            args.length > 2 || 
            (args.length == 1 && args[0].equals("-debug")) ||
            (args.length == 2 && !args[0].equals("-debug"))) {
            System.out.println("Usage:  java Interpreter [-debug] <source file> ");
            System.out.println("  <source file>: Path to the program to be run by the interpreter.");
			System.exit(-1);
		}

        Interpreter elena = null;
        String programName = null;
        if (args[0].equals("-debug")) {
            DEBUG = true;
            programName = args[1];
        }
        else {
            programName = args[0];
        }

        elena = new Interpreter(programName);
        elena.buildProgram();

        if (DEBUG) {
            System.out.println();
            System.out.println("Interpreting Program: " + programName);
            System.out.println();
            System.out.println("Program Source Code:");
            elena.printProgram();

            if (elena.labelMap.size() > 0) {
                System.out.println();
                System.out.println("Line Labels:");
                System.out.println();
                elena.printLabelMap();
            }

            System.out.println();
            System.out.println("Output from Program Execution:");
            System.out.println();
        }

        elena.executeProgram();

        if (DEBUG) {
            System.out.println();
            System.out.println("Final Variable Map:");
            System.out.println();
            elena.printVariableMap();
        }

        /*
         * Note: The variable name elena for the Interpreter is inspired by Elena Kidd 
         * who was an interpreter for Mikhail Gorbachev.
         * Learn more:
         * https://www.theguardian.com/world/2014/jul/25/interpreters-world-events-gorbachev-reagan-deng-xiaoping-ahmadinejad
         */
    }

    private Interpreter(String filename) {
        sourceFile = filename;
        theProgram = new ArrayList<ProgramStatement>();
        varMap = new HashMap<String,Integer>();
        labelMap = new HashMap<String,Integer>();
    }

    /**
     * Read all of the program lines from the file and convert them into
     * ProgramStatement objects.  This also builds the labelMap that maps
     * line labels to the line number on which they occured.
     */
    private void buildProgram() {
        try {
            /*
            * Add a filler line so index 1 is our first program instruction. This way line
            * 1 of the program will be stored at index 1. That will simplify error
            * reporting,
            */
            theProgram.add(ProgramStatement.getStatement(0, "# Filler", labelMap, varMap));

			/*
			 * Read the lines from program, convert each to a ProgramStatement object
             * and add it to the program...
			 */
			File programFile = new File(sourceFile);
			Scanner scr = new Scanner(programFile);
			int lineNum = 1;
			while (scr.hasNext()) {
				String rawLine = scr.nextLine().toUpperCase();
                ProgramStatement inst = ProgramStatement.getStatement(lineNum, rawLine, labelMap, varMap);
                if (inst == null) {
                    // There was no ProgramStatement subclass matching the rawLine.
                    System.err.println("Syntax error on Line " + lineNum);
                    System.err.println("  " + rawLine);
                    System.err.println("   Unrecognized statement.");
                    System.err.println();
                    System.exit(-1);
                }
				theProgram.add(inst);
				lineNum++;
			}
			scr.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: " + sourceFile + " not found.");
			System.exit(-1);
		}
    }

    private void printProgram() {
        for(int i=1; i<theProgram.size(); i++) {
            System.out.println(theProgram.get(i));
        }
    }

    private void printLabelMap() {
        System.out.println("Label\tValue");
        System.out.println("---------------");
        for(String label : labelMap.keySet()) {
            System.out.println(label + "\t" + labelMap.get(label));
        }
    }

    private void printVariableMap() {
        System.out.println("Var\tValue");
        System.out.println("---------------");
        for(String var : varMap.keySet()) {
            System.out.println(var + "\t" + varMap.get(var));
        }
    }

    private void executeProgram() {
        int curLine = 1;
        while (curLine < theProgram.size()) {
            curLine = theProgram.get(curLine).execute();
        }
    }
}
