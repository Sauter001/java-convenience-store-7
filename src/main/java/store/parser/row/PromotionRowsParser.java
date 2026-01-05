package store.parser.row;

import store.domain.io.Row;
import store.domain.io.Rows;
import store.domain.promotion.BuyGet;
import store.domain.promotion.Period;
import store.domain.promotion.Promotion;
import store.error.InvalidInputException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class PromotionRowsParser implements RowsParser<Set<Promotion>> {
    @Override
    public Set<Promotion> parse(Rows rows) {
        Set<Promotion> promotionSet = new HashSet<>();
        for (Row row : rows) {
            promotionSet.add(createPromotion(row));
        }
        return promotionSet;
    }

    private Promotion createPromotion(Row row) {
        try {
            return new Promotion(row.contentOf(Column.NAME.order),
                    createBuyGet(row),
                    createPeriod(row));
        } catch (NumberFormatException | DateTimeException e) {
            throw new InvalidInputException();
        }
    }

    private BuyGet createBuyGet(Row row) {
        int buy = Integer.parseInt(row.contentOf(Column.BUY.order));
        int get = Integer.parseInt(row.contentOf(Column.GET.order));
        return new BuyGet(buy, get);
    }

    private Period createPeriod(Row row) {
        LocalDate start = LocalDate.parse(row.contentOf(Column.START_DATE.order));
        LocalDate end = LocalDate.parse(row.contentOf(Column.END_DATE.order));
        return new Period(start, end);
    }

    private enum Column {
        NAME(0), BUY(1), GET(2), START_DATE(3), END_DATE(4);
        final int order;

        Column(int order) {
            this.order = order;
        }
    }
}
