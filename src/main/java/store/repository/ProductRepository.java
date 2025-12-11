package store.repository;

import store.constants.FileConstant;
import store.domain.Product;
import store.exception.ErrorMessage;
import store.exception.ServiceException;
import store.parser.StockCSVParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductRepository {
    private final List<Product> products;

    public ProductRepository() {
        this.products = readProducts();
    }

    private List<Product> readProducts() {
        List<Product> products = new ArrayList<>();

        try (InputStream inputStream = getClass().getResourceAsStream(FileConstant.PRODUCT_FILE_NAME);
             InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            // 첫 라인은 컬럼이므로 넘김
            bufferedReader.readLine();

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                Product product = StockCSVParser.parseProduct(line);
                products.add(product);
            }

            return products;
        } catch (IOException e) {
            throw new ServiceException(ErrorMessage.FILE_READ_FAIL);
        }
    }

    public List<Product> findAll() {
        return List.copyOf(products);
    }
}
