package wanted.jun.pre_subject.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wanted.jun.pre_subject.member.Member;
import wanted.jun.pre_subject.member.MemberRepository;
import wanted.jun.pre_subject.member.ResponseDTO;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductControllerTest {

    @Autowired
    ProductController productController;


    @Autowired
    private MemberRepository memberRepository;
    private Member member;

    @BeforeEach
    void setUp() throws Exception {
        member = memberRepository.save(Member.signUp("id", "password", "name"));
    }

    @Test
    public void registProduct() throws Exception {
        //given
        Long memberId = member.getId();
        String productName = "product_name";
        Integer productPrice = 1000;
        ProductRegistReqDTO request = new ProductRegistReqDTO(memberId, productName, productPrice);

        //when
        ResponseEntity<ResponseDTO<?>> response = productController.registProduct(request);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void registProductNonMember() throws Exception {
        //given
        Long memberId = null;
        String productName = "product_name";
        Integer productPrice = 1000;
        ProductRegistReqDTO request = new ProductRegistReqDTO(memberId, productName, productPrice);

        //when
        ResponseEntity<ResponseDTO<?>> response = productController.registProduct(request);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void registProductWrongMember() throws Exception {
        //given
        Long memberId = member.getId() + 1;
        String productName = "product_name";
        Integer productPrice = 1000;
        ProductRegistReqDTO request = new ProductRegistReqDTO(memberId, productName, productPrice);

        //when
        ResponseEntity<ResponseDTO<?>> response = productController.registProduct(request);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
