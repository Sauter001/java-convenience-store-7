package store.repository;

import store.constants.FileConstant;
import store.domain.Product;
import store.exception.ErrorMessage;
import store.exception.ServiceException;
import store.parser.ProductCSVParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductRepository {
    private final List<Product> products;

    public ProductRepository() {
        this.products = readProducts();
    }

    private static List<Product> convertToProduct(BufferedReader bufferedReader, List<Product> products) throws IOException {
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            Product product = ProductCSVParser.parse(line);
            products.add(product);
        }

        return products;
    }

    private void skipColumns(BufferedReader bufferedReader) throws IOException {
        bufferedReader.readLine();
    }

    private List<Product> readProducts() {
        List<Product> products = new ArrayList<>();

        try (InputStream inputStream = getClass().getResourceAsStream(FileConstant.PRODUCT_FILE_NAME);
             InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            skipColumns(bufferedReader);

            return convertToProduct(bufferedReader, products);
        } catch (IOException e) {
            throw new ServiceException(ErrorMessage.FILE_READ_FAIL);
        }
    }

    public List<Product> findAll() {
        return List.copyOf(products);
    }

    public List<Product> findByName(String name) {
        return this.products.stream()
                .filter(p -> p.name().equals(name))
                .toList();
    }

    public boolean existProductOfName(String name) {
        return this.products.stream().anyMatch(p -> p.name().equals(name));
    }

    public int getStockOf(String productName) {
        List<Product> productsOfName = findByName(productName);
        return productsOfName.stream()
                .mapToInt(Product::stock)
                .sum();
    }
}
