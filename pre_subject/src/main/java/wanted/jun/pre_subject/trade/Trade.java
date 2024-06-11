package wanted.jun.pre_subject.trade;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.jun.pre_subject.member.Member;
import wanted.jun.pre_subject.product.Product;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder(access = AccessLevel.PRIVATE)
    private Trade(Member buyer, Product product) {
        this.buyer = buyer;
        this.product = product;
    }

    public static Trade startTrade(Member buyer, Product product) {
        product.changeStatusToReserved();
        return Trade.builder()
                .buyer(buyer)
                .product(product).build();
    }

    
}
