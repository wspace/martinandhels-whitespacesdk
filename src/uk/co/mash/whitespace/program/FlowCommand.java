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
    public void process() {
        boolean keepGoing = true;
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
                    "***  Program End  ***");
            keepGoing = false;
        }
        getNextCommand(nextCommand).process();
    }
}
