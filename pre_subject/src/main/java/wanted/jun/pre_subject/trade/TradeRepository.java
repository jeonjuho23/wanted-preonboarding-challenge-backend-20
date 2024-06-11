package wanted.jun.pre_subject.trade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import wanted.jun.pre_subject.member.Member;
import wanted.jun.pre_subject.product.Product;

import java.util.Optional;


public interface TradeRepository extends JpaRepository<Trade, Long> {
    Page<Trade> findAllByBuyerAndProduct(Member buyer, Product product, Pageable pageable);
    Optional<Trade> findByProduct(Product product);
}
