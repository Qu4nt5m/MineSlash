package me.theboykiss.ovh.mineslash;


import me.theboykiss.ovh.mineslash.Handlers.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class MineSlash extends Plugin implements Listener {
    private String lobbyServerName;
    private String sentToLobbyMessageSingle;
    private List<String> sentToLobbyMessageMulti;
    private boolean useMultiLineMessage;

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new LobbyHubCommand());
        loadConfig();
    }

    private void loadConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }

            File configFile = new File(getDataFolder(), "config.yml");

            if (!configFile.exists()) {
                try (InputStream in = getResourceAsStream("config.yml")) {
                    Files.copy(in, configFile.toPath());
                }
            }

            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);

            lobbyServerName = config.getString("lobby-server", "lobby");
            sentToLobbyMessageSingle = config.getString("messages.sent-to-lobby-single", "&aHas sido enviado al servidor &b<lobby>&a.");
            sentToLobbyMessageMulti = config.getStringList("messages.sent-to-lobby-multi");
            useMultiLineMessage = config.getBoolean("settings.use-multi-line-message", false);

            getLogger().info("Config loaded successfully");
            getLogger().info("use-multi-line-message: " + useMultiLineMessage);
            getLogger().info("sent-to-lobby-multi: " + sentToLobbyMessageMulti);

        } catch (IOException e) {
            e.printStackTrace();
            getLogger().severe("Error loading config file");
        }
    }

    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ServerInfo lobbyServer = ProxyServer.getInstance().getServerInfo(lobbyServerName);

        if (event.getKickedFrom() != lobbyServer) {
            event.setCancelled(true);
            event.setCancelServer(lobbyServer);

            if (useMultiLineMessage) {
                for (String line : sentToLobbyMessageMulti) {
                    String formattedLine = Utils.format(line.replace("<lobby>", lobbyServerName));
                    sendMessage(player, formattedLine);
                }
            } else {
                String formattedMessage = Utils.format(sentToLobbyMessageSingle.replace("<lobby>", lobbyServerName));
                sendMessage(player, formattedMessage);
            }
        }
    }


    public class LobbyHubCommand extends Command {
        public LobbyHubCommand() {
            super("lobby", "", "hub");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (!(sender instanceof ProxiedPlayer)) return;
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (getName().equalsIgnoreCase("lobby")) {
                player.connect(ProxyServer.getInstance().getServerInfo("lobby"));
            } else if (getName().equalsIgnoreCase("hub")) {
                player.connect(ProxyServer.getInstance().getServerInfo("lobby"));
            }
        }
    }

    private void sendMessage(ProxiedPlayer player, String message) {
        player.sendMessage(new TextComponent(message));
    }
}
