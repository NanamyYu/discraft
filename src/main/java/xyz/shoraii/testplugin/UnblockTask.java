package xyz.shoraii.testplugin;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class UnblockTask extends BukkitRunnable {
    private final Player player;

    public UnblockTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.setGameMode(GameMode.SURVIVAL);
    }

}