package discraft.discraft;

import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class PluginConfigTest {

    static String configPathName = "discraft_config.json";
    static String configPathNameBad = "bad_discraft_config.json";

    private static void createConfig(String configPathName, String content) {
        Path configPath = Path.of(configPathName);
        try {
            Files.createFile(configPath);
            Files.writeString(configPath, content);
        } catch (IOException ex) {
            System.err.printf("FAILED TO CREATE FILE: %s \n", ex);
        }
    }

    @AfterClass
    public static void clean() {
        Path.of(configPathName).toFile().delete();
        Path.of(configPathNameBad).toFile().delete();
    }

    @Test
    public void constructorTest() {
        createConfig(configPathName, "{\"bot_nick\":\"bot_nick\", \"bot_token\": \"bot_token\"}");
        PluginConfig pluginConfig = new PluginConfig(configPathName);
        assertEquals(pluginConfig.botNick, "bot_nick");
        assertEquals(pluginConfig.botToken, "bot_token");
    }


    @Test
    public void constructorBadPathsTest() {
        PluginConfig pluginConfig = new PluginConfig(configPathNameBad);
        assertNull(pluginConfig.botNick);
        assertNull(pluginConfig.botToken);
    }

    @Test
    public void constructorBadTokensTest() {
        createConfig(configPathNameBad,"{\"bad_bot_nick\":\"bot_nick\", \"bad_bot_token\": \"bot_token\"}");
        PluginConfig pluginConfig = new PluginConfig(configPathNameBad);
        assertNull(pluginConfig.botNick);
        assertNull(pluginConfig.botToken);
    }
}
