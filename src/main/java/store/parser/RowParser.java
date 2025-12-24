package store.parser;

import store.domain.io.Row;

import java.util.List;

@FunctionalInterface
public interface RowParser<T> {
    T parse(List<Row> rows);
}
