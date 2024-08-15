package io.tonme.minebot.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserTelegramInfoRep {
    private long userId;
    private String username;
    private String profilePhoto;
    private boolean isGroup;
}
