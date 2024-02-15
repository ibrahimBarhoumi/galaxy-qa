package fr.lovotech.galaxy.qa.backoffice.config.security;

import com.google.common.collect.Lists;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

public class UserUtils {

    private static final String ORGANIZATIONS_CLAIM = "organizations";
    private static final String LOGIN_CLAIM = "preferred_username";
    private static final String FULL_NAME_CLAIM = "name";
    private static final String EMAIL = "email";

    private static final String ALL = "ALL";

    private UserUtils() {

    }


    public static List<String> getOrganizations() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
            if (authentication != null) {
                return authentication.getToken().getClaimAsStringList(ORGANIZATIONS_CLAIM);
            }
        }
        return Lists.newArrayList();
    }

    public static String getLogin() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
            if (authentication != null) {
                return authentication.getToken().getClaimAsString(LOGIN_CLAIM);
            }
        }
        return "robot@lovotech.fr";
    }

    public static String getEmail() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
            if (authentication != null) {
                return authentication.getToken().getClaimAsString(EMAIL);
            }
        }
        return null;
    }

    public static String getFullName() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
            if (authentication != null) {
                return authentication.getToken().getClaimAsString(FULL_NAME_CLAIM);
            }
        }
        return "Robot";
    }

    public static boolean isUserInOrganization(String organization) {
        return getOrganizations().stream().anyMatch(organization::equals);
    }

}
