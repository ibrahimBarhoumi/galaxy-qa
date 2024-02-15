package fr.lovotech.galaxy.qa.fileService.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * An code node as returned by the service.
 */
public class ErrorNode {
    public static final String SURVEY_SERVICE = "survey-service";
    /**
     * The Error.
     */
    private final String code;
    /**
     * The Error description.
     */
    private final String message;

    /**
     * the external source name
     */
    private final String source;

    public ErrorNode(String source, String code, String alreadyTranslatedMessage) {
        this.source = source == null ? SURVEY_SERVICE : source;
        this.code = code;
        this.message = alreadyTranslatedMessage;
    }

    public ErrorNode(String source, String code, MessageSource messageSource, String messageKey, Object... messageArgs) {
        this(source, code, ErrorNode.getErrorNodeMessage(messageSource, code, messageKey, messageArgs));
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets code description.
     *
     * @return the description
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the source name
     */
    public String getSource() {
        return source;
    }


    private static String getErrorNodeMessage(MessageSource messageSource, String errorCode, String messageKey, Object[] messageArgs){
        // If it's missing, enrich error message using error code
        String errorMessage;
        //the details of a technical error are confidential

        try {
            if (ErrorCode.TECHNICAL_ERROR.equals(errorCode)) {
                return messageSource.getMessage(ErrorCode.TECHNICAL_ERROR, messageArgs, LocaleContextHolder.getLocale());
            }
            errorMessage = messageSource.getMessage(messageKey, messageArgs , LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e2){
            errorMessage = errorCode;
        }
        return errorMessage;
    }
}
