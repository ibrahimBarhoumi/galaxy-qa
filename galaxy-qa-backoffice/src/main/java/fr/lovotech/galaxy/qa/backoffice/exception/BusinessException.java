package fr.lovotech.galaxy.qa.backoffice.exception;


/**
 * Exception thrown for a non respected business rule
 */
public class BusinessException extends RuntimeException {

    /**
     * The message arguments for message using arguments
     */
    private Object[] messageArgs;

    /**
     * Instantiates a new Business exception.
     *
     * @param code the code
     */
    public BusinessException(String code){
        super(code);
    }

    /**
     * Instantiates a new Business exception.
     *
     * @param code the code
     * @param message the message
     */
    public BusinessException(String code, String message){
        super(message);
    }

    /**
     * Instantiates a new Business exception.
     *
     * @param code the code
     * @param messageArgs the args
     */
    public BusinessException(String code, Object[] messageArgs){
        this(code);
        this.messageArgs = messageArgs;
    }


    public Object[] getMessageArgs() {
        return messageArgs;
    }

    public void setMessageArgs(Object[] messageArgs) {
        this.messageArgs = messageArgs;
    }
}
