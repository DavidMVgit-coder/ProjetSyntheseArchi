package application.creatSession.port;

import domain.Session;
import java.util.UUID;
import java.util.Optional;

public interface SessionRepository {
    void save(Session session);
    Optional<Session> findById(UUID sessionId);
    void addPlayer(UUID sessionId, UUID playerId);
    void removePlayer(UUID sessionId, UUID playerId);
}