package discraft.discraft;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConstantsTest {
    @Test
    public void constructorTest() {
        Constants constants = new Constants();
        assertNotNull(constants);
    }

    @Test
    public void messagesLoginPromptTest() {
        String testMessage = String.format(Constants.MESSAGES.LOGIN_PROMPT.toString(), "botNick");
        String correctMessage = "Please dont send \"/login\" command to bot botNick and enter the code here";
        assertEquals(testMessage, correctMessage);
    }

    @Test
    public void messagesRegPromptTest() {
        String testMessage = String.format(Constants.MESSAGES.REG_PROMPT.toString(), "botNick", "codeStr");
        String correctMessage = "Go to bot botNick and send \"/reg codeStr\" command to register";
        assertEquals(testMessage, correctMessage);
    }

    @Test
    public void messagesCorrectCodeTest() {
        String testMessage = String.format(Constants.MESSAGES.CORRECT_CODE.toString());
        String correctMessage = "Correct code!";
        assertEquals(testMessage, correctMessage);
    }

    @Test
    public void messagesIncorrectCodeTest() {
        String testMessage = String.format(Constants.MESSAGES.INCORRECT_CODE.toString());
        String correctMessage = "Incorrect code!";
        assertEquals(testMessage, correctMessage);
    }
}
