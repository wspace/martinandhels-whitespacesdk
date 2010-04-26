package uk.co.mash.whitespace.program;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;

/**
 * JWRE Command implementation that support IO operations.
 * 
<pre>
    IMP             Meaning
    [Tab][LF]       I/O

    Command         Parameters  Meaning
    [Space][Space]  -           Output the character at the top of the stack
    [Space][Tab]    -           Output the number at the top of the stack
    [Tab][Space]    -           Read a character and place it in the location given by the top of the stack
    [Tab][Tab]      -           Read a number and place it in the location given by the top of the stack
</pre>
 * @author mash
 *
 */
public class IOCommand extends Command implements KeyListener{

    public IOCommand(FullCommandType subCommand, String paramsAsWhitespace) {
        super(subCommand, paramsAsWhitespace);
        
    }

    @Override
    public void process() {
        Stack<Long> stack = getFlatPackStack();
        Map<Long,Long> dump = takeDump();
        BufferedReader inputReader = 
            new BufferedReader(new InputStreamReader(System.in));
        Long input = 0L;
        
        if (!stack.isEmpty()) {
            switch (fullCommandType) {
            case OutputChar:
                System.out.print((char) stack.pop().intValue());
                break;
                
            case OutputNumber:
                System.out.print(stack.pop());
                break;
                
                /* TODO: AAAAARGH!
                 * 
                 * Java console expects keyboard input to be terminated by 
                 * enter key. It's a String, not a char!
                 * I *think* the implementation guide implies that it's char by char. 
                 * Oh bugger! It would appear that the only way is using JNI.
                 * 
                 * Bollocks to that. I'd frig the interpreter instead.
                 * 
                 * Until then you're going to get the first character.
                 *  
                 */
            case InputChar:
                try {
                    //String inputStr = inputReader.readLine();
                    //input = new Long(temp);
                    char c = (char)inputReader.read();
                    dump.put(stack.pop(), new Long(c));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
                /* As per above.
                 * AAAAARGH!
                 * Because there's no direct keyboard access I can't validate numeric 
                 * input as entered... 
                 * So I've frigged it to only accept valid numbers.
                 */
            case InputNumber:
                try {
                    boolean validNumber = false;
                    String in = null;
                    do {
                        in = inputReader.readLine();
                        if (StringUtils.containsOnly(in, "-0123456789")) {
                            validNumber = true;
                            input = Long.parseLong(in);
                        } else {
                            System.out.println("integer input only accepted. ["+in+"] is not valid");
                        }
                    } while (!validNumber);
                    dump.put(stack.pop(), input);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
        }
        super.process();
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }
}
