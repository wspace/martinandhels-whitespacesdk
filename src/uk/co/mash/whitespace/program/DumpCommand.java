package uk.co.mash.whitespace.program;

import java.util.Map;
import java.util.Stack;

/**
 * JWRE Command implementation that support "Heap" operations.
 * aka "Dump" of whatever.
<pre>
    IMP             Meaning
    [Tab][Tab]      Heap access

    Command Parameters  Meaning
    [Space] -           Store - gets the value from the top of the stack, an address from the next and stores it. 
    [Tab]   -           Retrieve - gets the address from the top of the stack, retrieves from store and puts it on the stack.
</pre>
 * @author mash
 *
 */
public class DumpCommand extends Command {

    public DumpCommand(FullCommandType subCommand, String paramsAsWhitespace) {
        super(subCommand, paramsAsWhitespace);
    }

    @Override
    public int process() {
        Stack<Long> stack = getFlatPackStack();
        Map<Long,Long> dump = takeDump();
        if (!stack.isEmpty()) {
            Long key;
            Long value;
            switch (fullCommandType) {
            case Store:
                if (stack.size()>=2) {
                    key = stack.pop();
                    value = stack.pop();
                    dump.put(key,value);
                }
                break;
                
            case Retrieve:
                key = stack.pop();
                stack.push(dump.get(key));
                break;
            }
        }
        return getCurrentCommand()+1;
    }
}
