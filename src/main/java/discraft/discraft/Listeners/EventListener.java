package discraft.discraft.Listeners;

import discraft.discraft.Constants;
import discraft.discraft.Database;
import discraft.discraft.PluginConfig;
import discraft.discraft.Statistics.SessionStatistics;
import discraft.discraft.Tasks.UnblockTask;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class EventListener implements Listener {

    final JavaPlugin plugin;

    final PluginConfig config;

    final Database database;

    public EventListener(JavaPlugin plugin, PluginConfig config, Database database) {
        this.plugin = plugin;
        this.config = config;
        this.database = database;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        String name = e.getPlayer().getName();
        HashMap<String, String> nameToDiscord = this.database.getLinkedUsersHashMap();
        if (nameToDiscord.containsKey(name)) {
            this.waitForLogin(e);
        } else {
            String codeStr = this.waitForReg(e);
            this.database.pullRegCodes(name, codeStr);
            this.database.pullLoginCodes(name, "None");
        }
    }

    private void waitForLogin(PlayerJoinEvent e) {
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10000));
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 10000));
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 10000));
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
        String loginMessage = String.format(Constants.MESSAGES.LOGIN_PROMPT.toString(), this.config.botNick);
        e.getPlayer().sendMessage(loginMessage);
    }

    private String waitForReg(PlayerJoinEvent e) {
        int code = ThreadLocalRandom.current().nextInt(1000000);
        String codeStr = String.format("%06d", code);
        String kickMessage = String.format(Constants.MESSAGES.REG_PROMPT.toString(), this.config.botNick, codeStr);
        e.getPlayer().kickPlayer(kickMessage);
        return codeStr;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        String name = e.getPlayer().getName();
        SessionStatistics.loggedInPlayers.remove(name);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String name = e.getPlayer().getName();
        String message = e.getMessage();
        HashMap<String, String> nameToLoginCode = this.database.getLoginCodesHashMap();
        if (SessionStatistics.loggedInPlayers.contains(name)) {
            return;
        }
        if (!Objects.equals(nameToLoginCode.get(name), "None") && Objects.equals(nameToLoginCode.get(name), message)) {
            e.getPlayer().sendMessage(Constants.MESSAGES.CORRECT_CODE.toString());
            BukkitTask unblockTask = new UnblockTask(e.getPlayer()).runTask(this.plugin);
            SessionStatistics.loggedInPlayers.add(name);
            SessionStatistics.loginsThisSession.merge(name, 1, Integer::sum);
            this.database.pullLoginCodes(name, "None");
        } else {
            e.getPlayer().sendMessage(Constants.MESSAGES.INCORRECT_CODE.toString());
        }
        e.setCancelled(true);
    }
}
