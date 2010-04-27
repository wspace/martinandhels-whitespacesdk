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
package uk.co.mash.whitespace.program;

import org.apache.commons.lang.StringUtils;
/** Enum containing the Sum of All Commands.
 * 
 * For a given Command the enum constructor creating the enumerated type 
 * allocates the following:  
 * <ul>
 * <li>the actual whitespace code fragment that starts the command</li>
 * <li>a human-readable output of the command</li>
 * <li>parameter type (null, number or label)</li>
 * <li>the base command group e.g. Flow, IO</li>
 * <li>an actual description of the function - with palceholder for parameter used by the command describe() method.</li>
 * </ul>
 * Basically all properties it needs with direct accessors.
 *  
 * @author mash
 *
 */
public enum FullCommandType {
    

    // Stack
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
    Push("[Space][Space]","  ",ParameterType.Number, CommandGroupType.Stack,"Push {0} onto stack"),
    Duplicate("[Space][LF][Space]"," \n ",ParameterType.Null,CommandGroupType.Stack,"Duplicate top item on stack"),
    Copy("[Space][Tab][Space]"," \t ",ParameterType.Number,CommandGroupType.Stack,"Copy item {0} from stack to top"),
    Swap("[Space][LF][Tab]"," \n\t",ParameterType.Null,CommandGroupType.Stack,"Swap top two items on stack"),
    Discard("[Space][LF][LF]"," \n\n",ParameterType.Null,CommandGroupType.Stack,"Discard top item on stack"),
    Slide("[Space][Tab][LF]"," \t\n",ParameterType.Number,CommandGroupType.Stack,"Slide {0} items off stack, keeping top item"),
    
    
    //Arithmetic
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
    Add("[Tab][Space][Space][Space]","\t   ",ParameterType.Null,CommandGroupType.Arithmetic,"Add top two items on stack"),
    Subtract("[Tab][Space][Space][Tab]","\t  \t",ParameterType.Null,CommandGroupType.Arithmetic,"Subtract top two items on stack"),
    Multiply("[Tab][Space][Space][LF]","\t  \n",ParameterType.Null,CommandGroupType.Arithmetic,"Multiply top two items on stack"),
    Divide("[Tab][Space][Tab][Space]","\t \t ",ParameterType.Null,CommandGroupType.Arithmetic,"Divide top two items on stack"),
    Mod("[Tab][Space][Tab][Tab]","\t \t\t",ParameterType.Null,CommandGroupType.Arithmetic,"Modulus top two items on stack"),
    
    
    // Dump
    /*
    IMP             Meaning
    [Tab][Tab]      Heap access

    Command Parameters  Meaning
    [Space] -   Store
    [Tab]   -   Retrieve
             */
    Store("[Tab][Tab][Space]","\t\t ",ParameterType.Null,CommandGroupType.Dump,"Store top of stack onto heap at address in second item"),
    Retrieve("[Tab][Tab][Tab]","\t\t\t",ParameterType.Null,CommandGroupType.Dump,"Retrieve data from heap address held in top item in stack and copy to stack"),
    
    
    // Flow
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
    Label("[LF][Space][Space]","\n  ",ParameterType.Label,CommandGroupType.Flow,"Label {0}"),
    Gosub("[LF][Space][Tab]","\n \t",ParameterType.Label,CommandGroupType.Flow,"Subroutine {0}"),
    Goto("[LF][Space][LF]","\n \n",ParameterType.Label,CommandGroupType.Flow,"Goto {0}"),
    GotoIfZero("[LF][Tab][Space]","\n\t ",ParameterType.Label,CommandGroupType.Flow,"Goto {0} if top of stack is zero"),
    GotoIfNegative("[LF][Tab][Tab]","\n\t\t",ParameterType.Label,CommandGroupType.Flow,"Goto {0} if top of stack is negative"),
    EndSub("[LF][Tab][LF] ","\n\t\n",ParameterType.Null,CommandGroupType.Flow,"End subroutine return to caller"),
    End("[LF][LF][LF]","\n\n\n",ParameterType.Null,CommandGroupType.Flow,"End program"),
    NullOp(StringUtils.EMPTY,StringUtils.EMPTY,ParameterType.Null,CommandGroupType.Flow,"NullOp"),
    
    /*
    IMP             Meaning
    [Tab][LF]       I/O

    Command Parameters  Meaning
    [Space][Space]  -   Output the character at the top of the stack
    [Space][Tab]    -   Output the number at the top of the stack
    [Tab][Space]    -   Read a character and place it in the location given by the top of the stack
    [Tab][Tab]  -   Read a number and place it in the location given by the top of the stack
             */
    // IO
    OutputChar("[Tab][LF][Space][Space]","\t\n  ",ParameterType.Null,CommandGroupType.IO,"Output character from top of stack"),
    OutputNumber("[Tab][LF][Space][Tab]","\t\n \t",ParameterType.Null,CommandGroupType.IO,"Output number from top of stack"),
    InputChar("[Tab][LF][Tab][Space]","\t\n\t ",ParameterType.Null,CommandGroupType.IO,"Read a character to heap at address held at top of the stack"),
    InputNumber("[Tab][LF][Tab][Tab]","\t\n\t\t",ParameterType.Null,CommandGroupType.IO,"Read a character to heap at address held at top of the stack");
    
    private FullCommandType(String humanReadableCommand, String whitespaceCommand, ParameterType parameterType, CommandGroupType commandType, String description) {
        this.humanReadableCommand = humanReadableCommand;
        this.whitespaceCommand = whitespaceCommand;
        this.parameterType = parameterType;
        this.commandType = commandType;
        this.description = description;
    }
    
    public static FullCommandType getFullCommandTypeForWhitespace(String whitespace) {
        FullCommandType fullCommandType = NullOp;
        for (FullCommandType thisCommand: values()) {
            if (StringUtils.equals(thisCommand.getWhitespaceCommand(), whitespace)) {
                fullCommandType = thisCommand;
                break;
            }
        }
        return fullCommandType;
    }
    
    String whitespaceCommand;
    
    String humanReadableCommand;
    
    ParameterType parameterType;
    
    CommandGroupType commandType;
    
    String description;

    public String getWhitespaceCommand() {
        return whitespaceCommand;
    }

    public void setWhitespaceCommand(String whitespaceCommand) {
        this.whitespaceCommand = whitespaceCommand;
    }

    public String getHumanReadableCommand() {
        return humanReadableCommand;
    }

    public void setHumanReadableCommand(String humanReadableCommand) {
        this.humanReadableCommand = humanReadableCommand;
    }

    public ParameterType getParameterType() {
        return parameterType;
    }

    public void setParameterType(ParameterType parameterType) {
        this.parameterType = parameterType;
    }

    public CommandGroupType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandGroupType commandType) {
        this.commandType = commandType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
