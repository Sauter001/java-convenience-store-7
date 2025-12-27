package store.domain.promotion;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PromotionPeriodTest {

      @Test
      void should_be_expired_when_after_end_date() {
          PromotionPeriod period = new PromotionPeriod(
              LocalDate.of(2025, 1, 1),
              LocalDate.of(2025, 12, 31)
          );

          LocalDate endDateAfter = LocalDate.of(2026, 1, 1);
          assertThat(period.isExpiredAt(endDateAfter)).isTrue();
      }

      @Test
      void should_be_active_on_end_date() {
          PromotionPeriod period = new PromotionPeriod(
              LocalDate.of(2025, 1, 1),
              LocalDate.of(2025, 12, 31)
          );

          LocalDate endDate = LocalDate.of(2025, 12, 31);
          assertThat(period.isExpiredAt(endDate)).isFalse();
      }

      @Test
      void should_be_expired_before_start_date() {
          PromotionPeriod period = new PromotionPeriod(
              LocalDate.of(2025, 1, 1),
              LocalDate.of(2025, 12, 31)
          );

          LocalDate startDateBefore = LocalDate.of(2024, 12, 31);
          assertThat(period.isExpiredAt(startDateBefore)).isTrue();
      }

      @Test
      void integration_test_with_current_date() {
          LocalDate tod = LocalDate.now();

          // past promotion
          PromotionPeriod past = new PromotionPeriod(
              tod.minusMonths(2),
              tod.minusDays(1)
          );
          assertThat(past.isExpired()).isTrue();

          // ongoing promotion
          PromotionPeriod inPromotion = new PromotionPeriod(
              tod.minusDays(10),
              tod.plusDays(10)
          );
          assertThat(inPromotion.isExpired()).isFalse();
      }
  }