package io.tonme.minebot.pojo;

import io.tonme.minebot.entity.InlineKeyboardButtonEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BotBroadcastReq {
    private String message;
    private List<String> mediaUrls;
    private boolean play;
    private boolean back;
    private String mode;
    List<List<InlineKeyboardButtonEntity>> buttonList;
}
