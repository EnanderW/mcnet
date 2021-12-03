package me.code.config;

import lombok.Getter;
import lombok.Setter;
import me.code.ServerInfo;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ServerConfig {

    private final YamlFile config;

    private int port;
    private String description;
    private List<ServerInfo> serverInfos;

    public ServerConfig() {
        config = new YamlFile("mcnet.yml");
        this.serverInfos = new ArrayList<>();

        try {
            if (!config.exists()) {
                System.out.println("New file has been created: " + config.getFilePath() + "\n");
                config.createNewFile(true);
            } else {
                System.out.println(config.getFilePath() + " already exists, loading configurations...\n");
            }

            config.load();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        this.port = config.getInt("port");
        this.description = config.getString("description");

        ConfigurationSection section = config.getConfigurationSection("servers");

        for (String serverId : section.getKeys(false)) {
            ConfigurationSection serverSection = section.getConfigurationSection(serverId);

            String host = serverSection.getString("host");
            String[] split = host.split(":");
            String address = split[0];
            int port = Integer.parseInt(split[1]);

            ServerInfo info = new ServerInfo(serverId, address, port);

            serverInfos.add(info);
        }
    }

    public ServerInfo findServer(String serverName) {
        for (ServerInfo info : serverInfos)
            if (info.getName().equalsIgnoreCase(serverName))
                return info;

        return null;
    }
}
