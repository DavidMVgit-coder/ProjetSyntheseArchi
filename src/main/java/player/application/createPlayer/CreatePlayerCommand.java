package player.application.createPlayer;

public record CreatePlayerCommand(String pseudonym, String email, String password) {

    @Override
    public String toString() {
        return "CreatePlayerCommand[pseudonym=" + pseudonym + ", email=" + email + ", password=***]";
    }
}
