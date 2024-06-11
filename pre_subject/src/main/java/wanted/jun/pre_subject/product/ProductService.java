package wanted.jun.pre_subject.product;

import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product registProduct(ProductRegistReqDTO request);

    FetchProductListResDTO fetchProductList(Pageable request) throws Exception;

    FetchProductDetailResDTO fetchProductDetailForMember(FetchProductDetailReqDTO request);

    FetchProductDetailResDTO fetchProductDetailForNonMember(FetchProductDetailNonMemberReqDTO request);
}
