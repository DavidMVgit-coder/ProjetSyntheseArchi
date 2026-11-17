package api.lobby;

public class AuthenticationRequiredException extends RuntimeException {

    public AuthenticationRequiredException() {
        super("Un joueur authentifié est requis pour créer un lobby.");
    }
}
