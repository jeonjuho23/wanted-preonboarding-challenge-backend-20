package wanted.jun.pre_subject.trade;

import java.time.LocalDateTime;

public record ReservedTradeHistoryForBuyingDTO
        (String productName, int productPrice, String sellerUserId,
         LocalDateTime stateUpdateTime, Long tradeId) {
}
