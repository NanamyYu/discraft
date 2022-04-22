package discraft.discraft;

public final class Constants {
    public final static String BASE_DIR_PATHNAME = "./plugins/discraft/";
    public final static String LINKED_PATHNAME = "./plugins/discraft/linked.json";
    public final static String REG_CODES_PATHNAME = "./plugins/discraft/reg_codes.json";
    public final static String LOGIN_CODES_PATHNAME = "./plugins/discraft/login_codes.json";
    public final static String CONFIG_PATHNAME = "./plugins/discraft_config.json";

    public enum MESSAGES {
        LOGIN_PROMPT {
            public String toString() {
                return "Please send \"/login\" command to bot %s and enter the code here";
            }
        },

        REG_PROMPT {
            public String toString() {
                return "Go to bot %s and write code %s to register";
            }
        },

        CORRECT_CODE {
            public String toString() {
                return "Correct code!";
            }
        },

        INCORRECT_CODE {
            public String toString() {
                return "Incorrect code!";
            }
        }
    }
}
