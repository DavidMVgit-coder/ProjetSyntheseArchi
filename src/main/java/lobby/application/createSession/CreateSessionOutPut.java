package application.creatSession;

import java.util.UUID;

public record CreateSessionOutput(
        UUID sessionId,
        UUID creatorId,
        int playerCount
) {}