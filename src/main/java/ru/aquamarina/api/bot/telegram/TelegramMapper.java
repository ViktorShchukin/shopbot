package ru.aquamarina.api.bot.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.fsm.state.OrderState;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.util.Result;
import ru.aquamarina.model.command.IndexCmd;
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
            case StartCmd.NAME -> Result.ok(new StartCmd(user));
            case AboutCmd.NAME -> Result.ok(new AboutCmd(user));
            case IndexCmd.NAME -> Result.ok(new IndexCmd(user));
            case CatalogCmd.NAME -> Result.ok(new CatalogCmd(user));
            case String str when str.contains(ProductAboutCmd.NAME) -> Result.ok(new ProductAboutCmd(user, str.split("\\?")[1]));
            case QuantityMinusCmd.NAME -> Result.ok(new QuantityMinusCmd(user));
            case QuantityPlusCmd.NAME -> Result.ok(new QuantityPlusCmd(user));
            case AddToBasketCmd.NAME -> Result.ok(new AddToBasketCmd(user));
            case BasketCmd.NAME -> Result.ok(new BasketCmd(user));
            case DoOrderCmd.NAME -> Result.ok(new DoOrderCmd(user));
            case null, default -> Result.error(new UnknownCommand());
        };
    }
}
