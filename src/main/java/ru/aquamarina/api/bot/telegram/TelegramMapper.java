package ru.aquamarina.api.bot.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.util.Result;
import ru.aquamarina.model.command.Index;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.UnknownCommand;

@Mapper(config = AppMapperConfig.class)
public interface TelegramMapper {

    static TelegramMapper getInstance() {
        return Mappers.getMapper(TelegramMapper.class);
    }

    default Result<Command, Error> mapToCommand(Update update, User user) {
        String command = null;
        if (update.hasMessage()) {
            command = update.getMessage().getText();
        }
        if (update.hasCallbackQuery()) {
            command = update.getCallbackQuery().getData();
        }
        return switch (command) {
            case Start.NAME -> Result.ok(new Start(user));
            case About.NAME -> Result.ok(new About(user));
            case Index.NAME -> Result.ok(new Index(user));
            case Catalog.NAME -> Result.ok(new Catalog(user));
            case String str when str.contains(ProductAbout.NAME) -> Result.ok(new ProductAbout(user, str.split("\\?")[1]));
            case QuantityMinus.NAME -> Result.ok(new QuantityMinus(user));
            case QuantityPlus.NAME -> Result.ok(new QuantityPlus(user));
            case AddToBasket.NAME -> Result.ok(new AddToBasket(user));
            case Basket.NAME -> Result.ok(new Basket(user));
            case null, default -> Result.error(new UnknownCommand());
        };
    }
}
