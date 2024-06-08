package wanted.jun.pre_subject.product;

import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.jun.pre_subject.member.Member;
import wanted.jun.pre_subject.member.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void registProduct(ProductRegistReqDTO request) {

        Member seller = memberRepository.findById(request.memberId())
                .orElseThrow(()-> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Product product = Product.registProduct(seller, request.productName(), request.productPrice());

        productRepository.save(product);

    }


}
