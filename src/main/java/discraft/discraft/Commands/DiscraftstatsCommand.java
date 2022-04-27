package discraft.discraft.Commands;

import discraft.discraft.Database;
import discraft.discraft.Statistics.SessionStatistics;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static discraft.discraft.Constants.MESSAGES.STATS_REPLY;
import static discraft.discraft.Constants.MESSAGES.TOP_LOGGED_IN_REPLY;

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
        StringBuilder status = new StringBuilder(String.format(STATS_REPLY.toString(), registered, online) + "\n");
        status.append(String.format(TOP_LOGGED_IN_REPLY.toString(), "this session")).append("\n");

        Map<String, Integer> top =
                SessionStatistics.loginsThisSession.entrySet().stream()
                        .sorted(HashMap.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (Map.Entry<String, Integer> en: top.entrySet()) {
            status.append(String.format("%s - %d logins", en.getKey(), en.getValue())).append("\n");
        }
        sender.sendMessage(status.toString());
        return true;
    }
}
