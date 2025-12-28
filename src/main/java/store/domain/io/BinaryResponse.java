package store.domain.io;

import store.exception.InvalidInputException;

import java.util.Arrays;
import java.util.List;

public enum BinaryResponse {
    YES(List.of("Y", "YES")), NO(List.of("N", "NO"));

    private List<String> commands;
    BinaryResponse(List<String> commands) {
        this.commands = commands;
    }

    public static BinaryResponse from(String command) {
        return Arrays.stream(values())
                .filter(r -> r.commands.contains(command.strip().toUpperCase()))
                .findFirst()
                .orElseThrow(InvalidInputException::new);
    }
}
