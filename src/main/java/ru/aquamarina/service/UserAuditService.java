package ru.aquamarina.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.state.FsmState;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.entity.UserAuditEntry;
import ru.aquamarina.repository.AuditRepository;

import java.time.Instant;
import java.util.UUID;

@Singleton
public class UserAuditService {

    private static final Logger logger = LoggerFactory.getLogger(UserAuditService.class);

    private final AuditRepository auditRepository;

    public UserAuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public Command receiveCommand(User user, Command command) {
        log(user, Event.RECEIVE_USER_COMMAND, command.toString());
        return command;
    }

    public FsmState updateState(User user, FsmState state) {
        log(user, Event.UPDATE_USER_STATE, state.toString());
        return state;
    }

    public User createUser(User user, String details) {
        log(user, Event.CREATE_USER, details);
        return user;
    }

    private void log(User user, Event action, String comment) {
        try {
            var entry = new UserAuditEntry(
                    UUID.randomUUID(),
                    Instant.now(),
                    user.getId(),
                    action.toString(),
                    comment
            );

            auditRepository.save(entry);
        } catch (Exception e) {
            logger.error("Can't put audit entry in database", e);
        }
    }

    public enum Event {
        CREATE_USER,
        UPDATE_USER_INFO,
        RECEIVE_USER_COMMAND,
        UPDATE_USER_STATE
    }
}
