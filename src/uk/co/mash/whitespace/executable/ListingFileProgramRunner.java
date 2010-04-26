package uk.co.mash.whitespace.executable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import uk.co.mash.whitespace.program.Program;
import uk.co.mash.whitespace.util.Constants;

public class ListingFileProgramRunner extends WhitespaceProgramRunner {

    /** Special version of WhitespaceProgramRunner to be used with a "listing file.
     * The generated listing file is NOT a whitespace source file, it contains the 
     * source and comments.
     * 
     * Of course the comments and the listing themselves contain whitespace.
     * 2 options. Either fit the comments into the listing when creating the file,
     * which would be easy-ish to do, might be kind of fun, but might look a bit silly.
     * 
     * Or this way... The source in my listing file starts at the line <em>after</em> 
     * the MagicString "SOURCE:"
     *     * 
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        String inputFile = null;
        boolean sourceStarted = false;
        if (args.length == 0) {
            System.out.println(OUTPUT_MAIN_ERROR);
            System.exit(0);
        } else {
            inputFile = args[0];
        }
        BufferedReader inputStream = null;
        StringBuilder fileBuildy = new StringBuilder();
        try {
            inputStream = new BufferedReader(new FileReader(inputFile)); 
            String line = inputStream.readLine();
            while (line != null) {
                if(sourceStarted) {
                    // careful. readLine strips line-terminator whitespace!
                    fileBuildy.append(line);
                    fileBuildy.append(Constants.RAW_VALUE_LINEFEED);
                }
                if (StringUtils.equals(line, MAGIC_STRING_SOURCE_BEGIN)) {
                    sourceStarted = true;
                }
                line = inputStream.readLine();
            }
            Program program = getWithTheProgram(fileBuildy.toString(), true, null);
            program.run();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

}
