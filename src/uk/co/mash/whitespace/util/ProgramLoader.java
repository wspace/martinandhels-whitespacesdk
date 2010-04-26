package uk.co.mash.whitespace.util;

import org.apache.commons.lang.StringUtils;

import uk.co.mash.whitespace.program.Command;
import uk.co.mash.whitespace.program.FullCommandType;
import uk.co.mash.whitespace.program.Program;
/**
 * Class to load a Whitespace Program object from a String.
 * Not particularly efficient. Considering Whitespace programs can be thousands of lines long -  
 * typically 15 bytes to output a single char - 
 * This beast has to handle giant Strings and uses StringUtils everywhere.
 * 
 * @author mash
 *
 */
public class ProgramLoader {

    String programString;
    String[] commandStrings;
    Program program = new Program();

    /**
     * Does what it says on the tin. Reads a Whitespace Program as a String and 
     * returns a program object.
     * 
     * @param programString full whitespace program as String.
     * @return Whitespace Program object.
     */
    public Program loadProgram(String programString) {
        this.programString = programString;

        commandStrings = program.getListOfCommandStrings();
        int currentPosition = 0;
        do {
            currentPosition = loadNextCommand(currentPosition);
        } while (currentPosition >= 0);
        return program;
    }

    private int loadNextCommand(int currentPosition) {
        int nextCommandPos = -1;
        String thisCommandString = StringUtils.EMPTY;
        String stringParam = null;
        FullCommandType fullCommandType = FullCommandType.NullOp;
        String subby = StringUtils.substring(programString, currentPosition);
        nextCommandPos = StringUtils.indexOfAny(subby, commandStrings);
        subby = StringUtils.substring(subby, nextCommandPos);
        String rootCommand = null;
        // which one is it?..
        for (String prefix : commandStrings) {
            if (StringUtils.startsWith(subby, prefix)) {
                subby = StringUtils.substringAfter(subby, prefix);
                fullCommandType = getFullCommandType(prefix, subby);
                rootCommand = prefix;
                break;
            }
        }
        // Okies, so I know what type I am, now I need to assess parameters and
        // read ahead...
        switch (fullCommandType.getParameterType()) {
        case Label:
        case Number:
            // bugger, we need to read ahead and grab the params.
            // get the full program string label is terminated by newline

            stringParam = StringUtils.substringBetween(programString,
                    rootCommand, Constants.RAW_VALUE_LINEFEED);
            thisCommandString += stringParam;
            thisCommandString += Constants.RAW_VALUE_LINEFEED;
            // extract params

            break;

        case Null:
        default:
            // we're done. Load it up. Stick a fork in its ass.
            break;
        }
        // we've set up the params, so load it up my friend...
        // reset the program string

        // nextCommandPos=StringUtils.indexOf(programString,
        // thisCommandString)+thisCommandString.length();
        // load the command into Program
        programString = StringUtils.substringAfter(subby, thisCommandString);
        Command newCommand = program.getCommandInstance(fullCommandType,
                stringParam);
        program.addCommand(newCommand);
        if (fullCommandType.equals(FullCommandType.End)) {
            nextCommandPos = -1;
        }
        // load the String into the commandString array

        return nextCommandPos;
    }

    private FullCommandType getFullCommandType(String whitespace,
            String restOfProgramString) {
        return FullCommandType.getFullCommandTypeForWhitespace(whitespace);
    }
}
