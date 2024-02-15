package fr.lovotech.galaxy.qa.fileService.exception;

/**
 * Technical Exception
 */
public class TechnicalException extends RuntimeException {

    /**
     * Instantiates a new Technical exception.
     *
     * @param message the message
     * @param e the e
     */
    public TechnicalException(String message, Throwable e){
        super(message, e);
    }

    /**
     * Instantiates a new Technical exception.
     *
     * @param message the message
     */
    public TechnicalException(String message){
        this(message, null);
    }

    /**
     * Instantiates a new Technical exception.
     *
     * @param e the e
     */
    public TechnicalException(Throwable e){
        super(e);
    }

}
