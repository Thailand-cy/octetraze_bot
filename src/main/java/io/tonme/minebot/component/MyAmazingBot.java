package io.tonme.minebot.component;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.StringUtil;
import io.tonme.minebot.config.BotConfig;
import io.tonme.minebot.entity.BotMenuCommandsEntity;
import io.tonme.minebot.enums.GameConfigEnum;
import io.tonme.minebot.enums.RedisKeysEnum;
import io.tonme.minebot.handler.BotExceptionHandler;
import io.tonme.minebot.handler.BotMessageHandler;
import io.tonme.minebot.service.BotMenuCommandsService;
import io.tonme.minebot.util.RedisUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonWebApp;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Getter
@Component
public class MyAmazingBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private TelegramClient telegramClient;
    @Resource
    private BotConfig botConfig;
    @Resource
    private BotMessageHandler botMessageHandler;
    @Resource
    private BotExceptionHandler botExceptionHandler;
    @Resource
    private BotMenuCommandsService botMenuCommandsService;

    @PostConstruct
    public void initAmazingBot() {
        telegramClient = new OkHttpTelegramClient(getBotToken());
        registerCommands(telegramClient);
    }

    public void registerCommands(TelegramClient telegramClient) {
        SetChatMenuButton setChatMenuButton = new SetChatMenuButton();
        MenuButtonWebApp menuButtonWebApp = new MenuButtonWebApp("Play", new WebAppInfo(GameConfigEnum.MINE_WAR.getGameUrl()));

        setChatMenuButton.setMenuButton(menuButtonWebApp);
        try {
            telegramClient.executeAsync(setChatMenuButton);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        Message message = update.getMessage();
//        if(update.hasInlineQuery()){
//            botMessageHandler.logicHandleInlineQuery(update.getInlineQuery().getId(), update.getInlineQuery().getQuery());
//        }
        if (update.hasMessage() && message.hasText()) {
            RedisUtils.hashPut(RedisKeysEnum.TELEGRAM_USER_CHATID.getKey(),String.valueOf(message.getFrom().getId()),message.getChatId());
            log.info("Bot received message:【"+message.getFrom().getId()+"】===> "+message.getText());
            try {
                botMessageHandler.logicMatchMessage(telegramClient,message);
            } catch (Exception e) {
                e.printStackTrace();
                botExceptionHandler.handleExceptionMessage(message.getChatId());
            }
        }
        else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            if(StringUtil.isNotBlank(callbackQuery.getGameShortName())){
                try {
                    botMessageHandler.logicMatchGame(callbackQuery);
                } catch (Exception e) {
                    e.printStackTrace();
                    botExceptionHandler.handleExceptionMessage(callbackQuery.getMessage().getChatId());
                }
            } else {
                try {
                    botMessageHandler.logicMatchCallbackQuery(callbackQuery);
                } catch (Exception e) {
                    e.printStackTrace();
                    botExceptionHandler.handleExceptionMessage(callbackQuery.getMessage().getChatId());
                }
            }
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("\033[36m\n" +
                "┏┳┓  ┓             ┳┓     ┳┓      •    \n" +
                " ┃ ┏┓┃┏┓┏┓┏┓┏┓┏┳┓  ┣┫┏┓╋  ┣┫┓┏┏┓┏┓┓┏┓┏┓\n" +
                " ┻ ┗ ┗┗ ┗┫┛ ┗┻┛┗┗  ┻┛┗┛┗  ┛┗┗┻┛┗┛┗┗┛┗┗┫\n" +
                "         ┛                            ┛\n\033[0m");
    }
}