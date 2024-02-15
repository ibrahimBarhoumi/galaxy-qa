package fr.lovotech.galaxy.qa.backoffice.exception;

/**
 * The Class RecoverableException.
 */
public class RecoverableException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -725554324501425030L;

    /**
     * Instantiates a new recoverable exception.
     *
     * @param exceptionMessage the exception message
     */
    public RecoverableException(String exceptionMessage) {
        super(exceptionMessage);
    }

    /**
     * Instantiates a new recoverable exception.
     *
     * @param exceptionMessage the exception message
     * @param e                the e
     */
    public RecoverableException(String exceptionMessage, Throwable e) {
        super(exceptionMessage, e);
    }
}
