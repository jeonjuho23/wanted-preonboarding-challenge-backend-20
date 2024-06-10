package wanted.jun.pre_subject.product;

import java.util.List;

public record FetchProductListResDTO(int page, boolean hasNextPage, boolean hasPreviousPage,
                              int numberOfElements, boolean hasContent,
                              List<ProductListDTO> content) {
}
