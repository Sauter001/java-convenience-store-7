package store.repository;

import store.constant.IOConstant;
import store.domain.io.Row;
import store.domain.io.Rows;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.error.ProductNotExistsException;
import store.error.PromotionNotExistsException;
import store.error.StoreException;
import store.parser.io.CsvRowParser;
import store.parser.row.ProductRowsParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ProductRepository {
    private final List<Product> products;

    public ProductRepository() {
        this.products = readProducts();
    }

    private List<Product> readProducts() {
        try (InputStream is = getClass().getResourceAsStream(IOConstant.PRODUCT_PATH);
             InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(is));
             BufferedReader reader = new BufferedReader(isr)) {
            return readProductsWith(reader);
        } catch (IOException e) {
            throw new StoreException("프로모션 파일 읽기 실패");
        }
    }

    private List<Product> readProductsWith(BufferedReader reader) throws IOException {
        List<Row> rowsContent = new ArrayList<>();
        CsvRowParser csvRowParser = new CsvRowParser();
        ProductRowsParser productRowsParser = new ProductRowsParser();
        while (reader.ready()) {
            String line = reader.readLine();
            rowsContent.add(csvRowParser.parse(line));
        }
        return productRowsParser.parse(new Rows(rowsContent));
    }

    public List<Product> findAll() {
        return this.products;
    }

    public Product findByName(String productName) {
        return this.products.stream()
                .filter(p -> p.nameEquals(productName))
                .findFirst()
                .orElseThrow(ProductNotExistsException::new);
    }
}
