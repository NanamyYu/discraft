package discraft.discraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public final class Discraft extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup login

        // Check if files are here

        Path baseDir = Path.of(Constants.BASE_DIR_PATHNAME);
        Path linkedUsersPath = Path.of(Constants.LINKED_PATHNAME);
        Path regCodesPath = Path.of(Constants.REG_CODES_PATHNAME);
        Path loginCodesPath = Path.of(Constants.LOGIN_CODES_PATHNAME);

        if (!Files.exists(baseDir)) {
            try {
                Files.createDirectory(baseDir);
            }
            catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, ex.toString());
            }
        }
        if (!Files.exists(linkedUsersPath)) {
            try {
                Files.createFile(linkedUsersPath);
                Files.writeString(linkedUsersPath, "{}");
            }
            catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, ex.toString());
            }
        }
        if (!Files.exists(regCodesPath)) {
            try {
                Files.createFile(regCodesPath);
                Files.writeString(regCodesPath, "{}");
            }
            catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, ex.toString());
            }
        }
        if (!Files.exists(loginCodesPath)) {
            try {
                Files.createFile(loginCodesPath);
                Files.writeString(loginCodesPath, "{}");
            }
            catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, ex.toString());
            }
        }
        PluginConfig config = new PluginConfig(Constants.CONFIG_PATHNAME);
        getServer().getPluginManager().registerEvents(new EventListener(this, config), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
