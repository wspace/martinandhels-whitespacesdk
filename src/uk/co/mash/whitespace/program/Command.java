package uk.co.mash.whitespace.program;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
/**
 * Abstract superclass encapsulating a Whitespace Command Object.
 * This is at the core of the Whitespace Runtime Environment.
 * 
 * The JWRE basically exists as an ordered List of these that recursively call the 
 * process() method.
 * 
 * @author mash
 *
 */
public abstract class Command {

    // everything it needs.
    /** Reference to the controlling Program object.*/
    Program programRef;
    /** FullCommand is an enumerated state object containing all data relevant to this Command object.*/
    FullCommandType fullCommandType;
    /** Raw parameter as a whitespace string.*/
    String rawParam = null;
    
    /**
     * Main constructor. Create a new one and initialize:
     * Set the FullCommandType and Params (everything this impl needs)
     * 
     * @param fullCommandType Enumerated Object containing all properties this Command Object needs.
     * @param paramsAsWhitespace Implementation specific parameters for this Command.
     */
    public Command(FullCommandType fullCommandType, String paramsAsWhitespace) {
        
        super();
        this.fullCommandType=fullCommandType;
        this.rawParam=paramsAsWhitespace;
    }

    /**
     * The point of a command object is to run! The process method is how it works. 
     * N.B. all calls to process() are recursive.
     * @return TODO
     */
    public boolean process() {
        return getNextCommand().process();
    }
    /**
     * Getter for label. 
     * @return parameter for this command interpreted as a binary String.
     */
    public String getLabel() {
        return parseLabelParam(rawParam);
    }
    /**
     * Getter for FullCommandType - an enumerated object that contains all the properties for this Command Object.
     * @return the FullCOmmandType Object associated with this Command.
     */
    public FullCommandType getFullCommandType() {
        return fullCommandType;
    }

    public long getNumber() {
        long number = 0L;
        if (StringUtils.isNotEmpty(rawParam)) {
            number = parseNumberParam(rawParam);
        }
        return number;
    }

    public String describe() {
        String description = this.fullCommandType.getDescription();
        switch (getParameterType()) {
        case Label:
            description = MessageFormat.format(description, new Object[] {getLabel()});
            break;
            
        case Number:
            description = MessageFormat.format(description, new Object[] {getNumber()});
            break;
            
        }
        return description;
    }
    
    
    public void setProgramRef(Program program) {
        this.programRef = program;
    }
    
    public Program getProgramRef() {
        return this.programRef;
    }
    
    protected Command getNextCommand() {
        // apart from Flow always returns next in list.
        return programRef.getNextCommand();
    }
    
    protected Command getNextCommand(int next) {
        // apart from Flow always returns next in list.
        return programRef.getNextCommand(next);
    }
    
    public Map<Long, Long> takeDump() {
        return programRef.takeDump();
    }
    
    public String getWhitespaceCommand() {
        return this.fullCommandType.getWhitespaceCommand();
    }
    
    public String getHumanReadableCommand() {
        return this.fullCommandType.getHumanReadableCommand();
    }
    
    public ParameterType getParameterType() {
        return this.fullCommandType.getParameterType();
    }
    
    public String getRawParam() {
        return rawParam;
    }

    public void setRawParam(String rawParam) {
        if (StringUtils.isNotEmpty(rawParam)) {
            this.rawParam = rawParam;
        }
    }
    
    public int getCurrentCommand() {
        return getProgramRef().getCurrentCommand();
    }
    
    public Stack<Long> getFlatPackStack() {
        return getProgramRef().getFlatPackStack();
    }
    
    public int getIndexForLabel(String label) {
        return getProgramRef().getIndexForLabel(label);
    }
    
    public int getFromCommand() {
        return getProgramRef().getFromCommand();
    }

    public String humanReadableParams() {
        String humanBeatbox = StringUtils.EMPTY;
        if (StringUtils.isNotEmpty(rawParam)) {
            humanBeatbox = StringUtils.replace(rawParam, " ", "[Space]");
            humanBeatbox = StringUtils.replace(humanBeatbox, "\t", "[Tab]");
            humanBeatbox = StringUtils.replace(humanBeatbox, "\n", "[LF]");
            humanBeatbox += "[LF]";
        }
        return humanBeatbox;
    }
    
    private long parseNumberParam(String rawParam) {
        // First char is "sign". tab indicates negative.
        int sign = (rawParam.charAt(0)=='\t') ? -1 : 1;
        long number = Long.parseLong(parseLabelParam(rawParam),2);
        return  number * sign;
    }
    
    private String parseLabelParam(String rawParam) {
        String between = StringUtils.substring(rawParam,1,rawParam.length());
        between = StringUtils.replace(between, " ", "0");
        return StringUtils.replace(between, "\t", "1");
    }
}
