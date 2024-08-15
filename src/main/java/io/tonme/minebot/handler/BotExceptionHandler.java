package io.tonme.minebot.handler;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
public class BotExceptionHandler {
    @Resource
    private BotMessageHandler botMessageHandler;

    public void handleExceptionMessage(long chatId){
        botMessageHandler.sendMessage(chatId, "An error occurred, please try again later, or ask the group for help\uD83E\uDEE1\uD83E\uDEE1");
    }
}
