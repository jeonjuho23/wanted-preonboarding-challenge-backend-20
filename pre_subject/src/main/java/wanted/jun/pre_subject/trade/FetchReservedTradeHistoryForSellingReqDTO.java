package wanted.jun.pre_subject.trade;

import org.springframework.data.domain.Pageable;

public record FetchReservedTradeHistoryForSellingReqDTO(Long memberId, Pageable pageable) {
}
