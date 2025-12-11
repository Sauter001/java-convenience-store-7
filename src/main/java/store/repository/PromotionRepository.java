package store.repository;

import store.constants.FileConstant;
import store.domain.Promotion;
import store.domain.dao.PromotionDAO;
import store.exception.ErrorMessage;
import store.exception.ServiceException;
import store.parser.PromotionCSVParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PromotionRepository {
    private final List<PromotionDAO> promotions;

    public PromotionRepository(List<PromotionDAO> promotions) {
        this.promotions = readPromotionDAOs();
    }

    public PromotionDAO findByPromotion(Promotion promotion) {
        return this.promotions.stream().filter(p -> p.promotion().equals(promotion))
                .findFirst()
                .orElse(null);
    }

    private List<PromotionDAO> readPromotionDAOs() {
        List<PromotionDAO> promotionDAOS = new ArrayList<>();

        try (InputStream inputStream = getClass().getResourceAsStream(FileConstant.PRODUCT_FILE_NAME);
             InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream));
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            skipColumns(bufferedReader);

            return convertToProduct(bufferedReader, promotionDAOS);
        } catch (IOException e) {
            throw new ServiceException(ErrorMessage.FILE_READ_FAIL);
        }
    }

    private List<PromotionDAO> convertToProduct(BufferedReader bufferedReader, List<PromotionDAO> promotionDAOS) throws IOException {
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            PromotionDAO promotionDAO = PromotionCSVParser.parse(line);
            promotionDAOS.add(promotionDAO);
        }

        return promotionDAOS;
    }

    private void skipColumns(BufferedReader bufferedReader) throws IOException {
        bufferedReader.readLine();
    }


}
