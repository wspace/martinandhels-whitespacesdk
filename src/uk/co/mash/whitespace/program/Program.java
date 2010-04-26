package uk.co.mash.whitespace.program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
/**
 * Java implementation of runnable Whitespace program that runs within the JVM.
 * 
 * Combined with the ProgramLoader, WhitespaceProgramRunner and supporting 
 * java Objects in the program package this represents the Java Whitespace Runtime Environment,
 * or "JWRE" as I've decided to call it.
 * 
 * @author mash
 *
 */
public class Program implements Runnable{
    
    /**
     * Default constructor. Create and initialize.
     */
    public Program() {
        super();
        init();
    }

    /*
     *  All the things it needs...
     *  A reference to main.
     *  An ordered List of Commands.
     *  Current Command 
     *  From Command
     */
    /** Sum of All Commands.*/
    ArrayList<Command> listOfCommands = new ArrayList<Command>();
    /** Internal Stack Object. */
    Stack<Long> flatPackStack = new Stack<Long>();
    /** Internal dumping ground for stuff acting as a Heap.*/
    private Map<Long,Long> dump = new HashMap<Long, Long>();
    /** "Current" command that's running. Used to holds state of where we are in the program.*/
    int currentCommand =0;
    /** "From" command. Where it came from. Used by subroutines for the return or end-sub command.*/
    int fromCommand =-1;
    /** Convenience method to add a command to the list used by the program. */
    public void addCommand(Command command) {
        listOfCommands.add(command);
    }
    /**
     * Run that thing.
     * Outputs some stuff to let you know it's actually started, calls the 
     * first command then lets recursive calls to process() take over.
     * 
     */
    public void run() {
        System.out.println("*** Program Start ***"+
                System.getProperty("line.separator"));
        // commands need a reference to their controlling program.  
        listOfCommands.get(0).setProgramRef(this);
        listOfCommands.get(0).process();
        // all process methods are recursive!

    }
    /**
     * Increments current command, gets it as the next one, and re-sets the Program reference on the next command. 
     * @return the next Command Object to process.
     */
    public Command getNextCommand() {
        currentCommand++;
        Command nextCommand = listOfCommands.get(currentCommand);
        nextCommand.setProgramRef(this);
        return nextCommand;
    }
    /**
     * Overloaded method used exclusively by FlowCommand. 
     * 
     * @param next the actual command to use next instead of the incremented one.
     * @return the next Command Object to process.
     */
    public Command getNextCommand(int next) {
        currentCommand = next;
        Command nextCommand = listOfCommands.get(currentCommand);
        nextCommand.setProgramRef(this);
        return nextCommand;
    }
    /** 
     * Getter for internal Stack Object.
     * @return the internal Stack Object used by this Program.
     */
    public Stack<Long> getFlatPackStack() {
        return flatPackStack;
    }
    /** 
     * Getter for internal Heap Object.
     * @return the internal Heap Object used by this Program.
     */
    public Map<Long, Long> takeDump() {
        return dump;
    }

    /** 
     * Setter for internal Heap Object.
     * @param dump the internal Heap Object used by this Program. 
     */
    public void pitchALoaf(Map<Long, Long> dump) {
        this.dump = dump;
    }
    
    /** Gets the index of a Label command for a given String label.
     * Used exclusively by Flow Commands for it's icky Goto Gosub commands etc.
     * 
     * @param label The String label held in an internal Label Command.
     * @return the actual index in the list of Commands where the Label Command is held.
     */
    public int getIndexForLabel(String label) {
        // initialize this to the End command just in case.
        int index = 0; 
        for (Command command: listOfCommands) {
            if (command.getFullCommandType().equals(FullCommandType.Label)) {
                if (StringUtils.equals(command.getLabel(),label)) {
                    break;
                }
                
            }
            index++;
        }
        return index;
    }

    /** 
     * Getter for the current Command Object being processed.
     * @return the current command being processed.
     */
    public int getCurrentCommand() {
        return currentCommand;
    }

    
    /**
     * Getter for the index of which Command Object this came from.
     * @return the index of which Command Object this came from.
     */
    public int getFromCommand() {
        return fromCommand;
    }

    /**
     * Setter for the index of which Command Object this came from.
     * @param fromCommand the index of which Command Object this came from.
     */
    public void setFromCommand(int fromCommand) {
        this.fromCommand = fromCommand;
    }

    /**
     * The meat of the loader that generates actual Command instances.
     * 
     * @param fullCommandType Enumerated object that contains all the properties required by 
     *                          a specific Command instance.
     * @param paramsAsWhitespace Raw param for this command. As whitespace. 
     * @return concrete instance of the required Command Object.
     */
    public Command getCommandInstance(FullCommandType fullCommandType, String paramsAsWhitespace) {
        Command command = null;
        switch (fullCommandType.getCommandType()) {
        case Arithmetic: 
            command = new ArithmeticCommand(fullCommandType, paramsAsWhitespace);
            break;
            
        case Dump:
            command = new DumpCommand(fullCommandType, paramsAsWhitespace); 
            break;
            
        case Flow:
            command = new FlowCommand(fullCommandType, paramsAsWhitespace);
            break;
            
        case IO:
            command = new IOCommand(fullCommandType, paramsAsWhitespace);
            break;
            
        case Stack:
            command = new StackCommand(fullCommandType, paramsAsWhitespace); 
            break;
        }
        command.setRawParam(paramsAsWhitespace);
        return command;
    }


    /** Valid whitespace commands. 
     * Useful as debug in development to see what gets loaded.
     * Originally I thought these this static list could act as template -  
     * Dump 'em in a static cache and just bolt the params onto it to generate new 
     * instances... 
     * but I'm not so sure now.  
     */
    public static List<Command> REFERENCE_COMMANDS;
    
    /**
     * Get a list of the reference commands. Seems strangely pointless now...
     * 
     * @return Template list of all possible command types. 
     */
    public List<Command> getListOfReferenceCommands() {
        return REFERENCE_COMMANDS;
    }
    
    /**
     * Full list of the actual command strings for all avaialable Whitespace Commands.
     * 
     * @return list of valid command strings (as whitespace).
     */
    public String[] getListOfCommandStrings() {
        List<String> commandStrings = new ArrayList<String>();
        for (FullCommandType type: FullCommandType.class.getEnumConstants()) {
            if (!type.equals(FullCommandType.NullOp)) {
                commandStrings.add(type.getWhitespaceCommand());
            }
        }
        String[] arrayOfCommands = new String[commandStrings.size()];
        return commandStrings.toArray(arrayOfCommands);
    }
    /**
     * Not particularly configurable representation of a program listing 
     * for convenience. 
     * As in pseudocode-ish comments on the listing,  
     * and also a typable "how to create this program" 
     * 
     * This is in the tradition of good old ZX magazine "free programs"
     *  
     * [as long as you could be arsed spending 2 hours typing them in. 
     * And assuming it didn't crash]
     * 
     * @return per-command list of strings that represent a human-readable traditional program listing   
     */
    public List<String> programListing() {
        List<String> listing = new ArrayList<String>();
        int line = 1;
        for (Command command: listOfCommands) {
            StringBuilder listBuildy = new StringBuilder();
            listBuildy.append("#");
            listBuildy.append(line);
            listBuildy.append(": ");
            listBuildy.append(command.describe());
            listBuildy.append(System.getProperty("line.separator"));
            listBuildy.append("#");
            listBuildy.append(command.getHumanReadableCommand());
            listBuildy.append(command.humanReadableParams());
            listing.add(listBuildy.toString());
            line++;
        }
        return listing;
    }
    
    public List<String> testThyself() {
        // set em up for later.
        //List<String> warnings = new ArrayList<String>();
        List<String> catalogueOfErrors = new ArrayList<String>();
        List<String> listOfLabels = new ArrayList<String>();
        List<Command> commandsUsingLabels = new ArrayList<Command>();
        // check for "NullOp" loaded. It indicates an unrecognisable command.
        if (!getCommandsOfCommandType(FullCommandType.NullOp).isEmpty()) {
            catalogueOfErrors.add("ERROR: Unrecognisable command loaded as \"NullOp\". Normally indicates corrupt source file - check listing.");
        }
        for (Command command : getCommandsOfCommandType(FullCommandType.Label)) {
            listOfLabels.add(command.getLabel());
        }
        for (Command command : getCommandsOfParamType(ParameterType.Label)) {
            if (command.getFullCommandType()!=FullCommandType.Label) {
                commandsUsingLabels.add(command);
            }
        }
        // match the command using a label with an actual label.
        for (Command command: commandsUsingLabels) {
            if (!listOfLabels.contains(command.getLabel())) {
                // oooh, you're on the naughty list.
                catalogueOfErrors.add("ERROR: Command ["+command.getFullCommandType()+"]declares label ["+command.getLabel()+"/"+String.valueOf(Long.parseLong(command.getLabel(),2))+"] that does not exist.");
            }
        }
        // see if any labels are unused.
        for (String label: listOfLabels) {
            boolean gotOne = false;
            for (Command command: commandsUsingLabels) {
                if (StringUtils.equals(label, command.getLabel())){
                    gotOne=true;
                    break;
                }
            }
            if (!gotOne) {
                catalogueOfErrors.add("WARN: Label ["+label+"/"+String.valueOf(Long.parseLong(label,2))+"] is not used");
            }
        }
        
        return catalogueOfErrors;
    }
    
    public List<Command> getCommandsOfCommandType(FullCommandType type) {
        List<Command> list = new ArrayList<Command>();
        for (Command command: listOfCommands){
            if (command.getFullCommandType() == type) {
                list.add(command);
            }
        }
        return list;
    }
    
    public List<Command> getCommandsOfParamType(ParameterType type) {
        List<Command> list = new ArrayList<Command>();
        for (Command command: listOfCommands){
            if (command.getParameterType() == type) {
                list.add(command);
            }
        }
        return list;
    }
    /** Initialize reference commands. AKA "set 'em up for later..." */
    public void init() {
        List<Command> temp = new ArrayList<Command>();
        /*
IMP Meaning
[Space] Stack Manipulation

Command         Parameters  Meaning
[Space]         Number      Push the number onto the stack
[LF][Space]     -           Duplicate the top item on the stack
[Tab][Space]    Number      Copy the nth item on the stack (given by the argument) onto the top of the stack
[LF][Tab]       -           Swap the top two items on the stack
[LF][LF]        -           Discard the top item on the stack
[Tab][LF]       Number      Slide n items off the stack, keeping the top item
         */
        temp.add(getCommandInstance(FullCommandType.Push, null));
        temp.add(getCommandInstance(FullCommandType.Duplicate, null));
        temp.add(getCommandInstance(FullCommandType.Copy, null));
        temp.add(getCommandInstance(FullCommandType.Swap, null));
        temp.add(getCommandInstance(FullCommandType.Discard, null));
        temp.add(getCommandInstance(FullCommandType.Slide, null));
        
        /*
IMP             Meaning
[Tab][Space]    Arithmetic

Command         Parameters  Meaning
[Space][Space]  -           Add
[Space][Tab]    -           Subtract
[Space][LF]     -           Multiply
[Tab][Space]    -           Integer Divide
[Tab][Tab]      -           Mod
         */
        temp.add(getCommandInstance(FullCommandType.Add, null));
        temp.add(getCommandInstance(FullCommandType.Subtract, null));
        temp.add(getCommandInstance(FullCommandType.Multiply, null));
        temp.add(getCommandInstance(FullCommandType.Divide, null));
        temp.add(getCommandInstance(FullCommandType.Mod, null));
        
        /*
IMP             Meaning
[Tab][Tab]      Heap access

Command Parameters  Meaning
[Space] -   Store
[Tab]   -   Retrieve
         */
        temp.add(getCommandInstance(FullCommandType.Store, null));
        temp.add(getCommandInstance(FullCommandType.Retrieve, null));
        
        
        /*
IMP             Meaning
[LF]            Flow Control

Command         Parameters  Meaning
[Space][Space]  Label       Mark a location in the program
[Space][Tab]    Label       Call a subroutine
[Space][LF]     Label       Jump unconditionally to a label
[Tab][Space]    Label       Jump to a label if the top of the stack is zero
[Tab][Tab]      Label       Jump to a label if the top of the stack is negative
[Tab][LF]       -           End a subroutine and transfer control back to the caller
[LF][LF]        -           End the program
         */
        temp.add(getCommandInstance(FullCommandType.Label, null));
        temp.add(getCommandInstance(FullCommandType.Gosub, null));
        temp.add(getCommandInstance(FullCommandType.Goto, null));
        temp.add(getCommandInstance(FullCommandType.GotoIfZero, null));
        temp.add(getCommandInstance(FullCommandType.GotoIfNegative, null));
        temp.add(getCommandInstance(FullCommandType.EndSub, null));
        temp.add(getCommandInstance(FullCommandType.End, null));
        
        
        /*
IMP             Meaning
[Tab][LF]       I/O

Command Parameters  Meaning
[Space][Space]  -   Output the character at the top of the stack
[Space][Tab]    -   Output the number at the top of the stack
[Tab][Space]    -   Read a character and place it in the location given by the top of the stack
[Tab][Tab]  -   Read a number and place it in the location given by the top of the stack
         */
        temp.add(getCommandInstance(FullCommandType.OutputChar, null));
        temp.add(getCommandInstance(FullCommandType.OutputNumber, null));
        temp.add(getCommandInstance(FullCommandType.OutputChar, null));
        temp.add(getCommandInstance(FullCommandType.OutputNumber, null));
        
        REFERENCE_COMMANDS = Collections.unmodifiableList(temp);
    }

}
