package player.application.getPlayerProfile;

public class ProfileAccessForbiddenException extends RuntimeException {

    public ProfileAccessForbiddenException() {
        super("Un joueur ne peut consulter que son propre profil.");
    }
}
