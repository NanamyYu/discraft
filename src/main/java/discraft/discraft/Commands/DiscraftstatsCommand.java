package discraft.discraft.Commands;

import discraft.discraft.Database;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Objects;

import static discraft.discraft.Constants.MESSAGES.STATS_REPLY;

public class DiscraftstatsCommand implements CommandExecutor {

    final Database database;

    final Server server;

    public DiscraftstatsCommand(Database database, Server server) {
        this.database = database;
        this.server = server;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return false;
        }
        HashMap<String, String> linkedUsers = this.database.getLinkedUsersHashMap();
        int registered = linkedUsers.keySet().size();
        int online = server.getOnlinePlayers().size();
        sender.sendMessage(String.format(STATS_REPLY.toString(), registered, online));
        return true;
    }
}
