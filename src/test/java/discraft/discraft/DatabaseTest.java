package discraft.discraft;

import static org.junit.Assert.*;

import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;


public class DatabaseTest {
    static String baseDirPath = "./test_database/";
    static String linkedUsersPath = "./test_database/linked.json";
    static String regCodesPath = "./test_database/reg_codes.json";
    static String loginCodesPath = "./test_database/login_codes.json";

    static String badDirPath = "./bad/path";
    static String badFilePath = "./bad/path/file.json";


    @AfterClass
    public static void clean() {
        deleteFiles(Path.of(baseDirPath).toFile());
    }

    private static void deleteFiles(File dirPath) {
        File[] allFiles = dirPath.listFiles();
        if (allFiles != null) {
            for (File file : allFiles) {
                deleteFiles(file);
            }
        }
        dirPath.delete();
    }


    @Test
    public void constructorTest() {
        createDatabase();
        createDatabase();
    }

    private void createDatabase() {
        Database database = new Database(baseDirPath, linkedUsersPath, regCodesPath, loginCodesPath);
        assertTrue(Files.exists(Path.of(baseDirPath)));
        assertTrue(Files.exists(Path.of(linkedUsersPath)));
        assertTrue(Files.exists(Path.of(regCodesPath)));
        assertTrue(Files.exists(Path.of(loginCodesPath)));
    }

    @Test
    public void constructorBadPathsTest() {
        Database database = new Database(badDirPath, badFilePath, badFilePath, badFilePath);
        assertFalse(Files.exists(Path.of(badDirPath)));
        assertFalse(Files.exists(Path.of(badFilePath)));
    }

    @Test
    public void hashMaps() throws IOException {
        Database database = new Database(baseDirPath, linkedUsersPath, regCodesPath, loginCodesPath);
        HashMap<String, String> correct = new HashMap<>();
        equalHashMaps(database, correct);

        correct.put("key", "value");
        pullHashMaps(database, "key", "value");
        equalHashMaps(database, correct);

        correct = new HashMap<>();
        correct.put("key", "new_value");
        pullHashMaps(database, "key", "new_value");
        equalHashMaps(database, correct);

        pullHashMaps(database, "", "");
        equalHashMaps(database, correct);
        pullHashMaps(database, "key", "");
        equalHashMaps(database, correct);
        pullHashMaps(database, "", "value");
        equalHashMaps(database, correct);

        correct.put("new_key", "new_value");
        pullHashMaps(database, "new_key", "new_value");
        equalHashMaps(database, correct);

        database = new Database(badDirPath, badFilePath, badFilePath, badFilePath);
        correct = new HashMap<>();
        equalHashMaps(database, correct);
        pullHashMaps(database, "key", "value");
        equalHashMaps(database, correct);
    }

    private void equalHashMaps(Database database, HashMap<String, String> correct) {
        assertEquals(database.getLinkedUsersHashMap(), correct);
        assertEquals(database.getRegCodesHashMap(), correct);
        assertEquals(database.getLoginCodesHashMap(), correct);
    }

    private void pullHashMaps(Database database, String key, String value) {
        database.pullLinkedUsers(key, value);
        database.pullRegCodes(key, value);
        database.pullLoginCodes(key, value);
    }

}
