package store.tool;

import store.domain.io.Row;
import store.exception.DomainNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CSVReader {
    public static final String DELIMITER = ",";
    private final String filePath;

    public CSVReader(String resourcePath) {
        this.filePath = resourcePath;
    }

    public List<Row> readRows() {
        try (InputStream inputStream = getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            return readLines(bufferedReader);

        } catch (IOException ie) {
            throw new DomainNotFoundException(filePath);
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

    private InputStream getInputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new DomainNotFoundException(filePath);
        }
        return inputStream;
    }
}
