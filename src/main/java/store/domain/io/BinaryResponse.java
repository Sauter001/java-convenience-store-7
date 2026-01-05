package store.domain.io;

import store.error.InvalidInputException;

import java.util.Set;

public enum BinaryResponse {
    YES(Set.of("Y", "YES"), true), NO(Set.of("N", "NO"), false);

    private final Set<String> keySet;
    private final boolean bool;

    BinaryResponse(Set<String> keySet, boolean bool) {
        this.keySet = keySet;
        this.bool = bool;
    }

    public static BinaryResponse getResponseFrom(String input) {
        String strippedInput = input.strip().toUpperCase();
        for (BinaryResponse response : values()) {
            if (response.keySet.contains(strippedInput)) {
                return response;
            }
        }
        throw new InvalidInputException();
    }

    public boolean isYes() {
        return this == BinaryResponse.YES;
    }
}
