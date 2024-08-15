package io.tonme.minebot.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@Accessors(chain = true)
public class BotConfig {
    //bot_Name
    @Value("${bot.botname}")
    private String botname;
    //bot_Token
    @Value("${bot.token}")
    private String token;

}
