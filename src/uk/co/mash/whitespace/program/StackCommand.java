package uk.co.mash.whitespace.program;

import java.util.Stack;
/**
 * JWRE Command implementation that supports Stack operations.
 * N.B. 
 * Yes, in the documentation it suggests that the stack consists of binary numbers "of arbitrary width".
 * No. I haven't implemented that. If it's longer than a long then tough.
 * At one point I contemplated adding it as a pure binary or boolean array.
 * No thanks. 
 *  
 *
<pre>
IMP Meaning
[Space] Stack Manipulation

Command         Parameters  Meaning
[Space]         Number      Push the number onto the stack
[LF][Space]     -           Duplicate the top item on the stack
[Tab][Space]    Number      Copy the nth item on the stack (given by the argument) onto the top of the stack
[LF][Tab]       -           Swap the top two items on the stack
[LF][LF]        -           Discard the top item on the stack
[Tab][LF]       Number      Slide n items off the stack, keeping the top item
</pre>
 * @author mash
 *
 */
public class StackCommand extends Command {

    public StackCommand(FullCommandType fullCommandType, String paramsAsWhitespace) {
        super(fullCommandType, paramsAsWhitespace);
    }

    @Override
    public boolean process() {
        Stack<Long> stack = getFlatPackStack();
        Long number = getNumber();
        switch (fullCommandType) {
        case Push:
            stack.push(getNumber());
            break;
            
        case Duplicate:
            if (!stack.isEmpty()) {
                stack.push(stack.peek());
            }
            break;
            
        case Copy:
            if (stack.size()>= number) {
                stack.push(stack.elementAt(number.intValue()));
            }
            break;
            
        case Swap:
            if (stack.size()>= 2) {
                Long item1 = stack.pop();
                Long item2 = stack.pop();
                stack.push(item1);
                stack.push(item2);
            }
            break;
            
        case Discard:
            stack.pop();
            break;
            
        case Slide:
            if (stack.size()>= number) {
                Long topItem = stack.firstElement();
                for (int i=1; i<=number; i++) {
                    stack.pop();
                }
                stack.push(topItem);
            }
            break;
        }
        return super.process();
    } 
    
}
