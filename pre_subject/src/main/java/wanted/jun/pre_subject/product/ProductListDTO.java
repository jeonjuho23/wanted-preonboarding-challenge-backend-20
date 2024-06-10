package wanted.jun.pre_subject.product;

import java.time.LocalDateTime;

public record ProductListDTO(Long productId, String productName, Integer productPrice,
                             String productState, String sellerId, LocalDateTime registTime) {
}
