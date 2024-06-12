package wanted.jun.pre_subject.trade;

import org.springframework.data.domain.Pageable;

public record FetchReservedTradeHistoryForBuyingReqDTO(Long memberId, Pageable pageable) {
}
