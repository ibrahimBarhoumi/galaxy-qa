package fr.lovotech.galaxy.qa.fileService.exception;

/**
 * ErrorCodes thrown by the service
 */
public interface ErrorCode {

    String TECHNICAL_ERROR = "GLOBAL.ERROR.TECHNICAL_ERROR";

    String FORBIDDEN_ACCESS = "global.error.forbiddenAccess";

    String NOT_FOUND = "global.error.notFound";

    String BAD_REQUEST = "global.error.badRequest";

    String USER_NOT_FOUND = "global.error.user_not_found";

}
