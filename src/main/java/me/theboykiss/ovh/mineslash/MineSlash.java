package me.theboykiss.ovh.mineslash;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.ServerKickEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class MineSlash extends Plugin implements Listener {
    private String lobbyServerName;

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        loadConfig();
    }

    private void loadConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }

            File file = new File(getDataFolder(), "config.yml");

            if (!file.exists()) {
                try (InputStream in = getResourceAsStream("config.yml")) {
                    Files.copy(in, file.toPath());
                }
            }

            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            lobbyServerName = config.getString("lobby-server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ServerInfo lobbyServer = ProxyServer.getInstance().getServerInfo(lobbyServerName);

        if (event.getKickedFrom() != lobbyServer) {
            event.setCancelled(true);
            event.setCancelServer(lobbyServer);
            player.sendMessage(ChatColor.GREEN + "Has sido enviado al " + lobbyServerName + ".");
        }
    }
}
