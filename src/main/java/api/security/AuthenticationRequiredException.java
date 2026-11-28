package api.security;

public class AuthenticationRequiredException extends RuntimeException {

    public AuthenticationRequiredException() {
        super("Un joueur authentifié est requis.");
    }
}
