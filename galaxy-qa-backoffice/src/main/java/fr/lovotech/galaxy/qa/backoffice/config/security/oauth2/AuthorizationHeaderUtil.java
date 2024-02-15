package fr.lovotech.galaxy.qa.backoffice.config.security.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorizationHeaderUtil {


	public AuthorizationHeaderUtil() {
	}

	public Optional<String> getAuthorizationHeader() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		JwtAuthenticationToken oauthToken = (JwtAuthenticationToken) authentication;

		if (oauthToken == null) {
			return Optional.empty();
		} else {
			String tokenType = "Bearer";
			String authorizationHeaderValue = String.format("%s %s", tokenType, oauthToken.getToken().getTokenValue());
			return Optional.of(authorizationHeaderValue);
		}
	}
}
