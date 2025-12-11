package store.parser;

public interface DomainParser<T> {
    T parse(String input);
}
