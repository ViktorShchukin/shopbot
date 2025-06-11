package ru.aquamarina.api.bot.telegram;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.aquamarina.api.bot.View;
import ru.aquamarina.api.dto.ProductRowDto;
import ru.aquamarina.api.mapper.ProductMapper;
import ru.aquamarina.fsm.form.*;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.Folder;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.TelegramInfo;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.service.ProductService;
import ru.aquamarina.service.TelegramInfoService;
import ru.aquamarina.util.PathUtil;
import ru.aquamarina.util.ResultError;
import ru.aquamarina.util.ResultOk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class TelegramView implements View {

    private static final Logger log = LoggerFactory.getLogger(TelegramView.class);

    private final OkHttpTelegramClient client;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final TelegramInfoService telegramInfoService;

    public TelegramView(OkHttpTelegramClient client, ProductService productService, ProductMapper productMapper, TelegramInfoService telegramInfoService) {
        this.client = client;
        this.productService = productService;
        this.productMapper = productMapper;
        this.telegramInfoService = telegramInfoService;
    }

    @Override
    public void drawAboutForm(AboutForm form) {

        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        );

        String messageText = """
                Это чат-бот магазина химии для бассейнов “Аквамарина» (ИП Щукина М.А.) Мы предлагаем химию для бассейна российского производителя ТМ Aqualeon. Здесь вы можете заказать средства для ухода за бассейном.
                У нас есть пункт самовывоза в г. Ростов-на-Дону. Подробнее смотрите в разделе «Доставка и оплата».
                Если вы находитесь в другом населенном пункте, то мы отправим вам заказ сервисом «Яндекс-доставка». При заказе более 2000р доставка бесплатная.
                """;

        rewriteMessage(form.user(), messageText, keyboardRow);
    }

    @Override
    public void drawIndexForm(IndexForm form) {
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new AboutCmd(null)),
                getButton(new CatalogCmd(null))
        );
        InlineKeyboardRow keyboardRow1 = new InlineKeyboardRow(
                getButton(new ForWholesalerCmd(null)),
                getButton(new PayAndDeliveryCmd(null))
        );
        InlineKeyboardRow keyboardRow2 = new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                        .text("Связаться с мэнеджером")
                        .url("tg://user?id=876199982")
                        .build()
        );

        List<InlineKeyboardRow> keyboardRowList = List.of(keyboardRow, keyboardRow1, keyboardRow2);

        String messageText = "Привет. Чего желаете";

        if (form.isRestartRequired()) {
            sendMessage(form.user(), messageText, keyboardRowList);
        } else {
            rewriteMessage(form.user(), messageText, keyboardRowList);
        }
    }

    @Override
    public void drawCatalogForm(CatalogForm form) {
        List<Command> productInFolder = form.products().stream()
                .map(product -> new ProductAboutCmd(null, product.getId()))
                .collect(Collectors.toList());
        List<Command> folderInFolder = form.folders().stream()
                .map(Folder::path)
                .map(pth -> new FolderCmd(null, pth))
                .collect(Collectors.toList());
        List<Command> commands;
        if (form.path().equals("/")) {
            commands = List.of(
                    new BasketCmd(null),
                    new IndexCmd(null)
            );
        } else {
            commands = List.of(
                    new BasketCmd(null),
                    new CatalogCmd(null),
                    new IndexCmd(null)
            );
        }

        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        Stream.of(productInFolder, folderInFolder, commands)
                .flatMap(Collection::stream)
                .forEach(command -> {
                    InlineKeyboardRow keyboardRow = new InlineKeyboardRow();
                    keyboardRow.add(getButton(command));
                    keyboardRowList.add(keyboardRow);
                });

        String messageText = "Выберете нужный товар";

        rewriteMessage(form.user(), messageText, keyboardRowList);
    }

    @Override
    public void drawProductAboutForm(ProductAboutForm form) {
        Product product = form.product();
        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(String.valueOf(form.quantity()), new DoNothing(null))
        ));
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(new QuantityMinusCmd(null, product.getId())),
                getButton(new QuantityPlusCmd(null, product.getId()))
        ));
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(new BasketCmd(null)),
                getButton(new InstructionCmd(null))
        ));
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(new CatalogCmd(null))
        ));
        keyboardRowList.add(new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        ));

        String messageText = product.getName() + "\n" + "Цена: " + (double) product.getCost() / 100 + " руб" + "\n" + "В корзине: " + form.quantity();

        rewriteMessage(form.user(), messageText, keyboardRowList);
    }

    @Override
    public void drawOrderForm(OrderForm form) {
        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        );
        InlineKeyboardRow keyboardRow1 = new InlineKeyboardRow(
                getButton("Сделать еще один заказ", new CatalogCmd(null))
        );
        keyboardRowList.add(keyboardRow);
        keyboardRowList.add(keyboardRow1);


        List<ProductRowDto> products = form.rows().stream()
                .map(basketRow -> productMapper.mapTo(basketRow, productService::getById))
                // todo how to handle that product exist in basket and doesn't exist in product table???
                .map(res -> res.ok())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        String productTable = TelegramUtils.getProductTable(products);
        String messageText = productTable + "\n" + "Сумма заказа: " + (double) form.totalCost() / 100 + " руб" + "\n\nСпасибо за заказ.\nМы свяжемся с вами позже.";

        sendMessage(form.user(), messageText, keyboardRowList);
    }

    @Override
    public void drawBasketForm(BasketForm form) {

        List<ProductRowDto> products = form.rows().stream()
                .map(basketRow -> productMapper.mapTo(basketRow, productService::getById))
                // todo how to handle that product exist in basket and doesn't exist in product table???
                .map(res -> res.ok())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        List<InlineKeyboardRow> keyboardRowList = new ArrayList<>();
        products.forEach(productRowDto -> {
            var keyBoardRow = new InlineKeyboardRow(
                    getButton(productRowDto.product().getName(), new ProductAboutCmd(null, productRowDto.product().getId())),
                    getButton("-", new QuantityMinusCmd(null, productRowDto.product().getId())),
                    getButton("+", new QuantityPlusCmd(null, productRowDto.product().getId()))
            );
            keyboardRowList.add(keyBoardRow);
        });
        InlineKeyboardRow clearBasketRow = new InlineKeyboardRow(
                getButton(new ClearBasketCmd(null))
        );
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new DoOrderCmd(null)),
                getButton(new CatalogCmd(null))
        );
        keyboardRowList.add(clearBasketRow);
        keyboardRowList.add(keyboardRow);


        String productTable = TelegramUtils.getProductTable(products);
        String messageText = "Корзина" + "\n\n" + productTable + "\n" + "Сумма: " + (double) form.totalCost() / 100 + " руб";


        rewriteMessage(form.user(), messageText, keyboardRowList);
    }

    @Override
    public void drawForWholesalerForm(ForWholesalerForm form) {
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        );

        String messageText = """
                Наша компания является региональным представителем ТМ AQUALEON на территории Ростовской области. Поэтому мы можем предоставить выгодные цены для оптовых покупателей.

                Мы открыты для партнерства с магазинами и профессионалами в сфере обслуживания бассейнов.

                Проконсультируем Вас по вопросам продажи и использования химии, поможем разобраться в ассортименте.
                 
                Напишите нашему <a href="tg://user?id=876199982">менеджеру</a> и мы предоставим вам оптовый прайс.
                """;

        rewriteMessage(form.user(), messageText, keyboardRow);
    }

    @Override
    public void drawPayAndDeliveryFormForm(PayAndDeliveryForm form) {
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new IndexCmd(null))
        );

        String messageText = """
                <u>Самовывоз</u>: Выдача заказа производится по адресу: г. Ростов-на-Дону, ул. Доватора, 142А, рынок «Молот», павильон 22. Оплатить заказ можно наличными или банковской картой в магазине.
                               
                <u>Доставка</u>: Мы отправляем заказы сервисом «Яндекс-маркет-доставка». Получить заказ вы сможете в любом пункте выдачи «Яндекс-маркет», который вы укажете при оформлении заказа.
                Доставка для вас бесплатная при заказе на сумму свыше 2000 руб.
                Оплатить заказ вы сможете по ссылке, которую вам вышлет наш менеджер при согласовании заказа.
                """;

        rewriteMessage(form.user(), messageText, keyboardRow);
    }

    @Override
    public void drawProductInstructionForm(ProductInstructionForm form) {
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton("Назад", new ProductAboutCmd(null, form.product().getId()))
        );

        String messageText = "Описание товара\n\n" + form.product().getDescription();

        rewriteMessage(form.user(), messageText, keyboardRow);
    }

    @Override
    public void drawErrorForm(ErrorForm form) {
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton("Вернуться в корзину", new BasketCmd(null)),
                getButton(new IndexCmd(null))
        );

        String messageText = "Во время работы возникла ошибка\nПожалуйста помните что при вводе адреса он должен начинаться с \"г\".\nПри вводе номера телефона он должен начинаться с \"+7\"";

        rewriteMessage(form.user(), messageText, keyboardRow);
    }

    @Override
    public void drawDistributionModeForm(DistributionModeForm form) {
        InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
                getButton(new DeliveryCmd(null)),
                getButton(new SelfPickupCmd(null))
        );

        String messageText = "Пожалуйста выберите способ доставки\n";

        rewriteMessage(form.user(), messageText, keyboardRow);
    }

    @Override
    public void drawOrderAdditionalInfoAddressForm(OrderAdditionalInfoAddressForm form) {
        String messageText = "Пожалуйста введите адресс доставки.\nАдрес должен начинаться с \"г.\"\n" +
                "Например: г. Ростов ул. Большая Садовая 438б";

        sendMessage(form.user(), messageText);
    }

    @Override
    public void drawOrderAdditionalInfoPhoneForm(OrderAdditionalInfoPhoneForm form) {
        String messageText = "Пожалуйста введите свой номер телефона.\nНомер должен начинаться с \"+7\"\n" +
                "Например: +79281184838";

        sendMessage(form.user(), messageText);
    }

    @Override
    public void OrderAdditionalInfoPhoneInvalidForm(OrderAdditionalInfoPhoneInvalidForm form) {
        String messageText = "Пожалуйста введите свой номер телефона.\nНомер должен начинаться с \"+7\"\n" +
                "Например: +79281184838\n\n" +
                "Ошибка в номере телефона:\n" +
                "Вы ввели: %s\n".formatted(form.invalidPhoneNumber()) +
                "Номер телефона обязательно должен начинаться с \"+7\", содержать 11 цифр от 0 до 9, между цифрами допускаются только знаки пробелов и тире \"-\" ";
        sendMessage(form.user(), messageText);
    }

    @Override
    public void draw(Error error) {
        log.error("=== error inside the app: {}", error.toString());
    }

    private InlineKeyboardButton getButton(String buttonText, Command command) {
        return InlineKeyboardButton.builder()
                .text(buttonText)
                .callbackData(command.toString())
                .build();
    }

    private InlineKeyboardButton getButton(Command command) {
        String text = switch (command) {
            case AboutCmd cmd -> "О нас";
            case ForWholesalerCmd cmd -> "Оптовикам";
            case PayAndDeliveryCmd cmd -> "Оплата и доставка";
            case AddToBasketCmd cmd -> "Добавить в корзину";
            case BasketCmd cmd -> "\uD83D\uDED2Посмотреть корзину\uD83D\uDED2";
            case CatalogCmd cmd -> "Каталог товаров";
            case DoOrderCmd cmd -> "Оформить заказ";
            case FolderCmd cmd -> PathUtil.getFolderName(cmd.path());
            case IndexCmd cmd -> "На главную";
            case InstructionCmd cmd -> "Описание товара";
            case ProductAboutCmd cmd -> productService.getById(cmd.productId()).ok().get().getName();
            case QuantityMinusCmd cmd -> "Убрать из корзины";
            case QuantityPlusCmd cmd -> "Добавить в корзину";
            case ClearBasketCmd cmd -> "Очистить корзину";
            case DoNothing cmd -> "¯\\_(ツ)_/¯";
            case DeliveryCmd cdm -> "Доставка";
            case SelfPickupCmd cmd -> "Самовывоз";
            case OrderAdditionalInfoAddressCmd cmd -> "This command should not appear in user interface.";
            case OrderAdditionalInfoPhoneCmd cmd -> "This command should not appear in user interface.";
            case StartCmd cmd -> "Restart session. This command should not appear in user interface.";
        };
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(command.toString())
                .build();
    }

    private void sendMessage(User user, String messageText, List<InlineKeyboardRow> keyboardRowList) {
        Long telegramUserId;
        Integer messageId;
        switch (telegramInfoService.getByUser(user)) {
            case ResultOk<TelegramInfo, Error> ok -> {
                telegramUserId = ok.result().getTelegramId();
                messageId = ok.result().getLastMessageId();
            }
            case ResultError<TelegramInfo, Error> err -> {
                draw(err.err());
                return;
            }
        }
        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboard(keyboardRowList)
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(telegramUserId)
                .text(messageText)
                .parseMode("HTML")
                .replyMarkup(keyBoard)
                .build();

        try {
            if (!(messageId == null)) {
                DeleteMessage deleteMessage = DeleteMessage.builder()
                        .chatId(telegramUserId)
                        .messageId(messageId)
                        .build();
                client.execute(deleteMessage);
            }
            log.trace("=== try to send message ===");
            Message res = client.execute(message);
            telegramInfoService.update(telegramUserId, null, null, null, res.getMessageId());
            log.trace("=== send message: {} ===", res);
        } catch (TelegramApiException e) {
            log.error("Telegram error during sending message: ", e);
        }
    }

    private void sendMessage(User user, String messageText) {
        Long telegramUserId;
        Integer messageId;
        switch (telegramInfoService.getByUser(user)) {
            case ResultOk<TelegramInfo, Error> ok -> {
                telegramUserId = ok.result().getTelegramId();
                messageId = ok.result().getLastMessageId();
            }
            case ResultError<TelegramInfo, Error> err -> {
                draw(err.err());
                return;
            }
        }

        SendMessage message = SendMessage.builder()
                .chatId(telegramUserId)
                .text(messageText)
                .parseMode("HTML")
                .build();

        try {
            if (!(messageId == null)) {
                DeleteMessage deleteMessage = DeleteMessage.builder()
                        .chatId(telegramUserId)
                        .messageId(messageId)
                        .build();
                client.execute(deleteMessage);
            }
            log.trace("=== try to send message ===");
            Message res = client.execute(message);
            telegramInfoService.update(telegramUserId, null, null, null, res.getMessageId());
            log.trace("=== send message: {} ===", res);
        } catch (TelegramApiException e) {
            log.error("Telegram error during sending message: ", e);
        }
    }

    private void rewriteMessage(User user, String messageText, InlineKeyboardRow keyboardRow) {
        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboardRow(keyboardRow)
                .build();
        rewriteMessage(user, messageText, keyBoard);
    }

    private void rewriteMessage(User user, String messageText, List<InlineKeyboardRow> keyboardRowList) {
        var keyBoard = InlineKeyboardMarkup.builder()
                .keyboard(keyboardRowList)
                .build();
        rewriteMessage(user, messageText, keyBoard);
    }

    private void rewriteMessage(User user, String messageText, InlineKeyboardMarkup keyboard) {
        Long telegramUserId;
        Integer messageId;
        switch (telegramInfoService.getByUser(user)) {
            case ResultOk<TelegramInfo, Error> ok -> {
                telegramUserId = ok.result().getTelegramId();
                messageId = ok.result().getLastMessageId();
            }
            case ResultError<TelegramInfo, Error> err -> {
                draw(err.err());
                return;
            }
        }

        EditMessageText message = EditMessageText.builder()
                .chatId(telegramUserId)
                .messageId(messageId)
                .text(messageText)
                .parseMode("HTML")
                .build();
        EditMessageReplyMarkup replyMarkup = EditMessageReplyMarkup.builder()
                .chatId(telegramUserId)
                .messageId(messageId)
                .replyMarkup(keyboard)
                .build();
        try {
            log.trace("Try to rewrite telegram message");
            client.execute(message);
            client.execute(replyMarkup);
        } catch (TelegramApiException e) {
            log.error("Telegram error during rewriting message: ", e);
        }
    }

//    private void closeQuery(AnswerCallbackQuery answerCallbackQuery) {
//        try {
//            log.trace("Try to close telegram query");
//            client.execute(answerCallbackQuery);
//        } catch (TelegramApiException e) {
//            log.error("Telegram error during closing query: ", e);
//        }
//    }
//
//    private void closeQueryAndRewriteMessage(AnswerCallbackQuery answerCallbackQuery,
//                                             EditMessageText messageText,
//                                             EditMessageReplyMarkup messageReplyMarkup) {
//        closeQuery(answerCallbackQuery);
//        rewriteMessage(messageText, messageReplyMarkup);
//
//    }


}
