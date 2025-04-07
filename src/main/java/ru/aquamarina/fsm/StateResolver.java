package ru.aquamarina.fsm;

import io.micronaut.context.ApplicationContext;
import jakarta.inject.Singleton;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.aquamarina.repository.ProductRepository;
import ru.aquamarina.repository.UserTelegramInfoRepository;

@Singleton
public class StateResolver {

    private final ApplicationContext applicationContext;

    public StateResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public FsmState resolve(Update update, AbsSender sender) {
        if (update.hasMessage()) {
            if (update.getMessage().getText().equals("/start")) {
                return new Init(getTelegramRepo(), sender);
            } else {
                return new UnknownCommand();
            }
        } else if (update.hasCallbackQuery()) {
            switch (update.getCallbackQuery().getData()) {
                case "index" -> {
                    return new Start(update, sender);
                }
                case "about" -> {
                    return new About(update, sender);
                }
                case "catalog" -> {
                    return new Products(applicationContext.getBean(ProductRepository.class), sender);
                }
                case String s when s.contains("product") && s.contains("about") -> {
                    return new ProductAbout(applicationContext.getBean(ProductRepository.class), sender);
                }
                default -> {
                    return new UnknownCommand();
                }
            }
        } else {
            return new UnknownCommand();
        }
    }

    public UserTelegramInfoRepository getTelegramRepo() {
        return applicationContext.getBean(UserTelegramInfoRepository.class);
    }
}
