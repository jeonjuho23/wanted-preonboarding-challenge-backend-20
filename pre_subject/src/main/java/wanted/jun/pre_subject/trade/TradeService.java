package wanted.jun.pre_subject.trade;

public interface TradeService {
    Trade reserveProduct(ReserveTradeReqDTO request);

    Trade approveTrade(ApproveTradeReqDTO request);
}
