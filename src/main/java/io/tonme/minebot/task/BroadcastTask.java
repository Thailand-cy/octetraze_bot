package io.tonme.minebot.task;

import io.tonme.minebot.component.MyAmazingBot;
import io.tonme.minebot.enums.GameConfigEnum;
import io.tonme.minebot.enums.RedisKeysEnum;
import io.tonme.minebot.handler.BotMessageHandler;
import io.tonme.minebot.util.RedisUtils;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.*;

//@Component
public class BroadcastTask {
    @Resource
    private MyAmazingBot myAmazingBot;
    @Resource
    private BotMessageHandler botMessageHandler;
    // 每天10点
    @Scheduled(cron = "0 0 10 * * ?")
    public void reportCurrentTime() {

        String msg = "This is a send broadcast message!!\uD83D\uDCAB\uD83D\uDCAB";
        List<InlineKeyboardRow> inlineKeyboardBottons = new ArrayList<>();
        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(InlineKeyboardButton
                .builder()
                .text("Play")
                .webApp(new WebAppInfo(GameConfigEnum.MINE_WAR.getGameUrl()))
                .build());
        inlineKeyboardBottons.add(row);
        Map<Object, Object> chatIds = RedisUtils.hashGetAll(RedisKeysEnum.TELEGRAM_USER_CHATID.getKey());
        Set<Object> keySet = chatIds.keySet();
        for(Object key : keySet){
            if(key != null){
                Object value = chatIds.get(key);
                if(value instanceof Long chatId)
                    botMessageHandler.sendPhotoMessageWithButtons4Broadcast(chatId, msg, "https://hot.tonme.io/img/20240712-181600.jpeg",inlineKeyboardBottons);
            }
        }
    }
}
