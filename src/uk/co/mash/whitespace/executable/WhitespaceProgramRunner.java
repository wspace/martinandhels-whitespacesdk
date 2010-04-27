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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import uk.co.mash.whitespace.program.Program;
import uk.co.mash.whitespace.util.Constants;
import uk.co.mash.whitespace.util.ProgramLoader;
/**
 * Fairly Noddy implementation to run a Whitespace program from file within Java 
 * Runtime Environment. WJRE I calls it.
 * 
 * - Reads Whitespace program from file, 
 * - Parses, 
 * - Pre-processes, 
 * - Loads into a Program object, 
 * - Produces an optional program listing, and 
 * - Runs the bugger.
 * 
 * BTW: I've spent FAR too much time on this as a "project". 
 * Approx 12 hours and counting as of 24/04!
 *  
 * Few TODO's: Which I'm pretty sure are TODON'T. I Can't Be Arsed. 
 * 1. Stack is of type Long... Should be a binary of unlimited width.
 * 2. Keyboard IO. No direct access for char input.
 * 3. Loader not particularly efficient. Giant Strings and StringUtils everywhere. 
 *  
 * @author mash
 *
 */
public class WhitespaceProgramRunner {

    /** Main method. The Big One. 
     * Parses file, loads it into a Program Object,produces an optional program listing and runs it. 
     * <ol>
     * <li>arg[0]=filename. Fully qualified path of the Whitespace program source code to run please.</li>
     * <li>arg[1]=literal "list". If additional command line argument is set it will additionally output the
     * full program listing, and dump it to a file (original filename appended with "_listing")</li>
     * </ol>
     * @param args command line args.
     * 
     * 
     */
    public static void main(String[] args) throws IOException{

        FileReader inputStream = null;
        PrintWriter outputStream = null;
        String inputFile = null;
        String outputFile = null;
        boolean outputListing = false;
        ArrayList<Character> listOfChars = new ArrayList<Character>();
        

        if (args.length == 0) {
            System.out.println(OUTPUT_MAIN_ERROR);
            System.out.println();
            System.exit(0);
        } else {
            inputFile = args[0];
            if (args.length >1) {
                // output source code?
                if(StringUtils.equals(Constants.INPUT_MAIN_PARAM_LIST, args[1])) {
                    outputListing = true;
                }
            }
        }

        String name = StringUtils.substringBeforeLast(inputFile, Constants.OUTPUT_MAIN_FILE_SEPARATOR);
        if (outputListing) {
            outputFile = StringUtils.replace(inputFile,name,name+Constants.OUTPUT_MAIN_FILE_SUFFIX);
        }
        int c;
        try {
            inputStream = new FileReader(inputFile);
            if (outputListing) {
                outputStream = new PrintWriter(new FileWriter(outputFile));
            }
            StringBuilder rawBuildy = new StringBuilder();
            // if your name's not whitespace you're not coming in.
            while ((c = inputStream.read()) != -1) {
                if (c==9) {
                    rawBuildy.append((char) c);
                    listOfChars.add((char)c);
                } else if (c==32) {
                    rawBuildy.append((char)c);
                    listOfChars.add((char)c);
                } else if (c==10) {
                    rawBuildy.append((char)c);
                    listOfChars.add((char)c);
                } 
            }
            Program program = getWithTheProgram(rawBuildy.toString(), outputListing, outputStream);
            program.run();

        } finally {
            // tidy up.
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
       }
    }
    
    protected static Program getWithTheProgram(String whiteString,
            boolean outputListing, PrintWriter outputStream) {
        ProgramLoader loader =new ProgramLoader();
        Program program = loader.loadProgram(whiteString);
        // output listing?...
        if (outputListing) {
            for (String listing: program.programListing()) {
                System.out.println(listing);
                if(outputStream != null) {
                    outputStream.println(listing);
                 // ALWAYS remember to flush. To not do so would be "impolite".
                    outputStream.flush();
                }
            }
            // and if we've got an output file, output the source to that...
            // with a "magic" source-begin comment.
            if (outputStream != null) {
                outputStream.println(MAGIC_STRING_SOURCE_BEGIN);
                outputStream.println(whiteString);
                // don't forget to flush
                outputStream.flush();
            }
        }
        return program;
    }

    protected static final String MAGIC_STRING_SOURCE_BEGIN = "SOURCE:";
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    



























































    /* If I put this one way way way at the bottom do you think nobody will notice?..*/
    /** Output message if command line args is zero. */
    protected final static String OUTPUT_MAIN_ERROR = 
        "*********************************************************\n" +
        "*****                   ERROR!!!                    *****\n" +
        "*****  SENSE OF HUMOUR ALERT IN SECTOR 12 DETECTED  *****\n" +
        "***** PROGRAM ABORTED: INSTRUCTION - TERMINATE USER *****\n" +
        "*********************************************************\n\n";
    
}
