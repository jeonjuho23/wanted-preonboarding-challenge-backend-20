package wanted.jun.pre_subject.trade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wanted.jun.pre_subject.member.Member;
import wanted.jun.pre_subject.member.MemberRepository;
import wanted.jun.pre_subject.member.ResponseDTO;
import wanted.jun.pre_subject.product.Product;
import wanted.jun.pre_subject.product.ProductRepository;
import wanted.jun.pre_subject.product.SortBy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TradeControllerTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TradeController tradeController;
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
        Map<String, Long> data = new HashMap<>();
        data.put("buyerId", buyer.getId());

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.reserveProduct(productId, data);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void reserveProductNonMember() throws Exception {
        //given
        Long productId = product.getId();
        Map<String, Long> data = new HashMap<>();

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.reserveProduct(productId, data);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void reserveProductWithWrongMember() throws Exception {
        //given
        Long productId = product.getId();
        Map<String, Long> data = new HashMap<>();
        data.put("buyerId", buyer.getId()+10);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.reserveProduct(productId, data);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void reserveProductWithWrongProduct() throws Exception {
        //given
        Long productId = product.getId() + 10;
        Map<String, Long> data = new HashMap<>();
        data.put("buyerId", buyer.getId());

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.reserveProduct(productId, data);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void reserveProductWithSeller() throws Exception {
        //given
        Long productId = product.getId();
        Map<String, Long> data = new HashMap<>();
        data.put("buyerId", seller.getId());

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.reserveProduct(productId, data);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    public void approveTrade() throws Exception {
        //given
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));

        Long tradeId = reservedTrade.getId();
        Map<String, Long> data = new HashMap<>();
        data.put("sellerId", seller.getId());

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.approveTrade(tradeId, data);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ApproveTradeResDTO dto = (ApproveTradeResDTO) response.getBody().data();
        assertThat(dto.tradeId()).isEqualTo(tradeId);
    }


    @Test
    public void approveTradeNonMember() throws Exception {
        //given
        Long productId = product.getId();
        Map<String, Long> data = new HashMap<>();

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.approveTrade(productId, data);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void approveProductWithWrongMember() throws Exception {
        //given
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));

        Long tradeId = reservedTrade.getId();
        Map<String, Long> data = new HashMap<>();
        data.put("sellerId", seller.getId() + 10);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.approveTrade(tradeId, data);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void approveProductWithWrongTrade() throws Exception {
        //given
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));

        Long tradeId = reservedTrade.getId() + 10;
        Map<String, Long> data = new HashMap<>();
        data.put("sellerId", seller.getId());

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.approveTrade(tradeId, data);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void approveProductWithBuyer() throws Exception {
        //given
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));

        Long tradeId = reservedTrade.getId();
        Map<String, Long> data = new HashMap<>();
        data.put("sellerId", buyer.getId());

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.approveTrade(tradeId, data);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    public void fetchPurchasedTradeHistory() throws Exception {
        //given
        // 예약
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));
        // 승인
        Long sellerId = seller.getId();
        Long tradeId = reservedTrade.getId();
        ApproveTradeReqDTO approveTradeReqDTO = new ApproveTradeReqDTO(tradeId, sellerId);
        Trade approvedTrade = tradeService.approveTrade(approveTradeReqDTO);

        Long memberId = buyer.getId();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.fetchPurchasedTradeHistory(memberId, pageable);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void fetchPurchasedTradeHistoryEmpty() throws Exception {
        //given
        Long memberId = buyer.getId();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.fetchPurchasedTradeHistory(memberId, pageable);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().data()).isEqualTo(Optional.empty());
    }

    @Test
    public void fetchPurchasedTradeHistoryEmptyWithReservedProduct() throws Exception {
        //given
        // 예약
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));

        Long memberId = buyer.getId();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.fetchPurchasedTradeHistory(memberId, pageable);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().data()).isEqualTo(Optional.empty());
    }

    @Test
    public void fetchPurchasedTradeHistoryNonMember() throws Exception {
        //given
        // 예약
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));

        Long memberId = null;
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.fetchPurchasedTradeHistory(memberId, pageable);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().data()).isEqualTo(Optional.empty());
    }


    @Test
    public void fetchReservedTradeHistoryForBuying() throws Exception {
        //given
        // 예약
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));

        Long memberId = buyer.getId();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.fetchReservedTradeHistoryForBuying(memberId, pageable);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().data()).isNotEqualTo(Optional.empty());
    }

    @Test
    public void fetchReservedTradeHistoryForBuyingEmpty() throws Exception {
        //given
        Long memberId = buyer.getId();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.fetchReservedTradeHistoryForBuying(memberId, pageable);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().data()).isEqualTo(Optional.empty());
    }

    @Test
    public void fetchReservedTradeHistoryForBuyingEmptyWithPurchasedProduct() throws Exception {
        //given
        // 예약
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));
        // 승인
        Long sellerId = seller.getId();
        Long tradeId = reservedTrade.getId();
        ApproveTradeReqDTO approveTradeReqDTO = new ApproveTradeReqDTO(tradeId, sellerId);
        Trade approvedTrade = tradeService.approveTrade(approveTradeReqDTO);

        Long memberId = buyer.getId();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.fetchReservedTradeHistoryForBuying(memberId, pageable);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().data()).isEqualTo(Optional.empty());
    }

    @Test
    public void fetchReservedTradeHistoryForBuyingNonMember() throws Exception {
        //given
        // 예약
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));

        Long memberId = null;
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.fetchReservedTradeHistoryForBuying(memberId, pageable);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().data()).isEqualTo(Optional.empty());
    }


    @Test
    public void fetchReservedTradeHistoryForSelling() throws Exception {
        //given
        // 예약
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));

        Long memberId = seller.getId();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.fetchReservedTradeHistoryForSelling(memberId, pageable);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().data()).isNotEqualTo(Optional.empty());
    }

    @Test
    public void fetchReservedTradeHistoryForSellingEmpty() throws Exception {
        //given
        Long memberId = buyer.getId();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.fetchReservedTradeHistoryForSelling(memberId, pageable);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().data()).isEqualTo(Optional.empty());
    }

    @Test
    public void fetchReservedTradeHistoryForSellingEmptyWithPurchasedProduct() throws Exception {
        //given
        // 예약
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));
        // 승인
        Long sellerId = seller.getId();
        Long tradeId = reservedTrade.getId();
        ApproveTradeReqDTO approveTradeReqDTO = new ApproveTradeReqDTO(tradeId, sellerId);
        Trade approvedTrade = tradeService.approveTrade(approveTradeReqDTO);

        Long memberId = buyer.getId();
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.fetchReservedTradeHistoryForSelling(memberId, pageable);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().data()).isEqualTo(Optional.empty());
    }

    @Test
    public void fetchReservedTradeHistoryForSellingNonMember() throws Exception {
        //given
        // 예약
        Long productId = product.getId();
        Long buyerId = buyer.getId();
        Trade reservedTrade = tradeService.reserveProduct(new ReserveTradeReqDTO(productId, buyerId));

        Long memberId = null;
        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.fetchReservedTradeHistoryForSelling(memberId, pageable);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().data()).isEqualTo(Optional.empty());
    }
}
