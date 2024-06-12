package wanted.jun.pre_subject.trade;

import java.util.List;

public record FetchPurchasedTradeHistoryResDTO
        (int page, boolean hasNextPage, boolean hasPreviousPage, int numberOfElements,
         boolean hasContents, List<PurchasedTradeHistoryDTO> content) {
}
