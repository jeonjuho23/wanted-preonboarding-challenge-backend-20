package wanted.jun.pre_subject.product;

import java.util.List;

public record TradeHistoryListDTO(int page, boolean hasNextPage, boolean hasPreviousPage,
                           int numberOfElements, boolean hasContents,
                           List<TradeHistoryDTO> content) {
}
