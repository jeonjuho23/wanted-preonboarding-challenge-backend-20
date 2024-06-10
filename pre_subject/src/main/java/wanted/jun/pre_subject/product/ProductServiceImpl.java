package wanted.jun.pre_subject.product;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.jun.pre_subject.member.Member;
import wanted.jun.pre_subject.member.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;


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
                        content.getProductState(), content.getSeller().getUserId(), content.getRegistTime())).toList();

        if(!productPage.hasContent()) throw new Exception("해당 페이지에는 데이터가 없습니다.");


        return new FetchProductListResDTO(request.getPageNumber(), productPage.hasNext(), productPage.hasPrevious(),
                productPage.getNumberOfElements(), productPage.hasContent(), productListDTOS);
    }

    @Override
    public FetchProductDetailResDTO fetchProductDetailForMember(FetchProductDetailReqDTO request) {

        Member buyer = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new IllegalArgumentException("제품이 존재하지 않습니다."));

        Page<Trade> tradeHistoryPage;
        if (Objects.equals(buyer.getId(), product.getSeller().getId())
            && Objects.equals(product.getProductState(), State.SALE.value())) {
            return new FetchProductDetailResDTO(product.getId(), product.getProductName(), product.getProductPrice(),
                    product.getSeller().getUserId(), product.getProductState(), product.getRegistTime(), Optional.empty());
        } else if (Objects.equals(buyer.getId(), product.getSeller().getId())
                && (!Objects.equals(product.getProductState(), State.SALE.value()))) {
            Trade trade = tradeRepository.findByProduct(product)
                    .orElseThrow(() -> new IllegalArgumentException("거래가 존재하지 않습니다."));
            buyer = trade.getBuyer();
        }

        tradeHistoryPage = tradeRepository.findAllByBuyerAndProduct(buyer, product, request.pageable());

        List<TradeHistoryDTO> content = tradeHistoryPage.stream()
                .map(trade -> new TradeHistoryDTO(trade.getId(), trade.getProduct().getProductName(),
                        trade.getProduct().getProductPrice(), trade.getProduct().getProductState(),
                        trade.getProduct().getStateUpdateTime()))
                .toList();

        TradeHistoryListDTO tradeHistory = new TradeHistoryListDTO(tradeHistoryPage.getPageable().getPageNumber(),
                tradeHistoryPage.hasNext(), tradeHistoryPage.hasPrevious(), tradeHistoryPage.getNumberOfElements(),
                tradeHistoryPage.hasContent(), content);

        return new FetchProductDetailResDTO(product.getId(), product.getProductName(), product.getProductPrice(),
                product.getSeller().getUserId(), product.getProductState(), product.getRegistTime(),
                Optional.of(tradeHistory));
    }


}
