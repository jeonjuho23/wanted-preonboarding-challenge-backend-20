package wanted.jun.pre_subject.trade;

import java.util.List;

public record FetchReservedTradeHistoryForSellingResDTO
        (int page, boolean hasNextPage, boolean hasPreviousPage, int numberOfElements,
         boolean hasContents, List<ReservedTradeHistoryForSellingDTO> content) {
}
