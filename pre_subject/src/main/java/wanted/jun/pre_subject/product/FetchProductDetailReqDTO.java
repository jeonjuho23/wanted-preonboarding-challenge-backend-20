package wanted.jun.pre_subject.product;


import org.springframework.data.domain.Pageable;

public record FetchProductDetailReqDTO(Long memberId, Long productId, Pageable pageable) {
}
