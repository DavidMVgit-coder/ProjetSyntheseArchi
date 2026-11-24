package api.session;

import player.application.login.LoginOutput;

import java.util.UUID;

public record SessionResponse(UUID playerId, String token) {

    public static SessionResponse from(LoginOutput output) {
        return new SessionResponse(output.playerId(), output.token());
    }
}
