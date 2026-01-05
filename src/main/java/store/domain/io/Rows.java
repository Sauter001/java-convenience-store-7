package store.domain.io;

import java.util.Iterator;
import java.util.List;

public class Rows implements Iterable<Row>{
    private final List<Row> rows;

    public Rows(List<Row> rows) {
        this(rows, true);
    }

    public Rows(List<Row> rows, boolean skipFirst) {
        if (skipFirst) {
            this.rows = rows.stream().skip(1).toList();
            return;
        }
        this.rows = rows;
    }

    @Override
    public Iterator<Row> iterator() {
        return this.rows.iterator();
    }
}
