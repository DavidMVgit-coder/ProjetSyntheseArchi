package player.api;

import player.application.createPlayer.CreatePlayerOutput;
import player.application.getPlayerProfile.PlayerProfileOutput;

import java.util.UUID;

public record PlayerResponse(UUID id, String pseudonym, String email) {

    public static PlayerResponse from(CreatePlayerOutput output) {
        return new PlayerResponse(output.id(), output.pseudonym(), output.email());
    }

    public static PlayerResponse from(PlayerProfileOutput output) {
        return new PlayerResponse(output.id(), output.pseudonym(), output.email());
    }
}
