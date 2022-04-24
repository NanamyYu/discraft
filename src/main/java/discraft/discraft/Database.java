package discraft.discraft;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Database {
    private Path baseDirPath;
    private Path linkedUsersPath;
    private Path regCodesPath;
    private Path loginCodesPath;

    Logger logger;

    public Database(String baseDirPathName, String linkedUsersPathName,
                    String regCodesPathName, String loginCodesPathName) {
        this.setLogger();
        this.setBaseDir(baseDirPathName);
        this.setLinkedUsersFile(linkedUsersPathName);
        this.setRegCodesFile(regCodesPathName);
        this.setLoginCodesFile(loginCodesPathName);
    }

    private void setLogger() {
        if (Bukkit.getServer() != null) {
            this.logger = Bukkit.getLogger();
        } else {
            this.logger = Logger.getLogger(Database.class.getName());
        }
    }

    private void setBaseDir(String baseDirPathName) {
        Path baseDirPath = Path.of(baseDirPathName);
        if (this.createDirectory(baseDirPath)) {
            this.baseDirPath = baseDirPath;
        } else {
            this.baseDirPath = Path.of("");
        }
    }

    private void setLinkedUsersFile(String linkedUsersPathName) {
        Path linkedUsersPath = Path.of(linkedUsersPathName);
        if (this.createFile(linkedUsersPath)) {
            this.linkedUsersPath = linkedUsersPath;
        } else {
            this.linkedUsersPath = Path.of("");
        }
    }

    private void setRegCodesFile(String regCodesPathName) {
        Path regCodesPath = Path.of(regCodesPathName);
        if (this.createFile(regCodesPath)) {
            this.regCodesPath = regCodesPath;
        } else {
            this.regCodesPath = Path.of("");
        }
    }

    private void setLoginCodesFile(String loginCodesPathName) {
        Path loginCodesPath = Path.of(loginCodesPathName);
        if (this.createFile(loginCodesPath)) {
            this.loginCodesPath = loginCodesPath;
        } else {
            this.loginCodesPath = Path.of("");
        }
    }

    public HashMap<String, String> getLinkedUsersHashMap() {
        return this.readJsonFile(this.linkedUsersPath);
    }

    public HashMap<String, String> getRegCodesHashMap() {
        return this.readJsonFile(this.regCodesPath);
    }

    public HashMap<String, String> getLoginCodesHashMap() {
        return this.readJsonFile(this.loginCodesPath);
    }

    public void pullLinkedUsers(String minecraftName, String discordName) {
        HashMap<String, String> linkedUsersHashMap = this.pullInHashMap(this.getLinkedUsersHashMap(), minecraftName, discordName);
        if (linkedUsersHashMap != null) {
            this.writeJsonFile(this.linkedUsersPath, linkedUsersHashMap);
        }
    }

    public void pullRegCodes(String name, String codeStr) {
        HashMap<String, String> regCodesHashMap = this.pullInHashMap(this.getRegCodesHashMap(), name, codeStr);
        if (regCodesHashMap != null) {
            this.writeJsonFile(this.regCodesPath, regCodesHashMap);
        }
    }

    public void pullLoginCodes(String name, String codeStr) {
        HashMap<String, String> loginCodesHashMap = this.pullInHashMap(this.getLoginCodesHashMap(), name, codeStr);
        if (loginCodesHashMap != null) {
            this.writeJsonFile(this.loginCodesPath, loginCodesHashMap);
        }
    }

    private boolean createDirectory(Path dirPath) {
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectory(dirPath);
            } catch (IOException ex) {
                this.logger.log(Level.SEVERE, String.format("FAILED TO CREATE DIRECTORY: %s \n", ex));
                return false;
            }
        }
        return true;
    }

    private boolean createFile(Path filePath) {
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
                Files.writeString(filePath, "{}");
            } catch (IOException ex) {
                this.logger.log(Level.SEVERE, String.format("FAILED TO CREATE FILE: %s \n", ex));
                return false;
            }
        }
        return true;
    }

    private HashMap<String, String> readJsonFile(Path filePath) {
        String resultJson = "{}";
        try {
            resultJson = Files.readString(filePath);
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, String.format("FAILED TO READ FILE: %s \n", ex));
        }
        return Database.getHashMapFromJson(resultJson);
    }

    private void writeJsonFile(Path filePath, HashMap<String, String> content) {
        try {
            Files.writeString(filePath, Database.getJsonFromHashMap(content));
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, String.format("FAILED TO WRITE IN FILE: %s \n", ex));
        }
    }

    private static HashMap<String, String> getHashMapFromJson(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(jsonString, type);
    }

    private static String getJsonFromHashMap(HashMap<String, String> jsonHashMap) {
        Gson gson = new Gson();
        return gson.toJson(jsonHashMap);
    }

    private HashMap<String, String> pullInHashMap(HashMap<String, String> workingHashMap, String key, String value) {
        if (Objects.equals(key, "") || Objects.equals(value, "")) {
            this.logger.log(Level.SEVERE, "FAILED TO ADD: empty key or value");
            return null;
        }
        workingHashMap.put(key, value);
        return workingHashMap;
    }
}
