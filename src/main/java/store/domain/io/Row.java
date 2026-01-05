package store.domain.io;

import java.util.Iterator;
import java.util.List;

public class Row implements Iterable<String> {
    private final List<String> columns;

    public Row(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public Iterator<String> iterator() {
        return this.columns.iterator();
    }

    public String contentOf(int index) {
        return columns.get(index);
    }
}
