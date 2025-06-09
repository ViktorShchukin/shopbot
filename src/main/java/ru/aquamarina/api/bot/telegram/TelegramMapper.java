package ru.aquamarina.api.bot.telegram;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.config.AppMapperConfig;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.util.CommandUtil;
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
            case ForWholesalerCmd.NAME -> Result.ok(new ForWholesalerCmd(user));
            case PayAndDeliveryCmd.NAME -> Result.ok(new PayAndDeliveryCmd(user));
            case IndexCmd.NAME -> Result.ok(new IndexCmd(user));
            case CatalogCmd.NAME -> Result.ok(new CatalogCmd(user));
            case String str when str.contains(ProductAboutCmd.NAME) ->
                    CommandUtil.parseCmdWithUuidArg(str).mapValue(id -> new ProductAboutCmd(user, id));
            case String str when str.contains(QuantityMinusCmd.NAME) ->
                    CommandUtil.parseCmdWithUuidArg(str).mapValue(id -> new QuantityMinusCmd(user, id));
            case String str when str.contains(QuantityPlusCmd.NAME) ->
                    CommandUtil.parseCmdWithUuidArg(str).mapValue(id -> new QuantityPlusCmd(user, id));
            case AddToBasketCmd.NAME -> Result.ok(new AddToBasketCmd(user));
            case BasketCmd.NAME -> Result.ok(new BasketCmd(user));
            case DoOrderCmd.NAME -> Result.ok(new DoOrderCmd(user));
            case String str when str.contains(FolderCmd.NAME) -> Result.ok(new FolderCmd(user, str.split("\\?")[1]));
            case ClearBasketCmd.NAME -> Result.ok(new ClearBasketCmd(user));
            case InstructionCmd.NAME -> Result.ok(new InstructionCmd(user));
            case DoNothing.NAME -> Result.ok(new DoNothing(user));
            case DeliveryCmd.NAME -> Result.ok(new DeliveryCmd(user));
            case SelfPickupCmd.NAME -> Result.ok(new SelfPickupCmd(user));
            case String str when str.contains(OrderAdditionalInfoPhoneCmd.NAME) ->
                    Result.ok(new OrderAdditionalInfoPhoneCmd(user, str.trim()));
            case String str when str.toLowerCase().contains(OrderAdditionalInfoAddressCmd.NAME) ->
                    Result.ok(new OrderAdditionalInfoAddressCmd(user, str.trim()));
            case null, default -> Result.error(new UnknownCommand(user));
        };
    }
}
