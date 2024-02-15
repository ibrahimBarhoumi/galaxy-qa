package fr.lovotech.galaxy.qa.backoffice.exception;

/**
 * The Class NonRecoverableException.
 */
public class NonRecoverableException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 7906387099644503700L;

    /**
     * Instantiates a new non recoverable exception.
     *
     * @param exceptionMessage the exception message
     */
    public NonRecoverableException(String exceptionMessage) {
        super(exceptionMessage);
    }

    /**
     * Instantiates a new non recoverable exception.
     *
     * @param exceptionMessage the exception message
     * @param cause            the cause
     */
    public NonRecoverableException(String exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }
}
