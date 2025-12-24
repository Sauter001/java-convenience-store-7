package store.domain.io;

import java.util.Iterator;
import java.util.List;

public class Row {
    private final List<String> columns;

    public Row(List<String> columns) {
        this.columns = columns;
    }

    public Iterator<String> getColumns() {
        return columns.iterator();
    }
}
