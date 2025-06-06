package ru.aquamarina.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;

import java.util.UUID;

public class CommandUtil {

    private static final Logger log = LoggerFactory.getLogger(CommandUtil.class);

    public static Result<UUID, Error> parseCmdWithUuidArg(String cmd) {
        return stringToUuid(cmd.split("\\?")[1]);
    }

    public static Result<UUID, Error> stringToUuid(String strId) {
        try {
            return Result.ok(UUID.fromString(strId));
        } catch (IllegalArgumentException e) {
            log.error("error during parse of the uuid string: ", e);
            // todo think about better error.
            return Result.error(new NotSupportedCommand());
        }
    }
}
