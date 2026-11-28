package api.security;

import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;
import java.util.UUID;

public final class AuthenticatedPlayer {

    private AuthenticatedPlayer() {
    }

    public static UUID idFrom(SecurityContext securityContext) {
        Principal principal = securityContext == null ? null : securityContext.getUserPrincipal();
        if (principal == null || principal.getName() == null) {
            throw new AuthenticationRequiredException();
        }

        String name = principal.getName();
        try {
            UUID playerId = UUID.fromString(name);
            if (!playerId.toString().equalsIgnoreCase(name)) {
                throw new AuthenticationRequiredException();
            }
            return playerId;
        } catch (IllegalArgumentException notAnId) {
            throw new AuthenticationRequiredException();
        }
    }
}
