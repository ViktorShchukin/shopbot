package ru.aquamarina.api.bot.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.model.command.*;
import ru.aquamarina.util.Result;
import ru.aquamarina.model.command.Index;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.UnknowCommand;

import java.util.UUID;

@Mapper
public interface TelegramMapper {

    static TelegramMapper getInstance() {
        return Mappers.getMapper(TelegramMapper.class);
    }

    default Result<Command, Error> map(Update update, UUID userId) {
        String command = null;
        if (update.hasMessage()) {
            command = update.getMessage().getText();
        }
        if (update.hasCallbackQuery()) {
            command = update.getCallbackQuery().getData();
        }
        switch (command) {
            case String s when s.equals(Start.getCommandName())  -> {
                return Result.ok(new Start(userId));
            }
            case String s when s.equals(About.getCommandName()) -> {
                return Result.ok(new About(userId));
            }
            case String s when s.equals(Index.getCommandName()) -> {
                return Result.ok(new Index(userId));
            }
            case String s when s.equals(Catalog.getCommandName()) -> {
                return Result.ok(new Catalog(userId));
            }
            case String s when s.equals(About.getCommandName()) -> {
                // todo think how to extract product name
                return Result.ok(new About(userId));
            }
            default -> {
                return Result.error(new UnknowCommand());
            }
        }
    }
}
