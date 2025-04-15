package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;

import java.util.Optional;

//public class Products implements FsmState {
//
//    private final Logger log = LoggerFactory.getLogger(Products.class);
//
////    private final ProductRepository productRepository;
////    private final AbsSender sender;
////
////    public Products(ProductRepository productRepository, AbsSender sender) {
////        this.productRepository = productRepository;
////        this.sender = sender;
////    }
//
//    @Override
//    public Optional<FsmState> doWork(FsmContextHolder context, Update update) {
////        var chatId = update.getCallbackQuery().getFrom().getId().toString();
////        var products = productRepository.findAll();
////        List<InlineKeyboardButton> buttons = new ArrayList<>(products.size());
////        for (Product p : products) {
////            buttons.add(
////                    InlineKeyboardButton.builder()
////                            .text(p.getName())
////                            .callbackData("product/" + p.getName() + "/about")
////                            .build()
////            );
////        }
////        buttons.add(
////                InlineKeyboardButton.builder()
////                        .text("Назад")
////                        .callbackData("index")
////                        .build()
////        );
////        var keyBoard = InlineKeyboardMarkup.builder()
////                .keyboardRow(buttons)
////                .build();
////        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
////                .callbackQueryId(update.getCallbackQuery().getId())
////                .build();
////        SendMessage message = SendMessage.builder()
////                .chatId(chatId)
////                .text("Каталог")
////                .replyMarkup(keyBoard)
////                .build();
////
////        try {
////            sender.execute(close);
////            sender.execute(message);
////        } catch (TelegramApiException e) {
////            log.error("some err", e);
////        }
//        return Optional.empty();
//    }
//
//    @Override
//    public Form getForm() {
//        // todo
//        return null;
//    }
//}
