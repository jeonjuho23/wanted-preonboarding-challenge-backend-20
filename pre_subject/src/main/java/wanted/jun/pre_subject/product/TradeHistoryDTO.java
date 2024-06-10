package wanted.jun.pre_subject.product;

import java.time.LocalDateTime;

public record TradeHistoryDTO(Long tradeId, String productName, int productPrice,
                       String tradeState, LocalDateTime tradeTime) {
}
