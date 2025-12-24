package store.parser;

@FunctionalInterface
public interface Parser<T> {
    T parse(String input);
}
