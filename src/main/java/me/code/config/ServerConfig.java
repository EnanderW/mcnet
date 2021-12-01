package me.code.config;

import lombok.Getter;
import lombok.Setter;
import org.simpleyaml.configuration.file.YamlFile;

@Getter
@Setter
public class ServerConfig {

    private final YamlFile config;

    private int port;
    private String description;

    public ServerConfig() {
        config = new YamlFile("mcnet.yml");

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
    }

}
