package xyz.shoraii.testplugin;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class EventListener implements Listener {

    final JavaPlugin plugin;

    final PluginConfig config;

    public EventListener(JavaPlugin plugin, PluginConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Gson gson = new Gson();
        String name = e.getPlayer().getName();

        Path linkedUsersPath = Path.of(Constants.LINKED_PATHNAME);
        Path regCodesPath = Path.of(Constants.REG_CODES_PATHNAME);
        Path loginCodesPath = Path.of(Constants.LOGIN_CODES_PATHNAME);
        String linkedUsersJson;
        String regCodesJson;
        String loginCodesJson;
        try {
            linkedUsersJson = Files.readString(linkedUsersPath);
        }
        catch (IOException ex) {
            Bukkit.broadcastMessage(ex.toString());
            linkedUsersJson = "{}";
        }
        try {
            regCodesJson = Files.readString(regCodesPath);
        }
        catch (IOException ex) {
            Bukkit.broadcastMessage(ex.toString());
            regCodesJson = "{}";
        }
        try {
            loginCodesJson = Files.readString(loginCodesPath);
        }
        catch (IOException ex) {
            Bukkit.broadcastMessage(ex.toString());
            loginCodesJson = "{}";
        }
        HashMap<String, String> nameToDiscord = gson.fromJson(linkedUsersJson, HashMap.class);
        HashMap<String, String> nameToRegCode = gson.fromJson(regCodesJson, HashMap.class);
        HashMap<String, String> nameToLoginCode = gson.fromJson(loginCodesJson, HashMap.class);
        if (nameToDiscord.containsKey(name)) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10000));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 10000));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 10000));
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            String loginMessage = String.format(Constants.MESSAGES.LOGIN_PROMPT.toString(), config.botNick);
            e.getPlayer().sendMessage(loginMessage);
        } else {
            int code = ThreadLocalRandom.current().nextInt(1000000);
            String codeStr = String.format("%06d", code);
            String kickMessage = String.format(Constants.MESSAGES.REG_PROMPT.toString(), this.config.botNick, codeStr);
            nameToRegCode.put(name, codeStr);
            nameToLoginCode.put(name, "None");
            regCodesJson = gson.toJson(nameToRegCode);
            loginCodesJson = gson.toJson(nameToLoginCode);
            try {
                Files.writeString(regCodesPath, regCodesJson);
            }
            catch (IOException ex) {
                Bukkit.broadcastMessage(ex.toString());
            }
            try {
                Files.writeString(loginCodesPath, loginCodesJson);
            }
            catch (IOException ex) {
                Bukkit.broadcastMessage(ex.toString());
            }
            e.getPlayer().kickPlayer(kickMessage);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Gson gson = new Gson();
        String name = e.getPlayer().getName();
        String message = e.getMessage();
        Path loginCodesPath = Path.of(Constants.LOGIN_CODES_PATHNAME);
        String loginCodesJson;
        try {
            loginCodesJson = Files.readString(loginCodesPath);
        }
        catch (IOException ex) {
            Bukkit.broadcastMessage(ex.toString());
            loginCodesJson = "{}";
        }
        HashMap<String, String> nameToLoginCode = gson.fromJson(loginCodesJson, HashMap.class);
        if (!Objects.equals(nameToLoginCode.get(name), "None") && Objects.equals(nameToLoginCode.get(name), message)) {
            e.getPlayer().sendMessage(Constants.MESSAGES.CORRECT_CODE.toString());
            BukkitTask unblockTask = new UnblockTask(e.getPlayer()).runTask(this.plugin);
            nameToLoginCode.put(name, "None");
            loginCodesJson = gson.toJson(nameToLoginCode);
            try {
                Files.writeString(loginCodesPath, loginCodesJson);
            }
            catch (IOException ex) {
                Bukkit.broadcastMessage(ex.toString());
            }
            e.setCancelled(true);
            return;
        }
        e.getPlayer().sendMessage(Constants.MESSAGES.INCORRECT_CODE.toString());
        e.setCancelled(true);
    }
}
