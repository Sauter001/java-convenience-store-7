package store.domain.promotion;

import store.exception.ServiceException;

import java.time.LocalDate;

public class PromotionPeriod {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public PromotionPeriod(LocalDate startDate, LocalDate endDate) {
        validatePeriod(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new ServiceException("종료일은 시작일 뒤여야 합니다.");
        }
    }

    public boolean isExpired() {
        LocalDate today = LocalDate.now();
        return today.isBefore(startDate) || today.isAfter(endDate);
    }

    boolean isExpiredAt(LocalDate date) {
        return date.isBefore(startDate) || date.isAfter(endDate);
    }
}
