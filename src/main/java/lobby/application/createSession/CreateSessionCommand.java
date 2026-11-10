package application.session.CreateSession;

import java.util.UUID;

public record CreateSessionCommand(
        UUID creatorId
) {}