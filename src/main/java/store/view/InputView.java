package store.view;

import store.exception.ErrorMessage;
import store.exception.ServiceException;

public class InputView {
    private boolean checkYesOrNo(String input) {
        String strippedInput = input.strip();

        if (isYes(strippedInput)) {
            return true;
        }

        if  (isNo(strippedInput)) {
            return false;
        }

        throw new ServiceException(ErrorMessage.INVALID_INPUT_FORMAT);
    }

    private static boolean isNo(String strippedInput) {
        return strippedInput.equalsIgnoreCase("no") || strippedInput.equalsIgnoreCase("n");
    }

    private static boolean isYes(String strippedInput) {
        return strippedInput.equalsIgnoreCase("yes") || strippedInput.equalsIgnoreCase("y");
    }
}
