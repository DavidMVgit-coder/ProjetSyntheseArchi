package infrastructure.repository;

import application.session.port.SessionRepository;
import domain.entities.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemorySessionRepository implements SessionRepository {

    private final Map<UUID, Session> sessions = new HashMap<>();

    @Override
    public void save(Session session) {

        sessions.put(session.getSessionId(), session);
    }

    @Override
    public Optional<Session> findById(UUID sessionId) {

        return Optional.ofNullable(sessions.get(sessionId));
    }
}