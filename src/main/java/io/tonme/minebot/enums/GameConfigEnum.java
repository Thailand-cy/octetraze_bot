package io.tonme.minebot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@AllArgsConstructor
public enum GameConfigEnum {
//    MINE_WAR("Mine War", "ts_mine_war","https://beta.hotfi.io");  // 测试
    MINE_WAR("Octocraze", "Octocraze","https://t.me/ts_ots_bot/ts_ots_app","https://t.me/ts_ots_bot/ts_ots_app", "https://t.me/ts_ots_bot/ts_ots_app");   // 生产

    private String gameName;
    private String gameShortName;
    private String gameUrl;
    private String gameUrlDev;
    private String gameMiniApp;

    public static String getUrlByShortName(String gsn){
        EnumSet<GameConfigEnum> set = EnumSet.allOf(GameConfigEnum.class);
        AtomicReference<String> url = new AtomicReference<>();
        set.forEach(gameConfigEnum -> {
            if(gameConfigEnum.gameShortName.equals(gsn))
                url.set(gameConfigEnum.gameUrl);
        });
        return url.get();
    }

    public static String getUrlByShortName(String gsn, boolean isDev){
        EnumSet<GameConfigEnum> set = EnumSet.allOf(GameConfigEnum.class);
        AtomicReference<String> url = new AtomicReference<>();
        set.forEach(gameConfigEnum -> {
            if(gameConfigEnum.gameShortName.equals(gsn))
                if(isDev){
                    url.set(gameConfigEnum.gameUrlDev);
                } else {
                    url.set(gameConfigEnum.gameUrl);
                }
        });
        return url.get();
    }

    public static String getUrlByName(String gn){
        EnumSet<GameConfigEnum> set = EnumSet.allOf(GameConfigEnum.class);
        AtomicReference<String> url = new AtomicReference<>();
        set.forEach(gameConfigEnum -> {
            if(gameConfigEnum.gameName.equals(gn))
                url.set(gameConfigEnum.gameUrl);
        });
        return url.get();
    }

    public static String getUrlByName(String gn ,boolean isDev){
        EnumSet<GameConfigEnum> set = EnumSet.allOf(GameConfigEnum.class);
        AtomicReference<String> url = new AtomicReference<>();
        set.forEach(gameConfigEnum -> {
            if(gameConfigEnum.gameName.equals(gn))
                if(isDev){
                    url.set(gameConfigEnum.gameUrlDev);
                } else {
                    url.set(gameConfigEnum.gameUrl);
                }
        });
        return url.get();
    }

    public static String getShortNameByName(String gn){
        EnumSet<GameConfigEnum> set = EnumSet.allOf(GameConfigEnum.class);
        AtomicReference<String> shortName = new AtomicReference<>();
        set.forEach(gameConfigEnum -> {
            if(gameConfigEnum.gameName.equals(gn))
                shortName.set(gameConfigEnum.gameShortName);
        });
        return shortName.get();
    }

    public static List<GameConfigEnum> getAll(){
        return EnumSet.allOf(GameConfigEnum.class).stream().toList();
    }
}
