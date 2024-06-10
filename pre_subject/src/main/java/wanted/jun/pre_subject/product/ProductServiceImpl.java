package wanted.jun.pre_subject.product;

import lombok.*;
import org.hibernate.FetchNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.jun.pre_subject.member.Member;
import wanted.jun.pre_subject.member.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    private static final Integer PRODUCT_LIST_PAGE_SIZE = 10;


    @Override
    @Transactional
    public void registProduct(ProductRegistReqDTO request) {

        Member seller = memberRepository.findById(request.memberId())
                .orElseThrow(()-> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Product product = Product.registProduct(seller, request.productName(), request.productPrice());

        productRepository.save(product);

    }

    @Override
    public FetchProductListResDTO fetchProductList(Pageable request) throws Exception {

        Page<Product> productPage = productRepository.findAll(request);

        List<ProductListDTO> productListDTOS = productPage.getContent().stream().map(content ->
                new ProductListDTO(content.getId(), content.getProductName(), content.getProductPrice(),
                        content.getState(), content.getSeller().getUserId(), content.getRegistTime())).toList();

        if(!productPage.hasContent()) throw new Exception("해당 페이지에는 데이터가 없습니다.");


        return new FetchProductListResDTO(request.getPageNumber(), productPage.hasNext(), productPage.hasPrevious(),
                productPage.getNumberOfElements(), productPage.hasContent(), productListDTOS);
    }


}
