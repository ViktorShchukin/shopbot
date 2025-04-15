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
        return switch (command) {
            case Start.NAME  -> Result.ok(new Start(userId));
            case About.NAME -> Result.ok(new About(userId));
            case Index.NAME -> Result.ok(new Index(userId));
            case Catalog.NAME -> Result.ok(new Catalog(userId));
            // todo think how to extract product name
            case ProductAbout.NAME -> Result.ok(new ProductAbout(userId));
            case null, default -> Result.error(new UnknowCommand());
        };
    }
}
