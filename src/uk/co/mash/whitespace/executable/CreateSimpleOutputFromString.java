/*
 * Java Whitespace SDK and Runtime Environment.
    
    Copyright (C) 2010 MArtin SHerratt 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */
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
