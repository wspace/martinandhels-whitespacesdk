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
