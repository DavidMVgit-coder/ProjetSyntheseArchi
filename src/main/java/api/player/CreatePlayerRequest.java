package api.player;

public record CreatePlayerRequest(String pseudonym, String email, String password) {

    @Override
    public String toString() {
        return "CreatePlayerRequest[pseudonym=" + pseudonym + ", email=" + email + ", password=***]";
    }
}
