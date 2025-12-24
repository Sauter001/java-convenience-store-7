package store.repository;

import store.constant.FileConstant;
import store.domain.io.Row;
import store.domain.product.ProductData;
import store.parser.ProductRowsParser;
import store.tool.CSVReader;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private final List<ProductData> products;

    public ProductRepository() {
        this.products = loadProducts();
    }

    private List<ProductData> loadProducts() {
        CSVReader csvReader = new CSVReader(FileConstant.PRODUCT_PATH);
        List<Row> rows = csvReader.readRows();
        ProductRowsParser parser = new ProductRowsParser();
        return parser.parse(rows);
    }

    public List<ProductData> findAll() {
        return new ArrayList<>(products);
    }
}
