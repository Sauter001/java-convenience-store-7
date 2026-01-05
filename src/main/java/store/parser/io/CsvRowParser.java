package store.parser.io;

import store.domain.io.Row;

import java.util.List;
import java.util.stream.Stream;

public class CsvRowParser implements Parser<Row> {
    private static final String DELIMITER = ",";

    @Override
    public Row parse(String input) {
        List<String> tokens = Stream.of(input.split(DELIMITER))
                .map(String::strip)
                .toList();
        return new Row(tokens);
    }
}
