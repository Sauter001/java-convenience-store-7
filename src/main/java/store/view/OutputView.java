package store.view;

import store.exception.ServiceException;

public class OutputView {
    public void printError(ServiceException e) {
        System.out.println(e.getMessage());
    }
}
