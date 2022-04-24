package discraft.discraft;

import org.bukkit.plugin.java.JavaPlugin;

public final class Discraft extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginConfig config = new PluginConfig(Constants.CONFIG_PATHNAME);
        Database database = new Database(Constants.BASE_DIR_PATHNAME,
                Constants.LINKED_PATHNAME, Constants.REG_CODES_PATHNAME, Constants.LOGIN_CODES_PATHNAME);
        getServer().getPluginManager().registerEvents(new EventListener(this, config, database), this);
    }
}
