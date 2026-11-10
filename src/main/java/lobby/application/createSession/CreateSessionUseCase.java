package application.CreateSession;

import application.port.SessionRepository;
import application.createSession.CreateSessionCommand;
import application.createSession.CreateSessionOutput;
import domain.Session;


import java.util.UUID;

public class CreateSessionUseCase {

    private final SessionRepository sessionRepository;

    public CreateSessionUseCase(
            SessionRepository sessionRepository
    ) {
        this.sessionRepository = sessionRepository;
    }

    public CreateSessionOutput execute(
            CreateSessionCommand command
    ) {

        UUID sessionId = UUID.randomUUID();

        Session session = new Session(
                sessionId,
                command.creatorId()
        );

        sessionRepository.save(session);

        return new CreateSessionOutput(
                session.getSessionId(),
                session.getCreatorId(),
                session.getPlayerCount()
        );
    }
}