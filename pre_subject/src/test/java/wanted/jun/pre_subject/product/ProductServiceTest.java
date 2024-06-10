package wanted.jun.pre_subject.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import wanted.jun.pre_subject.member.*;

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

    @Test
    public void fetchProductList() throws Exception {
        //given
        String productName = "product_name";
        Integer productPrice = 1000;
        ProductRegistReqDTO productRegistReqDTO = new ProductRegistReqDTO(member.getId(), productName, productPrice);
        productService.registProduct(productRegistReqDTO);

        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.REGIST_TIME.value());
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        //when
        FetchProductListResDTO response = productService.fetchProductList(pageRequest);

        //then
        assertThat(response.content().get(0).productName()).isEqualTo(productName);
    }

    @Test
    public void fetchProductListSortByRegistTime() throws Exception {
        //given
        String productName = "product_name";
        Integer productPrice = 1000;
        ProductRegistReqDTO productRegistReqDTO = new ProductRegistReqDTO(member.getId(), productName, productPrice);
        productService.registProduct(productRegistReqDTO);
        String productName2 = "product_name__2";
        Integer productPrice2 = 2000;
        ProductRegistReqDTO productRegistReqDTO2 = new ProductRegistReqDTO(member.getId(), productName2, productPrice2);
        productService.registProduct(productRegistReqDTO2);

        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.REGIST_TIME.value());
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        //when
        FetchProductListResDTO response = productService.fetchProductList(pageRequest);

        //then
        assertThat(response.content().get(0).productName()).isEqualTo(productName);
    }


    @Test
    public void fetchProductDetailForMember() throws Exception {
        //given
        String productName = "product_name";
        Integer productPrice = 1000;
        ProductRegistReqDTO productRegistReqDTO = new ProductRegistReqDTO(member.getId(), productName, productPrice);
        productService.registProduct(productRegistReqDTO);

        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.REGIST_TIME.value());
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Long productId = productService.fetchProductList(pageRequest).content().get(0).productId();
        Long memberId = member.getId();

        page = 0;
        size = 5;
        sort = Sort.by(SortBy.PRODUCT_STATE_UPDATE_TIME.value());
        Pageable pageable = PageRequest.of(page, size, sort);
        FetchProductDetailReqDTO request = new FetchProductDetailReqDTO(memberId, productId, pageable);

        //when
        FetchProductDetailResDTO response = productService.fetchProductDetailForMember(request);

        //then
        assertThat(response.productId()).isEqualTo(productId);
    }


    @Test
    public void fetchProductDetailForNonMember() throws Exception {
        //given
        String productName = "product_name";
        Integer productPrice = 1000;
        ProductRegistReqDTO productRegistReqDTO = new ProductRegistReqDTO(member.getId(), productName, productPrice);
        productService.registProduct(productRegistReqDTO);

        int page = 0;
        int size = 10;
        Sort sort = Sort.by(SortBy.REGIST_TIME.value());
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Long productId = productService.fetchProductList(pageRequest).content().get(0).productId();

        FetchProductDetailNonMemberReqDTO request = new FetchProductDetailNonMemberReqDTO(productId);
        //when
        FetchProductDetailResDTO response = productService.fetchProductDetailForNonMember(request);

        //then
        assertThat(response.productId()).isEqualTo(productId);
    }


}
