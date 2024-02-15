package fr.lovotech.galaxy.qa.fileService.exception;

public interface DomainErrorCodes {

    String FILE_NOT_FOUND = "FILE_NOT_FOUND";
    String CAN_NOT_DUPLICATE_FILE = "CAN_NOT_DUPLICATE_FILE";
    String DELETING_FILE_UNAUTHORIZED = "DELETING_FILE_UNAUTHORIZED";
    String EXISTS_FILE_UNAUTHORIZED_TO_DELETE = "EXISTS_FILE_UNAUTHORIZED_TO_DELETE";
}
