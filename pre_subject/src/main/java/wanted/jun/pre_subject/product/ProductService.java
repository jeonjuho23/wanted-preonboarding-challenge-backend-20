package wanted.jun.pre_subject.product;

import org.springframework.data.domain.Pageable;

public interface ProductService {
    void registProduct(ProductRegistReqDTO request);

    FetchProductListResDTO fetchProductList(Pageable request) throws Exception;
}
