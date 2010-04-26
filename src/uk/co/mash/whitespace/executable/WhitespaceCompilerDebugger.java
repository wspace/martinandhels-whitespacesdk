package uk.co.mash.whitespace.executable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import uk.co.mash.whitespace.exception.CompilerException;
import uk.co.mash.whitespace.program.FullCommandType;
import uk.co.mash.whitespace.program.Program;
import uk.co.mash.whitespace.util.Constants;

/**
 * Absolutely ridiculous! Welcome to my League of Sadness...
 * 
 * SOOO we have our Whitespace Java Runtime Environment... 
 * What could be better now than a Human-Readable editor and compiler to make our IDE complete?..
 * 
 * I can't really think of many things less useful in this world.
 * Except maybe some of the Ronco devices advertised in the 70s. 
 * Like the "Ronco Buttoneer". Or the "Ronco Electric Plantpot Sharpener".
 * 
 * Q: Haven't you got anything better to do?
 * A: NO.
 * 
 * It's an assembler of sorts. A file converter.
 * It converts a file as series of whitespace-delimited Strings (oh the <em>irony</em>, 
 * and uses the internal FullCommandType enumeration to String-replace the enum value and 
 * properties from a Human-Readable line of format: 
 * <pre> 
 * [Command Param]
 * </pre>
 * Into it's whitespace command equivalent. e.g.
 * <pre>
 * Push 10010010
 * </pre>
 * becomes:
 * <pre>
 * 
 * </pre>
 *  
 * 
 * @author mash
 *
 */
public class WhitespaceCompilerDebugger {

    private static final String COMPILE_ERROR_MESSAGE = "Compile Error line:";
    
    private static boolean endReached;


    /**
     * @param args
     * @throws IOException norty norty. TODO: Should handle this better...
     */
    public static void main(String[] args) throws IOException {
        // init variables
        List<String> listOfCommands = new ArrayList<String>();
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;
        String inputFile = null;
        String outputFile = null;
        boolean runThis = false;
        boolean writeThis = false;
        // load command-line arguments
        if (args.length == 0) {
            System.out.println(WhitespaceProgramRunner.OUTPUT_MAIN_ERROR);
            System.exit(0);
        } else {
            inputFile = args[0];
            if (args.length >1) {
                // output compiled file?
                if(StringUtils.equals(Constants.INPUT_MAIN_PARAM_WRITE, args[1])) {
                    writeThis = true;
                }
            }
            String ext = StringUtils.substringAfterLast(inputFile, Constants.OUTPUT_MAIN_FILE_SEPARATOR);
            if (writeThis) {
                outputFile = StringUtils.replace(inputFile,ext,"wss");
            }
            if (args.length >2) {
                // output compiled file?
                if(StringUtils.equals(Constants.INPUT_MAIN_PARAM_RUN, args[2])) {
                    runThis = true;
                }
            }
        }
        // read file into a String ArrayList.
        try {
            inputStream = new BufferedReader(new FileReader(inputFile)); 
            String line = inputStream.readLine();
            while (line != null) {
                listOfCommands.add(line);
                line = inputStream.readLine();
            } 
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        // generate our whitespace command.
        String compiled = null;
        try {
            compiled = compile(listOfCommands);
        } catch (CompilerException ce) {
            System.out.println(ce.getMessage());
            System.exit(0);
        }
        // output to console and/or/file
        System.out.println(compiled);
        if (writeThis) {
            try {
                outputStream = new PrintWriter(new FileWriter(outputFile));
                outputStream.println(StringUtils.replace(compiled, Constants.RAW_VALUE_LINEFEED, System.getProperty("line.separator")));
                outputStream.flush();
            } finally {
                // tidy up.
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        }
        // option to run.
        Program program = WhitespaceProgramRunner.getWithTheProgram(compiled, true, null);
        List<String> errors = program.testThyself();
        if (!errors.isEmpty()){
            for (String message: errors) {
                System.out.println(message);
            }
        }
        if (runThis) {
            program.run();
        }
    }

    private static String compile(List<String> listOfCommands) throws CompilerException {
        StringBuilder whitespaceBuildy = new StringBuilder();
        int line=1;
        for (String command: listOfCommands) {
            // # is a comment
            if (!StringUtils.startsWith(command, "#")) {
                if (endReached) {
                    // oh dear. Warning.
                    throw new CompilerException(COMPILE_ERROR_MESSAGE+line+" ["+command+"] End command must be the last command in the program.");
                }
                String compiledLine = compileLine(command, line);
//              String debug = StringUtils.replace(compiledLine, Constants.RAW_VALUE_SPACE,Constants.HUMAN_READABLE_SPACE);
//              debug = StringUtils.replace(debug, Constants.RAW_VALUE_TAB,Constants.HUMAN_READABLE_TAB);
//              debug = StringUtils.replace(debug, Constants.RAW_VALUE_LINEFEED,Constants.HUMAN_READABLE_LINEFEED);
//              System.out.println(command+"="+debug);
                
                whitespaceBuildy.append(compiledLine);
            }
            line++;
        }
        return whitespaceBuildy.toString();
    }

    private static String compileLine(String command,int line) throws CompilerException {
        StringBuilder commandBuildy = new StringBuilder();
        String[] tokens = StringUtils.split(command);
        FullCommandType yourJustMyType = null;
        FullCommandType[] types = FullCommandType.class.getEnumConstants();
        for (FullCommandType type: types) {
            if (StringUtils.equalsIgnoreCase(tokens[0], type.toString())) {
                yourJustMyType = type;
                if (type.equals(FullCommandType.End)) {
                    endReached = true;
                }
                break;
            }
        }
        if (yourJustMyType == null) {
            throw new CompilerException(COMPILE_ERROR_MESSAGE+line+" command not recognised ["+tokens[0]+"]");
        }
        // add the command
        commandBuildy.append(yourJustMyType.getWhitespaceCommand());
        // handle param
        switch (yourJustMyType.getParameterType()) {
        case Null:
            break;
        case Label:
            // Follow Through. As it were.
            // originally I coded this as chars but they're not. All labels are technically numbers!
        case Number:
            if (tokens.length>1) {
                long numberParam = 0;
                // MAGIC command just for me.
                if (StringUtils.equalsIgnoreCase(tokens[1],"char") && tokens.length>2 && tokens[2].length()==1) {
                    // load char up as number!
                    numberParam = tokens[2].charAt(0);
                } else {
                    
                    /*
                     * if (StringUtils.containsOnly(tokens[1], "10")) {
                        // Oh bless. It's binary. Treat it as such.
                        numberParam = Long.parseLong(tokens[1],2);
                    } 
                    else 
                     */
                    if (StringUtils.containsOnly(tokens[1], "-0123456789")) {
                        numberParam = Long.parseLong(tokens[1]);
                    } else {
                        throw new CompilerException(
                                COMPILE_ERROR_MESSAGE+line+" parameters must be numeric. ["+tokens[1]+"] is invalid.");
                    }
                }
                if (numberParam > -1) {
                    commandBuildy.append(" ");
                } else {
                    commandBuildy.append("\t");
                }
                commandBuildy.append(convertBinaryString(numberParam));
            } else {
                throw new CompilerException(
                        COMPILE_ERROR_MESSAGE+line+" command ["+tokens[0]+"] expects a parameter");
            }
            break;

        
        }
        return commandBuildy.toString();
    }
    

    private static Object convertBinaryString(long number) {
        String binaryString = Long.toBinaryString(number);
        binaryString = StringUtils.replace(binaryString,
                Constants.RAW_VALUE_ZERO, Constants.RAW_VALUE_SPACE);
        binaryString = StringUtils.replace(binaryString,
                Constants.RAW_VALUE_ONE, Constants.RAW_VALUE_TAB);
        binaryString += Constants.RAW_VALUE_LINEFEED;
        return binaryString;
    }
    
    

}
