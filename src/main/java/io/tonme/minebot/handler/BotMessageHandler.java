package io.tonme.minebot.handler;

import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.util.StringUtil;
import io.tonme.minebot.entity.InlineKeyboardButtonEntity;
import io.tonme.minebot.entity.MyMessageEntity;
import io.tonme.minebot.enums.GameConfigEnum;
import io.tonme.minebot.enums.ParseModeEnum;
import io.tonme.minebot.enums.RedisKeysEnum;
import io.tonme.minebot.pojo.LoginEntityRep;
import io.tonme.minebot.pojo.LoginEntityReq;
import io.tonme.minebot.pojo.UserTelegramInfoRep;
import io.tonme.minebot.service.IUserService;
import io.tonme.minebot.util.FileTypeUtils;
import io.tonme.minebot.util.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.methods.send.SendGame;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAudio;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonWebApp;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class BotMessageHandler {
    @Value("${bot.autodel}")
    private int autoDelTimeMills;
    @Value("${bot.gameshortname}")
    private String gameShortName;
    @Value("${bot.gamename}")
    private String gameName;
    @Value("${bot.launch-mode}")
    private String launchMode;
    @Resource
    private IUserService userService;
    @Resource
    private TelegramBotSendhandler telegramBotSendhandler;
    @Value("${spring.profiles.active}")
    private String activeProfiles;

    public void logicMatchMessage(TelegramClient telegramClient, Message message) {
        String command = message.getText().toLowerCase();
        String sharing = "";
        if(command.startsWith("/start ")){
            sharing = command.split(" ")[1];
            command = "/start";
        }
        switch (command) {
            case "start", "/start", "/start@MineWarBot" -> showFirstMessage(message, sharing);
//            case "login", "/login", "/login@ts_tonme_bot" -> loginLogic(message, true);
//            case "play", "/play", "/play@ts_tonme_bot" -> showGameMessage(message.getChatId(), gameShortName);
//            case "games", "/games", "/games@ts_tonme_bot" -> showGames(message.getChatId());
//            case "help", "/help", "/help@ts_tonme_bot" -> showHelp(message.getChatId());
//            default -> showMenu(message.getChatId());
//            case "url" -> showGameButtonMessage(message.getChatId());
            default -> showPlayInfo(message.getChatId());
        }
    }

    public void showGameButtonMessage(Long chatId) {
        String msg = "Hot\uD83D\uDCA5 NEW Ton Game \uD83C\uDF1F MineWar⚔\uFE0F\n" +
                "\n" +
                "\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25TG-BOT MINE & PVP\n" +
                "\n" +
                "⚔\uFE0FMineWar is a camp war mining game based on the Ton chain. \n" +
                "Users choose a camp to join the game, complete daily or designated tasks to earn gold coins, exchange gold coins for mining tools, mine in their respective camps and collect $MINE tokens\uD83E\uDD1E\uD83C\uDFFB\n" +
                "\n" +
                "✅Start Bot & Click Open App\n" +
                "✅Tap to earn Gold Coins & Designated tasks to earn Gold Coins\n" +
                "✅Exchange gold coins for mining tools to mine $MINE tokens\n" +
                "✅Win $MINE tokens through PVP battles\n" +
                "✅Hold Minewar NFTs to obtain weekly token airdrops\n" +
                "\n" +
                "⬇\uFE0FJoin the Minewar game,  earn rewards, and forge your legend! ⬇\uFE0F\n" +
                "\n" +
                "Invite Friends to Get More Gold Coins And Power☑\uFE0F\n" +
                "\n" +
                "\uD83D\uDCB0Invite Friends\n" +
                "+1000 Gold Coins For You And Your Friend\uD83C\uDF1F\n" +
                "+100 Capacity Limit For You (The Same Camp)\uD83C\uDF1F\n\n" +
                "\uD83D\uDC90\uD835\uDC0B\uD835\uDC22\uD835\uDC27\uD835\uDC24\uD835\uDC2C\n" +
                "<a href=\"https://t.me/ts_tonme_bot/ts_app_tonme?startapp=DjIO5cRX\">MineWar</a>";
        SendMessage sendMessage = SendMessage.builder().chatId(chatId)
                .text(msg)
                .chatId(chatId)
                .parseMode("HTML")
                .build();

        String messageKey = UUID.randomUUID().toString();
        telegramBotSendhandler.sendMessage(new MyMessageEntity()
                .setMessage(sendMessage)
                .setMessageUnique(messageKey)
                .setChatId(chatId)
                .setAutoCloseMillSeconds(-1));
    }

    public void logicMatchCallbackQuery(CallbackQuery callbackQuery) {
        String queryData = callbackQuery.getData();
        String gameShortName = "";
        if(queryData != null && queryData.contains("#")){
            String[] split = queryData.split("#");
            queryData = split[0];
            if("play".equals(queryData)||"/play".equals(queryData)){
                gameShortName = GameConfigEnum.getShortNameByName(split[1]);
            }
        }
        switch (Objects.requireNonNull(queryData)) {
//            case "play", "/play" ->
//                    showGameMessage(callbackQuery.getMessage().getChatId(), gameShortName);
//            case "profile" ->
//                    showProfile(callbackQuery.getMessage().getChatId(), callbackQuery.getFrom().getId());
//            case "mission", "shopping" ->
//                    showComingSoon(callbackQuery.getMessage().getChatId());
//            case "community" -> showCommunity(callbackQuery.getMessage().getChatId());
//            case "sharing" -> showSharing(callbackQuery.getFrom().getId(),callbackQuery.getMessage().getChatId());
//            case "back" -> deleteMessage(callbackQuery.getMessage().getChatId(),callbackQuery.getMessage().getMessageId());
//            case "help" -> showHelp(callbackQuery.getMessage().getChatId());
            case "guide", "/guide" -> showGuide(callbackQuery.getMessage().getChatId());
            default -> showPlayInfo(callbackQuery.getMessage().getChatId());
        }
    }

    public void logicHandleInlineQuery(String inlineQueryId, String query){
        List<InlineQueryResult> results = new ArrayList<>();

        if (!query.isEmpty()) {
            String miniAppUrl = GameConfigEnum.MINE_WAR.getGameMiniApp();  // 替换为你的 Mini App URL
            InlineQueryResultArticle result = InlineQueryResultArticle.builder()
                    .id("1")
                    .title("MineWar")
                    .inputMessageContent(new InputTextMessageContent("点击打开 [Mini App](" + miniAppUrl + ")"))
                    .build();
            results.add(result);
        }

        AnswerInlineQuery answerInlineQuery = AnswerInlineQuery.builder()
                .inlineQueryId(inlineQueryId)
                .results(results)
                .build();
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(answerInlineQuery)
                    .setMessageUnique(messageKey)
                    .setChatId(-1)
                    .setAutoCloseMillSeconds(-1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showGuide(Long chatId) {
        String msg = "⚔\uFE0FMineWar is a camp war mining game based on the Ton chain. \n" +
                "\n" +
                "\uD83C\uDFAEPlayers choose a camp to join the game, complete daily or designated tasks to earn gold coins, exchange gold coins for mining tools, mine in their respective camps and collect $MINE tokens\n" +
                "\n" +
                "✅Start Bot & Click Open App\n" +
                "✅Tap to earn Gold Coins & Designated tasks to earn Gold Coins\n" +
                "✅Exchange gold coins for mining tools to mine $MINE tokens\n" +
                "✅Win $MINE tokens through PVP battles\n" +
                "✅Hold Minewar NFTs to obtain weekly token airdrops\n" +
                "\n" +
                "⬇\uFE0FJoin the Minewar game, earn rewards, and forge your legend! ⬇\uFE0F\n" +
                "\n<b>MineWar Guideline：</b>https://link.medium.com/IWVm0O5LlLb\n" +
                "\n" +
                "Invite Friends to Get More Gold Coins And Power\n" +
                "\n" +
                "Invite Friends\n" +
                "+1000 Gold Coins For You And Your Friend\uD83C\uDF1F\n" +
                "+100 Capacity Limit For You (The Same Camp)\uD83C\uDF1F\n" +
                "\n" +
                "\uD83D\uDCF2 Tap to earn / Click to Earn : Tap on the Gold \uD83D\uDCB0continue and watch your balance grow\n" +
                "\n" +
                "\uD83D\uDC49 <a href=\"https://link.medium.com/IWVm0O5LlLb\">Guideline</a> / <a href=\"https://t.me/MineWar_Announcement\">TG Channel</a> / <a href=\"https://t.me/MineWar_Community_Global\">TG Group</a> / <a href=\"https://x.com/PlayMineWar\">X</a>";
        List<List<InlineKeyboardButtonEntity>> inlineKeyboardBottons = createPlayButton();
        sendMessage(chatId, msg,ParseModeEnum.HTML.getMode(), inlineKeyboardBottons, false);
    }

    private void showSharing( Long userId, Long chatId) {
        String userSharingCode = userService.getUserSharingCode(userId);
        if(StringUtil.isBlank(userSharingCode))
            throw new RuntimeException();
        String msg = "🎁Rare weapon loot boxes await!" +
                "\n\nYour Invitation Code:\n\n" +
                "https://t.me/ts_tonme_bot?start="+ userSharingCode +
                "\n\nClick to copy<code> https://t.me/ts_tonme_bot?start=" + userSharingCode + "</code> " +
                "\n\n❗️This invitation code is only redeemable for NEW USERS. Invite your new friends to join and play!❗" +
                "\n\nBoth you and your friends can score exclusive rewards!\n\n"+
                "Share the code, redeem it, and unlock the mystery box for a chance to snatch rare weapons. Don't miss out! Join the action now!🎉" +
                "\n\n📲Play now on telegram: https://t.me/ts_tonme_bot";
        List<List<InlineKeyboardButtonEntity>> inlineKeyboardBottons = createBackButton();
        sendMessage(chatId, msg,ParseModeEnum.HTML.getMode(), inlineKeyboardBottons, false);
    }

    private void showHelp(long chatId) {
        List<List<InlineKeyboardButtonEntity>> inlineKeyboardBottons = createBackButton();
        sendPhotoMessage(chatId,"XXXX \uD83E\uDD14\uD83E\uDD14", "https://5b0988e595225.cdn.sohucs.com/images/20191105/052ee7620bba4cdaa0f4ccbb8b2ab5dd.gif", inlineKeyboardBottons);
    }

    private void showCommunity(Long chatId) {
        String msg = "\uD83D\uDE8EJoin our community to share battle strategies with fellow players, or trade gears! \n\nSwap tips, tales from the frontier, and best each other's high scores together. Victory is sweeter shared among comrades!\n\n\uD83C\uDF81Along with irregular community gifts and activities within, won't you join us in battle?\n\nhttps://t.me/+LJXIoVMFXCI3NzQ9";
        List<List<InlineKeyboardButtonEntity>> inlineKeyboardBottons = createBackButton();
//        sendPhotoMessage(chatId, msg, "https://hot.tonme.io/img/20240712-181600.jpeg",inlineKeyboardBottons);
        sendPhotoMessage(chatId, msg, "https://hot.tonme.io/img/20240712-181600.jpeg",inlineKeyboardBottons);
    }

    public void logicMatchGame(CallbackQuery callbackQuery) {
        boolean isDev = "dev".equals(activeProfiles);
        String url = GameConfigEnum.getUrlByShortName(callbackQuery.getGameShortName(),isDev);
        if(StringUtil.isBlank(url)){
            showGames(callbackQuery.getMessage().getChatId());
        }
        String userToken = RedisUtils.hashGet(RedisKeysEnum.TELEGRAM_USER_TOKEN.getKey(), String.valueOf(callbackQuery.getFrom().getId()));
        if(StringUtil.isBlank(userToken)){
            LoginEntityRep loginEntityRep = loginLogic(callbackQuery, false);
            if(loginEntityRep == null || loginEntityRep.getCode() != 200 || StringUtil.isBlank(loginEntityRep.getData().getToken()))
                throw new RuntimeException();
            userToken = loginEntityRep.getData().getToken();
        }
        AnswerCallbackQuery answerCallbackQuery = AnswerCallbackQuery.builder()
                .url(url+"?token="+userToken)
                .callbackQueryId(callbackQuery.getId())
                .build();
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(answerCallbackQuery)
                    .setMessageUnique(messageKey)
                    .setChatId(0L)
                    .setAutoCloseMillSeconds(-1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showGames(Long chatId) {
        List<List<InlineKeyboardButtonEntity>> inlineKeyboardButtons = new ArrayList<>();
        List<GameConfigEnum> gameConfigEnumList = GameConfigEnum.getAll();
        int rows = gameConfigEnumList.size() % 2 == 0 ? gameConfigEnumList.size() / 2: gameConfigEnumList.size() / 2 + 1;
        for(int i = 0 ; i < rows ; i++){
            List<InlineKeyboardButtonEntity> rowList = new ArrayList<>();
            if(gameConfigEnumList.get(2*i) != null){
                rowList.add(new InlineKeyboardButtonEntity()
                        .setText("Play "+gameConfigEnumList.get(2*i).getGameName())
                        .setCallbackData("play#"+gameConfigEnumList.get(2*i).getGameName()));
            }
            if(2*i+1 <= gameConfigEnumList.size() - 1 && gameConfigEnumList.get(2*i+1) != null){
                rowList.add(new InlineKeyboardButtonEntity()
                        .setText("Play "+gameConfigEnumList.get(2*i+1).getGameName())
                        .setCallbackData("play#"+gameConfigEnumList.get(2*i+1).getGameName()));
            }
            inlineKeyboardButtons.add(rowList);
        }
        sendPhotoMessage(chatId,"Enjoy the popular games.. \uD83C\uDFAE\uD83C\uDFAE","https://hot.tonme.io/img/20240712-181600.jpeg",inlineKeyboardButtons);
    }

    private void showComingSoon(long chatId) {
        sendPhotoMessage(chatId, "This is not open yet, please stay tuned ☕☕", "https://www.ospreyssupportersclub.co.uk/wp-content/uploads/2015/03/Coming-Soon.gif", autoDelTimeMills);
    }

    private void showProfile(Long chatId, long userId) {
        String dataJson = RedisUtils.hashGet(RedisKeysEnum.TELEGRAM_USER_INFO.getKey(), String.valueOf(userId));
        UserTelegramInfoRep userTelegramInfoRep = JSONObject.parseObject(dataJson, UserTelegramInfoRep.class);
        sendMessage(chatId, userTelegramInfoRep.toString());
    }

    private void showGameMessage(long chatId, String gameShortName) {
        SendGame sendGame = SendGame.builder()
                .chatId(chatId)
                .gameShortName(gameShortName)
                .build();
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(sendGame)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showFirstMessage(Message message, String sharing) {
        String username = " "+message.getFrom().getUserName();
        if(StringUtil.isBlank(username))
            username = "";
        String mkey = sendMessage(message.getChatId(), "\uD83C\uDF89 Welcome @"+username+" to the MineWar Telegram Battle! \uD83C\uDF89");
        LoginEntityRep loginEntityRep = userService.userLogin(new LoginEntityReq()
                .setTgId(String.valueOf(message.getFrom().getId()))
                .setVip(false)
                .setTgName(message.getFrom().getUserName())
                .setInviteCode(StringUtil.isBlank(sharing)?"":sharing));
        if(loginEntityRep == null)
            throw new RuntimeException();
        log.info("Login user info:"+ loginEntityRep);
        try{
            RedisUtils.hashPut(RedisKeysEnum.TELEGRAM_USER_TOKEN.getKey(), String.valueOf(loginEntityRep.getData().getUid()),loginEntityRep.getData().getToken());
            Integer mid = RedisUtils.getValue(RedisKeysEnum.TELEGRAM_CACHE_MESSAGE_IDS.getKey() + mkey);
            if(mid != null){
                deleteMessage(message.getChatId(), mid);
            }
        } catch (Exception ignored){}
        showPlayInfo(message.getChatId());
    }

    private void showPlayInfo(long chatId) {
        String msg = "\uD83C\uDFAE MinerWar: Play, Collect Gold Coins and $MINE, Join the Camp and Win Victory! \uD83D\uDE80\n" +
                "\n" +
                "\uD83C\uDFC6 Complete tasks to earn gold coins.\n" +
                "\uD83D\uDED2 Use gold coins to purchase mining carts.\n" +
                "⛏\uFE0F Stake mining carts to earn mining tokens ($MINE).\n" +
                "\uD83C\uDF0D Participate in world battles and compete in rankings for airdrops!\n" +
                "\n" +
                "\uD83C\uDF81 Join the battle, earn rewards, and rise to glory now! \uD83C\uDFC5";
        List<InlineKeyboardRow> inlineKeyboardBottons = new ArrayList<>();
        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(InlineKeyboardButton
                .builder()
                .text("⚔\uFE0FPlay")
                .webApp(new WebAppInfo("dev".equals(activeProfiles)?GameConfigEnum.MINE_WAR.getGameUrlDev():GameConfigEnum.MINE_WAR.getGameUrl()))
                .build());
        inlineKeyboardBottons.add(row);
        InlineKeyboardRow row2 = new InlineKeyboardRow();
        row2.add(InlineKeyboardButton
                .builder()
                .text("❤\uFE0F\u200D\uD83D\uDD25Guide")
                .callbackData("/guide")
                .build());
        inlineKeyboardBottons.add(row2);
        sendPhotoMessageWithButtons(chatId, msg, "https://hot.tonme.io/img/20240712-181600.jpeg",inlineKeyboardBottons);
    }

    private void showMenu(long chatId) {
        List<List<InlineKeyboardButtonEntity>> inlineKeyboardBottons = new ArrayList<>();
        List<InlineKeyboardButtonEntity> inlineKeyboardButtonRow = new ArrayList<>();
        inlineKeyboardButtonRow.add(new InlineKeyboardButtonEntity()
                .setText("\uD83C\uDFAE Play Now")
                .setCallbackData("play#"+gameName));
        inlineKeyboardButtonRow.add(new InlineKeyboardButtonEntity()
                .setText("\uD83E\uDD77\uD83C\uDFFF My Profile")
                .setCallbackData("profile"));
        inlineKeyboardBottons.add(inlineKeyboardButtonRow);

        List<InlineKeyboardButtonEntity> inlineKeyboardButtonRow2 = new ArrayList<>();
        inlineKeyboardButtonRow2.add(new InlineKeyboardButtonEntity()
                .setText("\uD83D\uDD5C Mission")
                .setCallbackData("mission"));
        inlineKeyboardButtonRow2.add(new InlineKeyboardButtonEntity()
                .setText("\uD83D\uDE8E Community")
                .setCallbackData("community"));
        inlineKeyboardBottons.add(inlineKeyboardButtonRow2);
        List<InlineKeyboardButtonEntity> inlineKeyboardButtonRow3 = new ArrayList<>();
        inlineKeyboardButtonRow3.add(new InlineKeyboardButtonEntity()
                .setText("\uD83C\uDF89 Sharing")
                .setCallbackData("sharing"));
        inlineKeyboardButtonRow3.add(new InlineKeyboardButtonEntity()
                .setText("\uD83C\uDF81 Shopping")
                .setCallbackData("shopping"));
        inlineKeyboardBottons.add(inlineKeyboardButtonRow3);
        sendPhotoMessage(chatId,"Mining tokens, engaging the enemy, and expanding alliances.\uD83D\uDE80\n" +
                "\n" +
                "We are ready to go, Sir!\uD83E\uDEE1", "https://hot.tonme.io/img/20240712-181600.jpeg", inlineKeyboardBottons);
    }

    private LoginEntityRep loginLogic(Message message, boolean showLoginInfo) {
        LoginEntityRep loginEntityRep = userService.userLogin(new LoginEntityReq()
                .setTgId(String.valueOf(message.getFrom().getId()))
                .setVip(false)
                .setTgName(message.getFrom().getUserName()));
        if(loginEntityRep == null){
            // login error, alert login error
            sendMessage(message.getChatId(), "User login error");
        } else {
            userService.userTelegramInfo(message.getFrom().getId());
        }
        if(showLoginInfo){
            sendMessage(message.getChatId(), loginEntityRep.toString());
        }
        return loginEntityRep;
    }

    private LoginEntityRep loginLogic(CallbackQuery callbackQuery, boolean showLoginInfo) {
        LoginEntityRep loginEntityRep = userService.userLogin(new LoginEntityReq()
                .setTgId(String.valueOf(callbackQuery.getFrom().getId()))
                .setVip(false)
                .setTgName(callbackQuery.getFrom().getUserName()));
        if(loginEntityRep == null){
            // login error, alert login error
            sendMessage(callbackQuery.getMessage().getChatId(), "User login error");
        }
        if(showLoginInfo){
            sendMessage(callbackQuery.getMessage().getChatId(), loginEntityRep.toString());
        }
        return loginEntityRep;
    }

    public void deleteMessage(TelegramClient telegramClient, long chatId, int messageId){
        DeleteMessage deleteMessage = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
        try {
            telegramClient.executeAsync(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    public String deleteMessage(long chatId, int messageId){
        DeleteMessage deleteMessage = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(deleteMessage)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendMessage(long chatId, String message){
        SendMessage sendMessage = SendMessage // Create a message object
                .builder()
                .chatId(chatId)
                .text(message)
                .build();
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(sendMessage)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendMessage(long chatId, String message, boolean autoDel){
        SendMessage sendMessage = SendMessage // Create a message object
                .builder()
                .chatId(chatId)
                .text(message)
                .build();
        try {
            long autoCloseMillSeconds = autoDelTimeMills;
            if(!autoDel) {
                autoCloseMillSeconds = -1;
            }
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(sendMessage)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(autoCloseMillSeconds));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendPhotoMessage(long chatId, String message, String photoUrl, long autoCloseMillSeconds){
        SendPhoto photo = SendPhoto.builder().chatId(chatId)
                .photo(new InputFile(photoUrl))
                .caption(message)
                .build();
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(photo)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(autoCloseMillSeconds));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendPhotoMessage(long chatId, String message, String photoUrl){
        SendPhoto photo = SendPhoto.builder().chatId(chatId)
                .photo(new InputFile(photoUrl))
                .caption(message)
                .build();
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(photo)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendPhotoMessage(long chatId, String message, String photoUrl, boolean autoDel){
        SendPhoto photo = SendPhoto.builder().chatId(chatId)
                .photo(new InputFile(photoUrl))
                .caption(message)
                .build();
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(photo)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(autoDelTimeMills));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendPhotoMessage(long chatId, String message, String photoUrl, List<List<InlineKeyboardButtonEntity>> inlineKeyboardButtons){
        SendPhoto photo = SendPhoto.builder().chatId(chatId)
                .photo(new InputFile(photoUrl))
                .caption(message)
                .build();

        // 创建按钮
        InlineKeyboardMarkup inlineKeyboardMarkup = createButtons(inlineKeyboardButtons);
        photo.setReplyMarkup(inlineKeyboardMarkup);
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(photo)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendPhotoMessage(long chatId, String message, String photoUrl, List<List<InlineKeyboardButtonEntity>> inlineKeyboardButtons, boolean autoDel){
        SendPhoto photo = SendPhoto.builder().chatId(chatId)
                .photo(new InputFile(photoUrl))
                .caption(message)
                .build();

        // 创建按钮
        InlineKeyboardMarkup inlineKeyboardMarkup = createButtons(inlineKeyboardButtons);
        photo.setReplyMarkup(inlineKeyboardMarkup);
        try {
            long autoCloseMillSeconds = autoDelTimeMills;
            if(!autoDel) {
                autoCloseMillSeconds = -1;
            }
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(photo)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(autoCloseMillSeconds));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendMessage(long chatId, String message, String parseMode, List<List<InlineKeyboardButtonEntity>> inlineKeyboardButtons, boolean autoDel){
        SendMessage sendMessage = SendMessage.builder().chatId(chatId)
                .text(message)
                .chatId(chatId)
                .parseMode(parseMode)
                .build();

        InlineKeyboardMarkup inlineKeyboardMarkup = createButtons(inlineKeyboardButtons);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            long autoCloseMillSeconds = autoDelTimeMills;
            if(!autoDel) {
                autoCloseMillSeconds = -1;
            }
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(sendMessage)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(autoCloseMillSeconds));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendPhotoMessageWithButtons(long chatId, String message, String photoUrl,  List<InlineKeyboardRow> inlineKeyboardButtons){
        SendPhoto photo = SendPhoto.builder().chatId(chatId)
                .photo(new InputFile(photoUrl))
                .caption(message)
                .build();

        // 创建按钮
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(inlineKeyboardButtons)
                .build();
        photo.setReplyMarkup(inlineKeyboardMarkup);
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(photo)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String sendPhotoMessageWithButtons4Broadcast(long chatId, String message, String photoUrl,  List<InlineKeyboardRow> inlineKeyboardButtons){
        SendPhoto photo = SendPhoto.builder().chatId(chatId)
                .photo(new InputFile(photoUrl))
                .caption(message)
                .build();

        // 创建按钮
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(inlineKeyboardButtons)
                .build();
        photo.setReplyMarkup(inlineKeyboardMarkup);
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage4Broadcast(new MyMessageEntity()
                    .setMessage(photo)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendPhotoMessageWithButtons(long chatId, String message, String photoUrl,  InlineKeyboardMarkup inlineKeyboardMarkup){
        SendPhoto photo = SendPhoto.builder().chatId(chatId)
                .photo(new InputFile(photoUrl))
                .caption(message)
                .build();

        if(inlineKeyboardMarkup != null)
            photo.setReplyMarkup(inlineKeyboardMarkup);
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(photo)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String sendPhotoMessageWithButtons4Broadcast(long chatId, String message, String photoUrl,  InlineKeyboardMarkup inlineKeyboardMarkup){
        SendPhoto photo = SendPhoto.builder().chatId(chatId)
                .photo(new InputFile(photoUrl))
                .caption(message)
                .build();

        if(inlineKeyboardMarkup != null)
            photo.setReplyMarkup(inlineKeyboardMarkup);
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage4Broadcast(new MyMessageEntity()
                    .setMessage(photo)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendMessageWithButtons(long chatId, String message, String mode, InlineKeyboardMarkup inlineKeyboardMarkup){
        SendMessage sendMessage = SendMessage.builder().chatId(chatId)
                .text(message)
                .chatId(chatId)
                .parseMode(mode)
                .build();
        if(inlineKeyboardMarkup != null)
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(sendMessage)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String sendMessageWithButtons4Broadcast(long chatId, String message, String mode, InlineKeyboardMarkup inlineKeyboardMarkup){
        SendMessage sendMessage = SendMessage.builder().chatId(chatId)
                .text(message)
                .chatId(chatId)
                .parseMode(mode)
                .build();
        if(inlineKeyboardMarkup != null)
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage4Broadcast(new MyMessageEntity()
                    .setMessage(sendMessage)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sendMediaGroup(long chatId, String message, List<String> mediaUrls, InlineKeyboardMarkup inlineKeyboardMarkup){
        List<InputMedia> media = new ArrayList<>();
        for(String mediaUrl: mediaUrls){
            String fileType = FileTypeUtils.getFileType(mediaUrl);
            switch (fileType) {
                case "image" -> media.add(new InputMediaPhoto(mediaUrl));
                case "video" -> media.add(new InputMediaVideo(mediaUrl));
                case "audio" -> media.add(new InputMediaAudio(mediaUrl));
            }
        }
        SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                .medias(media)
                .chatId(chatId)
                .build();
        if(inlineKeyboardMarkup != null)
            sendMediaGroup.setReplyMarkup(inlineKeyboardMarkup);
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage(new MyMessageEntity()
                    .setMessage(sendMediaGroup)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String sendMediaGroup4BroadCast(long chatId, String message, List<String> mediaUrls, InlineKeyboardMarkup inlineKeyboardMarkup){
        List<InputMedia> media = new ArrayList<>();
        for(String mediaUrl: mediaUrls){
            String fileType = FileTypeUtils.getFileType(mediaUrl);
            switch (fileType) {
                case "image" -> media.add(new InputMediaPhoto(mediaUrl));
                case "video" -> media.add(new InputMediaVideo(mediaUrl));
                case "audio" -> media.add(new InputMediaAudio(mediaUrl));
            }
        }
        SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                .medias(media)
                .chatId(chatId)
                .build();
        if(inlineKeyboardMarkup != null)
            sendMediaGroup.setReplyMarkup(inlineKeyboardMarkup);
        try {
            String messageKey = UUID.randomUUID().toString();
            telegramBotSendhandler.sendMessage4Broadcast(new MyMessageEntity()
                    .setMessage(sendMediaGroup)
                    .setMessageUnique(messageKey)
                    .setChatId(chatId)
                    .setAutoCloseMillSeconds(-1));
            return messageKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public InlineKeyboardMarkup createButtons(List<List<InlineKeyboardButtonEntity>> inlineKeyboardButtons) {
        boolean isDev = "dev".equals(activeProfiles);
        // 创建按钮
        List<InlineKeyboardRow> rowList = new ArrayList<>();
        for(List<InlineKeyboardButtonEntity> rowButtons : inlineKeyboardButtons){
            InlineKeyboardRow row = new InlineKeyboardRow();
            for(InlineKeyboardButtonEntity btn : rowButtons) {
                if(btn.getText().indexOf("Play") != -1 && "miniapp".equals(launchMode)){
                    String name = btn.getCallbackData().split("#")[1];
                    InlineKeyboardButton button = InlineKeyboardButton.builder()
                            .text(btn.getText())
                            .webApp(new WebAppInfo(GameConfigEnum.getUrlByName(name, isDev)))
                            .build();
                    row.add(button);
                } else {
                    InlineKeyboardButton button = InlineKeyboardButton.builder()
                            .text(btn.getText())
                            .callbackData(btn.getCallbackData())
                            .build();
                    row.add(button);
                }
            }
            rowList.add(row);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(rowList)
                .build();
        return inlineKeyboardMarkup;
    }

    @NotNull
    public List<List<InlineKeyboardButtonEntity>> createBackButton() {
        List<List<InlineKeyboardButtonEntity>> inlineKeyboardBottons = new ArrayList<>();
        List<InlineKeyboardButtonEntity> inlineKeyboardButtonRow = new ArrayList<>();
        inlineKeyboardButtonRow.add(new InlineKeyboardButtonEntity()
                .setText("Back to menu")
                .setCallbackData("back"));
        inlineKeyboardBottons.add(inlineKeyboardButtonRow);
        return inlineKeyboardBottons;
    }

    @NotNull
    public List<List<InlineKeyboardButtonEntity>> createPlayButton() {
        List<List<InlineKeyboardButtonEntity>> inlineKeyboardBottons = new ArrayList<>();
        List<InlineKeyboardButtonEntity> inlineKeyboardButtonRow = new ArrayList<>();
        inlineKeyboardButtonRow.add(new InlineKeyboardButtonEntity()
                .setText("\uD83D\uDC49Play MineWar Now\uD83D\uDC48")
                .setCallbackData("play#Mine War"));
        inlineKeyboardBottons.add(inlineKeyboardButtonRow);
        return inlineKeyboardBottons;
    }

}
