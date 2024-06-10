package wanted.jun.pre_subject.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.jun.pre_subject.member.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @Column(name = "product_name", length = 100)
    private String productName;
    @Column(name = "product_price")
    private Integer productPrice;
    @Column(name = "state")
    private String state;
    @Column(name = "regist_time")
    private LocalDateTime registTime;
    @Column(name = "state_update_time")
    private LocalDateTime stateUpdateTime;

    @Builder
    private Product(Member seller, String productName, Integer productPrice) {
        this.seller = seller;
        this.productName = productName;
        this.productPrice = productPrice;
        this.state = State.SALE.value();
        this.registTime = LocalDateTime.now();
        this.stateUpdateTime = LocalDateTime.now();
    }

    public static Product registProduct(Member seller, String productName, Integer productPrice) {
        return Product.builder()
                .seller(seller)
                .productName(productName)
                .productPrice(productPrice).build();
    }
}
