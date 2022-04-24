package discraft.discraft;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginConfig {
    public String botNick;
    public String botToken;

    private static final String baseConfigJson = "{\"bot_nick\":\"INSERT BOT NAME\", \"bot_token\": \"INSERT BOT TOKEN\"}";

    Logger logger;

    public PluginConfig(String configPathName) {
        this.setLogger();
        HashMap<String, String> configHashMap = this.getConfigHashMap(configPathName);
        this.setBotNick(configHashMap.get("bot_nick"), configPathName);
        this.setBotToken(configHashMap.get("bot_token"), configPathName);
    }

    private void setLogger() {
        if (Bukkit.getServer() != null) {
            this.logger = Bukkit.getLogger();
        } else {
            this.logger = Logger.getLogger(Database.class.getName());
        }
    }

    private void setBotNick(String botNick, String configPathName) {
        HashMap<String, String> baseConfigJson = PluginConfig.getHashMapFromJson(PluginConfig.baseConfigJson);
        if ((botNick == null) || (botNick.equals(baseConfigJson.get("bot_nick")))) {
            String error = String.format("Found: %s. Fill bot_nick parameter correctly in %s in format:\n %s ",
                    botNick,
                    configPathName,
                    PluginConfig.baseConfigJson);
            this.logger.log(Level.SEVERE, String.format("FAILED TO SET BOT_NICK: %s \n", error));
        } else {
            this.botNick = botNick;
        }
    }

    private void setBotToken(String botToken, String configPathName) {
        HashMap<String, String> baseConfigJson = PluginConfig.getHashMapFromJson(PluginConfig.baseConfigJson);
        if ((botToken == null) || (botToken.equals(baseConfigJson.get("bot_token")))) {
            String error = String.format("Found: %s. Fill bot_token parameter correctly in %s in format:\n %s ",
                    botToken,
                    configPathName,
                    PluginConfig.baseConfigJson);
            this.logger.log(Level.SEVERE, String.format("FAILED TO SET BOT_TOKEN: %s \n", error));
        } else {
            this.botToken = botToken;
        }
    }

    private HashMap<String, String> getConfigHashMap(String configPathName) {
        Path configPath = Path.of(configPathName);
        String configJson = this.readConfigFile(configPath);
        return PluginConfig.getHashMapFromJson(configJson);
    }

    static HashMap<String, String> getHashMapFromJson(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(jsonString, type);
    }

    private String readConfigFile(Path filePath) {
        String configJson = PluginConfig.baseConfigJson;
        try {
            configJson = Files.readString(filePath);
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, String.format("FAILED TO READ FILE: %s \n", ex));
        }
        return configJson;
    }
}
