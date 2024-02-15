package fr.lovotech.galaxy.qa.fileService.exception;


/**
 * Exception thrown for a non respected business rule
 */
public class BusinessException extends RuntimeException {

    /**
     * The Error node.
     */
    private ErrorNode errorNode;

    /**
     * The message arguments for message using arguments
     */
    private Object[] messageArgs;

    /**
     * Instantiates a new Business exception.
     *
     * @param errorNode the error node
     */
    public BusinessException(ErrorNode errorNode){
        super(errorNode.getMessage());
        this.errorNode = errorNode;
    }

    /**
     * Instantiates a new Business exception.
     *
     * @param code the code
     */
    public BusinessException(String code){
        super(code);
        ErrorNode errorNode = new ErrorNode(ErrorNode.SURVEY_SERVICE, code, null);
        this.errorNode = errorNode;
    }

    /**
     * Instantiates a new Business exception.
     *
     * @param code the code
     * @param message the message
     */
    public BusinessException(String code, String message){
        super(message);
        ErrorNode errorNode = new ErrorNode(ErrorNode.SURVEY_SERVICE, code, message);
        this.errorNode = errorNode;
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

    /**
     * Gets error node.
     *
     * @return the error node
     */
    public ErrorNode getErrorNode() {
        return errorNode;
    }

    /**
     * Sets error node.
     *
     * @param errorNode the error node
     */
    public void setErrorNode(ErrorNode errorNode) {
        this.errorNode = errorNode;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }

    public void setMessageArgs(Object[] messageArgs) {
        this.messageArgs = messageArgs;
    }
}
