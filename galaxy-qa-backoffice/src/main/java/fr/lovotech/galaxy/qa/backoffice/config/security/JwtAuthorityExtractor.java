package fr.lovotech.galaxy.qa.backoffice.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class JwtAuthorityExtractor extends JwtAuthenticationConverter {

	public JwtAuthorityExtractor() {
	}

	@Override
	protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
		Map<String, Object> claims = jwt.getClaimAsMap("realm_access");
		return SecurityUtils.extractAuthorityFromClaims(claims);
	}
}
