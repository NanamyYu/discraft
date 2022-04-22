package discraft.discraft;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeoutKickTask extends BukkitRunnable {
    private final Player player;

    public TimeoutKickTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        player.kickPlayer("Login timed out");
    }

}