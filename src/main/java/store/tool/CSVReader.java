package store.tool;

import store.domain.io.Row;
import store.exception.FileNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CSVReader {
    public static final String DELIMITER = ",";
    private final String filePath;

    public CSVReader(String resourcePath) {
        this.filePath = resourcePath;
    }

    public List<Row> readRows() {
        try (BufferedReader bufferedReader = getReader()) {
            return readLines(bufferedReader);
        } catch (IOException ie) {
            throw new FileNotFoundException(filePath);
        }
    }

    private List<Row> readLines(BufferedReader bufferedReader) throws IOException {
        List<Row> rows = new ArrayList<>();
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            List<String> columns = Arrays.stream(line.split(DELIMITER))
                    .map(String::strip)
                    .toList();
            rows.add(new Row(columns));
        }

        return rows.stream().skip(1).toList(); // header 넘기기
    }

    private BufferedReader getReader() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);) {
            InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
            return new BufferedReader(inputStreamReader);
        }
    }
}
