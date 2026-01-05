package store.parser.io;

public interface Parser<T> {
    T parse(String input);
}
