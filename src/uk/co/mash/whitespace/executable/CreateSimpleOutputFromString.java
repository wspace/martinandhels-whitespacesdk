package uk.co.mash.whitespace.executable;

import org.apache.commons.lang.StringUtils;

import uk.co.mash.whitespace.util.Constants;
/** Amusing class that reads string input and creates a whitespace program that outputs it. */
public class CreateSimpleOutputFromString {

    /**
     * @param args
     */
    public static void main(String[] args) {
        boolean outputReadable = false;
        if (args.length>0) {
            outputReadable = true;
        }
        StringBuilder programBuilder = new StringBuilder();
        String output = "                                                    " +
        		"Insert text here..." ;
        //get char array
        char[] charArray = output.toCharArray();
        for (char thing:charArray) {
            StringBuilder binaryBuilder = new StringBuilder();
            String binaryChar = Integer.toBinaryString(thing);
            binaryBuilder.append(binaryChar);
            String binaryString = binaryBuilder.toString();
            String whitespaceCodeString = StringUtils.replace(binaryString, Constants.RAW_VALUE_ZERO, Constants.RAW_VALUE_SPACE);
            whitespaceCodeString = StringUtils.replace(whitespaceCodeString, Constants.RAW_VALUE_ONE,Constants.RAW_VALUE_TAB);
            String readableWhitespaceCodeString = StringUtils.replace(binaryString, Constants.RAW_VALUE_ZERO, Constants.HUMAN_READABLE_SPACE);
            readableWhitespaceCodeString = StringUtils.replace(readableWhitespaceCodeString, Constants.RAW_VALUE_ONE, Constants.HUMAN_READABLE_TAB);
            // create code...
            // readable push-to-stack command
            if (outputReadable) {
                programBuilder.append(Constants.HUMAN_READABLE_SPACE);
                programBuilder.append(Constants.HUMAN_READABLE_SPACE);
                programBuilder.append(Constants.HUMAN_READABLE_SPACE);
                // readable binary char
                programBuilder.append(readableWhitespaceCodeString);
                // readable param terminator
                programBuilder.append(Constants.HUMAN_READABLE_LINEFEED);
            }
            // whitespace push-to-stack command
            programBuilder.append(Constants.RAW_VALUE_SPACE);
            programBuilder.append(Constants.RAW_VALUE_SPACE);
            programBuilder.append(Constants.RAW_VALUE_SPACE);
            // whitespace param
            programBuilder.append(whitespaceCodeString);
            // whitespace param terminator
            programBuilder.append(Constants.RAW_VALUE_LINEFEED);
            if (outputReadable) {
            // readable IO output command
                programBuilder.append(Constants.HUMAN_READABLE_TAB);
                programBuilder.append(Constants.HUMAN_READABLE_LINEFEED);
                programBuilder.append(Constants.HUMAN_READABLE_SPACE);
                programBuilder.append(Constants.HUMAN_READABLE_SPACE);
            }
            // whitespace IO output command
            programBuilder.append(Constants.RAW_VALUE_TAB);
            programBuilder.append(Constants.RAW_VALUE_LINEFEED);
            programBuilder.append(Constants.RAW_VALUE_SPACE);
            programBuilder.append(Constants.RAW_VALUE_SPACE);
        }
        // readable program end command
        if (outputReadable) {
            programBuilder.append(Constants.HUMAN_READABLE_LINEFEED);
            programBuilder.append(Constants.HUMAN_READABLE_LINEFEED);
            programBuilder.append(Constants.HUMAN_READABLE_LINEFEED);
        }
        // whitespace program end command
        programBuilder.append(Constants.RAW_VALUE_LINEFEED);
        programBuilder.append(Constants.RAW_VALUE_LINEFEED);
        programBuilder.append(Constants.RAW_VALUE_LINEFEED);
        System.out.println(programBuilder.toString());

    }

}
