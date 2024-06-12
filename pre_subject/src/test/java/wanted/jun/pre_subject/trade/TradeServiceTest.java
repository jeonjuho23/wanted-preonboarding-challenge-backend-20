package wanted.jun.pre_subject.trade;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import wanted.jun.pre_subject.member.Member;
import wanted.jun.pre_subject.member.MemberRepository;
import wanted.jun.pre_subject.product.Product;
import wanted.jun.pre_subject.product.ProductRepository;
import wanted.jun.pre_subject.product.SortBy;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TradeServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TradeService tradeService;

    private Member seller, buyer;
    private Product product;
    @BeforeEach
    void setUp() throws Exception {
        seller = memberRepository.save(Member.signUp("seller", "1234", "seller"));
        buyer = memberRepository.save(Member.signUp("buyer", "1234", "buyer"));
        product = productRepository.save(Product.registProduct(seller, "productName", 1000));
    }


    @Test
    public void reserveProduct() throws Exception {
        //given
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        ReserveTradeReqDTO request = new ReserveTradeReqDTO(productId, buyerId);

        LocalDateTime stateUpdateTime = product.getStateUpdateTime();

        //when
        Trade reservedTrade = tradeService.reserveProduct(request);

        //then
        assertThat(reservedTrade.getProduct().getId()).isEqualTo(productId);
        assertThat(reservedTrade.getBuyer().getId()).isEqualTo(buyerId);
        assertThat(reservedTrade.getProduct().getStateUpdateTime()).isNotEqualTo(stateUpdateTime);
    }

    
    @Test
    public void approveTrade() throws Exception {
        //given
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        ReserveTradeReqDTO reserveTradeReqDTO = new ReserveTradeReqDTO(productId, buyerId);
        Trade reservedTrade = tradeService.reserveProduct(reserveTradeReqDTO);

        LocalDateTime stateUpdateTime = product.getStateUpdateTime();

        Long sellerId = seller.getId();
        Long tradeId = reservedTrade.getId();
        ApproveTradeReqDTO request = new ApproveTradeReqDTO(tradeId, sellerId);

        //when
        Trade approvedTrade = tradeService.approveTrade(request);
        
        //then
        assertThat(approvedTrade.getProduct().getStateUpdateTime()).isNotEqualTo(stateUpdateTime);
    }


    @Test
    public void fetchPurchasedTradeHistory() throws Exception {
        //given
        // 예약
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        ReserveTradeReqDTO reserveTradeReqDTO = new ReserveTradeReqDTO(productId, buyerId);
        Trade reservedTrade = tradeService.reserveProduct(reserveTradeReqDTO);

        // 판매 승인
        Long sellerId = seller.getId();
        Long tradeId = reservedTrade.getId();
        ApproveTradeReqDTO approveTradeReqDTO = new ApproveTradeReqDTO(tradeId, sellerId);
        Trade approvedTrade = tradeService.approveTrade(approveTradeReqDTO);

        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);
        Long memberId = buyerId;
        FetchPurchasedTradeHistoryReqDTO request = new FetchPurchasedTradeHistoryReqDTO(memberId, pageable);

        //when
        Optional<FetchPurchasedTradeHistoryResDTO> response =  tradeService.fetchPurchasedTradeHistory(request);

        //then
        assertThat(response.isPresent()).isEqualTo(true);
        assertThat(response.get().content().get(0).tradeId()).isEqualTo(tradeId);
    }

    @Test
    public void fetchPurchasedTradeHistoryEmptyWithReservedTrade() throws Exception {
        //given
        // 예약
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        ReserveTradeReqDTO reserveTradeReqDTO = new ReserveTradeReqDTO(productId, buyerId);
        Trade reservedTrade = tradeService.reserveProduct(reserveTradeReqDTO);

        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);
        Long memberId = buyer.getId();
        FetchPurchasedTradeHistoryReqDTO request = new FetchPurchasedTradeHistoryReqDTO(memberId, pageable);

        //when
        Optional<FetchPurchasedTradeHistoryResDTO> response =  tradeService.fetchPurchasedTradeHistory(request);

        //then
        assertThat(response.isEmpty()).isEqualTo(true);
    }

    @Test
    public void fetchPurchasedTradeHistoryEmpty() throws Exception {
        //given
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);
        Long memberId = buyer.getId();
        FetchPurchasedTradeHistoryReqDTO request = new FetchPurchasedTradeHistoryReqDTO(memberId, pageable);

        //when
        Optional<FetchPurchasedTradeHistoryResDTO> response =  tradeService.fetchPurchasedTradeHistory(request);

        //then
        assertThat(response.isEmpty()).isEqualTo(true);
    }


    @Test
    public void fetchReservedTradeHistoryForBuying() throws Exception {
        //given
        Long memberId = buyer.getId();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);
        FetchReservedTradeHistoryForBuyingReqDTO request = new FetchReservedTradeHistoryForBuyingReqDTO(memberId, pageable);

        //when
        Optional<FetchReservedTradeHistoryForBuyingResDTO> response = tradeService.fetchReservedTradeHistoryForBuying(request);

        //then
        assertThat(response.isPresent()).isEqualTo(true);
    }


}
