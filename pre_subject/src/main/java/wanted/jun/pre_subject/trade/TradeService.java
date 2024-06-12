package wanted.jun.pre_subject.trade;

import java.util.Optional;

public interface TradeService {
    Trade reserveProduct(ReserveTradeReqDTO request);

    Trade approveTrade(ApproveTradeReqDTO request);

    Optional<FetchPurchasedTradeHistoryResDTO> fetchPurchasedTradeHistory(FetchPurchasedTradeHistoryReqDTO request);

    Optional<FetchReservedTradeHistoryForBuyingResDTO> fetchReservedTradeHistoryForBuying(FetchReservedTradeHistoryForBuyingReqDTO request);

    Optional<FetchReservedTradeHistoryForSellingResDTO> fetchReservedTradeHistoryForSelling(FetchReservedTradeHistoryForSellingReqDTO request);
}
