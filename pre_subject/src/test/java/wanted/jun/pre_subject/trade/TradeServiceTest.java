package wanted.jun.pre_subject.trade;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wanted.jun.pre_subject.member.Member;
import wanted.jun.pre_subject.member.MemberRepository;
import wanted.jun.pre_subject.product.Product;
import wanted.jun.pre_subject.product.ProductRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
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
        ReservedTradeReqDTO request = new ReservedTradeReqDTO(productId, buyerId);

        LocalDateTime stateUpdateTime = product.getStateUpdateTime();

        //when
        Trade reservedTrade = tradeService.reserveProduct(request);

        //then
        assertThat(reservedTrade.getProduct().getId()).isEqualTo(productId);
        assertThat(reservedTrade.getBuyer().getId()).isEqualTo(buyerId);
        assertThat(reservedTrade.getProduct().getStateUpdateTime()).isNotEqualTo(stateUpdateTime);
    }

}
