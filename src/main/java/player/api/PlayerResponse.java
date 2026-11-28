package player.api;

import player.application.createPlayer.CreatePlayerOutput;

import java.util.UUID;

public record PlayerResponse(UUID id, String pseudonym, String email) {

    public static PlayerResponse from(CreatePlayerOutput output) {
        return new PlayerResponse(output.id(), output.pseudonym(), output.email());
    }
}
