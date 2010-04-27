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
    public int process() {
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
        return getCurrentCommand()+1;
    }

}
