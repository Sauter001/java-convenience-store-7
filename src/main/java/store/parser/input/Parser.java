package store.parser.input;

@FunctionalInterface
public interface Parser<T> {
    T parse(String input);
}
