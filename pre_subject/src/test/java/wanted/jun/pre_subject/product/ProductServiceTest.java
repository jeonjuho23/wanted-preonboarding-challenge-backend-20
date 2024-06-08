package wanted.jun.pre_subject.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import wanted.jun.pre_subject.member.*;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductServiceTest {


    @Autowired
    private ProductService productService;


    @Autowired
    private MemberRepository memberRepository;
    private Member member;

    @BeforeEach
    void setUp() throws Exception {
        member = memberRepository.save(Member.signUp("id", "password", "name"));
    }

    @Test
    public void regist() throws Exception {
        //given
        Long memberId = member.getId();
        String productName = "product_name";
        Integer productPrice = 1000;
        ProductRegistReqDTO request = new ProductRegistReqDTO(memberId, productName, productPrice);

        //when
        productService.registProduct(request);

        //then
    }

    @Test
    public void registWrongMember() throws Exception {
        //given
        Long memberId = member.getId() + 1;
        String productName = "product_name";
        Integer productPrice = 1000;
        ProductRegistReqDTO request = new ProductRegistReqDTO(memberId, productName, productPrice);


        //when
        String message = null;
        try {
            productService.registProduct(request);
        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        }

        //then
        Assert.hasText(message, "예외 메세지가 있습니다.");
    }

}
