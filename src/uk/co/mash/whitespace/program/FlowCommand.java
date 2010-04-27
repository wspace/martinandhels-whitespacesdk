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

import java.util.Stack;


/**
 * JWRE Command implementation that supports Flow operations.
 * 
<pre>
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
</pre>
 * @author mash
 *
 */
public class FlowCommand extends Command {

    public FlowCommand(FullCommandType subCommand, String paramsAsWhitespace) {
        super(subCommand, paramsAsWhitespace);

    }

    @Override
    public int process() {
        boolean terminate = true;
        // we jump around. This is the only command that uses the overridden process(int) method. 
        int currentCommand = getCurrentCommand();
        // default nextCommand
        int nextCommand = currentCommand+1;
        Stack<Long> stack = getFlatPackStack();
        switch (fullCommandType) {
        case Label:
            // null op
            break;
            
        case Gosub:
            getProgramRef().setFromCommand(currentCommand);
            nextCommand = getIndexForLabel(getLabel());
            break;
            
        case Goto:
            nextCommand = getIndexForLabel(getLabel());
            break;
            
        case GotoIfZero:
            if (!stack.isEmpty()) {
                if (stack.pop() == 0) {
                    nextCommand = getIndexForLabel(getLabel());
                }
            }
            break;
            
        case GotoIfNegative:
            if (!stack.isEmpty()) {
                if (stack.pop() < 0) {
                    nextCommand = getIndexForLabel(getLabel());
                }
            }
            break;
            
        case EndSub:
            // Go back to where you came from. Plus one.
            nextCommand = getFromCommand()+1;
            break;
            
        case End:
            System.out.println(System.getProperty("line.separator")+
                    System.getProperty("line.separator")+
                    "***  Program End Cocksucker ***");
//            System.exit(0);
            nextCommand= -1;
        }
        if (terminate) {
            
        }
        return nextCommand;
    }
}
