package uk.co.mash.whitespace.program;

import java.util.Stack;

/**
 * JWRE Command implementation that supports Arithmetic operations.
 * Generally it takes the first 2 items off the stack, processes the arithmetic operation,
 * and puts the result back on the stack.
 * 
<pre>
    IMP             Meaning
    [Tab][Space]    Arithmetic

    Command         Parameters  Meaning
    [Space][Space]  -           Add
    [Space][Tab]    -           Subtract
    [Space][LF]     -           Multiply
    [Tab][Space]    -           Integer Divide
    [Tab][Tab]      -           Mod
</pre>
 * @author mash
 *
 */
public class ArithmeticCommand extends Command {

    public ArithmeticCommand(FullCommandType subCommand, String paramsAsWhitespace) {
        // create a bollocks
        super(subCommand, paramsAsWhitespace);
    }
    
    @Override
    public boolean process() {
        Stack<Long> stack = getFlatPackStack();
        if (!stack.isEmpty()) {
            Long item1 = stack.pop();
            Long item2 = stack.pop();
            switch (fullCommandType) {
            
            case Add:
                stack.push(item1+item2);
                break;
                
            case Subtract:
                stack.push(item1-item2);
                break;
                
            case Multiply:
                stack.push(item1*item2);
                break;
                
            case Divide:
                stack.push(item1/item2);
                break;
                
            case Mod:
                stack.push(item1%item2);
                break;
            }
        }
        return super.process();
    }

}
