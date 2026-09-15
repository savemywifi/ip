package bern.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;

import org.junit.jupiter.api.Test;

import bern.ui.Dialog;

/** Verifies that invalid command input produces a readable response without changing tasks. */
public class ControllerTest {
    @Test
    public void getResponse_blankInput_returnsInvalidKeywordMessage() {
        Controller controller = new Controller();
        String expectedMessage = Dialog.getInstance().printKeywordInvalidError().toString();

        for (String input : List.of("", " ", "   ", "\t", "\n", " \t\r\n ")) {
            TextResponse response = assertInstanceOf(TextResponse.class, controller.getResponse(input));
            assertEquals(expectedMessage, response.toString());
        }
    }
}
