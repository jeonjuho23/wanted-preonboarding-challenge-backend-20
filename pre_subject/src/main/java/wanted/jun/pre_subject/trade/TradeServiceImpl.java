package wanted.jun.pre_subject.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.jun.pre_subject.member.Member;
import wanted.jun.pre_subject.member.MemberRepository;
import wanted.jun.pre_subject.product.Product;
import wanted.jun.pre_subject.product.ProductRepository;
import wanted.jun.pre_subject.product.State;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Trade reserveProduct(ReserveTradeReqDTO request) {

        Member buyer = memberRepository.findById(request.buyerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품입니다."));

        if (Objects.equals(buyer.getId(), product.getSeller().getId()))
            throw new IllegalArgumentException("판매자는 본인의 제품을 예약할 수 없습니다.");

        Trade newTrade = Trade.startTrade(buyer, product);
        return tradeRepository.save(newTrade);
    }

    @Override
    @Transactional
    public Trade approveTrade(ApproveTradeReqDTO request) {

        Member seller = memberRepository.findById(request.sellerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Trade trade = tradeRepository.findById(request.tradeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 거래입니다."));

        if (!Objects.equals(trade.getProduct().getSeller().getId(), seller.getId()))
            throw new IllegalArgumentException("판매자만 판매를 승인할 수 있습니다.");

        if(!Objects.equals(trade.getProduct().getProductState(),State.RESERVED.value())) {
            throw new IllegalArgumentException("예약 상태의 제품만 판매를 승인할 수 있습니다.");
        }

        return trade.approveTrade();
    }

    @Override
    public Optional<FetchPurchasedTradeHistoryResDTO> fetchPurchasedTradeHistory(FetchPurchasedTradeHistoryReqDTO request) {

        if (request.memberId() == null) throw new IllegalArgumentException("회원이어야 거래 내역을 조회할 수 있습니다.");

        Member buyer = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Page<Trade> tradePage = tradeRepository.findAllByBuyerAndProduct_ProductState(buyer, State.COMPLETE.value(), request.pageable());

        if (tradePage.isEmpty()) return Optional.empty();

        List<PurchasedTradeHistoryDTO> content = tradePage.getContent().stream()
                .map(trade -> new PurchasedTradeHistoryDTO(trade.getProduct().getProductName(),
                        trade.getProduct().getProductPrice(), trade.getProduct().getSeller().getUserId(),
                        trade.getProduct().getStateUpdateTime(), trade.getId())).toList();

        FetchPurchasedTradeHistoryResDTO response = new FetchPurchasedTradeHistoryResDTO(tradePage.getPageable().getPageNumber(),
                tradePage.hasNext(), tradePage.hasPrevious(), tradePage.getNumberOfElements(),
                tradePage.hasContent(), content);

        return Optional.of(response);
    }
}
