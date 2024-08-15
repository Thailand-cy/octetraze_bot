package io.tonme.minebot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Getter
@AllArgsConstructor
public enum RedisKeysEnum {
    TELEGRAM_USER_INFO("io:tonme.minebot:tg_user_info","telegram user info"),
    TELEGRAM_USER_TOKEN("io:tonme.minebot:tg_user_token","telegram user token"),
    TELEGRAM_USER_CHATID("io:tonme.minebot:MineWar:tg_user_chat_id","telegram user chatId"),
    TELEGRAM_CACHE_MESSAGE_IDS("io:tonme.minebot:cache_message_ids:","telegram cached message ids");

    private String key;
    private String desc;
}
