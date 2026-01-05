package store.parser.row;

import store.domain.io.Rows;

public interface RowsParser<T> {
    T parse(Rows rows);
}
