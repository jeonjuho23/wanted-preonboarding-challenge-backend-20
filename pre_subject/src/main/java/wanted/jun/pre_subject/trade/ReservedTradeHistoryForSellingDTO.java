package wanted.jun.pre_subject.trade;

import java.time.LocalDateTime;

public record ReservedTradeHistoryForSellingDTO
        (String productName, int productPrice, String buyerUserId,
         LocalDateTime stateUpdateTime, Long tradeId) {
}
