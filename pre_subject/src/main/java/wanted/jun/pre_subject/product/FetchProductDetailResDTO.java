package wanted.jun.pre_subject.product;

import java.time.LocalDateTime;
import java.util.Optional;

public record FetchProductDetailResDTO(Long productId, String productName, Integer productPrice,
                                       String sellerId, String productState, LocalDateTime registTime,
                                       Optional<TradeHistoryListDTO> tradeHistory) {
}
