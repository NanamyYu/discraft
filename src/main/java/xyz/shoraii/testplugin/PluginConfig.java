package xyz.shoraii.testplugin;

import com.google.gson.Gson;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Level;

public class PluginConfig {
    public final String botNick;
    public final String botToken;

    public PluginConfig(String configFilename) {
        Path configPath = Path.of(configFilename);
        Gson gson = new Gson();
        String configJson = "";
        try {
            configJson = Files.readString(configPath);
        }
        catch (IOException ex) {
            configJson = "{\"bot_nick\":\"INSERT BOT NAME\", \"bot_token\": \"INSERT BOT TOKEN\"}";
            Bukkit.getLogger().log(Level.SEVERE, ex.toString());
        }
        HashMap<String, String> conf = gson.fromJson(configJson, HashMap.class);
        this.botNick = conf.get("bot_nick");
        this.botToken = conf.get("bot_token");
    }
}
