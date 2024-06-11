package wanted.jun.pre_subject.trade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wanted.jun.pre_subject.member.Member;
import wanted.jun.pre_subject.member.MemberRepository;
import wanted.jun.pre_subject.member.ResponseDTO;
import wanted.jun.pre_subject.product.Product;
import wanted.jun.pre_subject.product.ProductRepository;

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

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.reserveProduct(productId, buyerId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void reserveProductNonMember() throws Exception {
        //given
        Long productId = product.getId();

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.reserveProduct(productId, null);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void reserveProductWithWrongMember() throws Exception {
        //given
        Long productId = product.getId();
        Long buyerId = buyer.getId() + 10;

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.reserveProduct(productId, buyerId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void reserveProductWithWrongProduct() throws Exception {
        //given
        Long productId = product.getId() + 10;
        Long buyerId = buyer.getId();

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.reserveProduct(productId, buyerId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void reserveProductWithSeller() throws Exception {
        //given
        Long productId = product.getId();
        Long buyerId = seller.getId();

        //when
        ResponseEntity<ResponseDTO<?>> response = tradeController.reserveProduct(productId, buyerId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


}
