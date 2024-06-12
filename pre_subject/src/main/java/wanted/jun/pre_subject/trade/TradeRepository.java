package wanted.jun.pre_subject.trade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import wanted.jun.pre_subject.member.Member;
import wanted.jun.pre_subject.product.Product;

import java.util.Optional;


public interface TradeRepository extends JpaRepository<Trade, Long> {
    Page<Trade> findAllByBuyerAndProduct(Member buyer, Product product, Pageable pageable);
    Optional<Trade> findByProduct(Product product);
    @EntityGraph(attributePaths = {"product", "product.seller"})
    Page<Trade> findAllByBuyerAndProduct_ProductState(Member buyer, String productState, Pageable pageable);

    @EntityGraph(attributePaths = {"product", "buyer"})
    Page<Trade> findAllByProduct_SellerAndProduct_ProductState(Member seller, String productState, Pageable pageable);
}
