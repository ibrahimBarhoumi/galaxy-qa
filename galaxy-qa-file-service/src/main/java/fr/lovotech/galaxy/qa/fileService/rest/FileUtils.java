package fr.lovotech.galaxy.qa.fileService.rest;

public final class FileUtils {


    public static boolean isContentTypeAllowed(String fileContentType) {
        return fileContentType != null &&
                ((fileContentType.startsWith("image/") && !"image/sgv+xml".equals(fileContentType))
                        || fileContentType.startsWith("video/")
                        || "application/pdf".equals(fileContentType)
                        || "application/octet-stream".equals(fileContentType));
    }
}
