package wanted.jun.pre_subject.product;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.jun.pre_subject.member.ResponseDTO;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ResponseDTO<?>> registProduct(@RequestBody ProductRegistReqDTO request) {

        if (request.memberId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>("회원만 제품을 등록할 수 있습니다.", Optional.empty()));

        try {
            productService.registProduct(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>("회원을 찾을 수 없습니다.", Optional.empty()));
        }

        return ResponseEntity.ok().body(new ResponseDTO<>("제품이 등록되었습니다.", Optional.empty()));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<?>> fetchProductList
            (@PageableDefault(size = 10, page = 0, sort = "registTime") Pageable pageable) {

        FetchProductListResDTO response;
        try {
            response = productService.fetchProductList(pageable);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(e.getMessage(), Optional.empty()));
        }

        return ResponseEntity.ok().body(new ResponseDTO<>("제품 목록을 조회했습니다.", response));
    }

    @GetMapping("/{productId}/{memberId}")
    public ResponseEntity<ResponseDTO<?>> fetchProductDetailForMember
            (@PathVariable("productId") Long productId, @PathVariable("memberId") Long memberId,
             @PageableDefault(size = 5, page = 0, sort = "product.stateUpdateTime") Pageable pageable) {

        FetchProductDetailReqDTO request = new FetchProductDetailReqDTO(memberId, productId, pageable);
        FetchProductDetailResDTO response;
        try {
            response = productService.fetchProductDetailForMember(request);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(ie.getMessage(), Optional.empty()));
        }

        return ResponseEntity.ok().body(new ResponseDTO<>("제품의 상세 정보를 조회했습니다.", response));
    }

    @GetMapping("/{productId")
    public ResponseEntity<ResponseDTO<?>> fetchProductDetailForNonMember
            (@PathVariable("productId") Long productId) {

        FetchProductDetailNonMemberReqDTO request = new FetchProductDetailNonMemberReqDTO(productId);
        FetchProductDetailResDTO response;
        try {
            response = productService.fetchProductDetailForNonMember(request);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(ie.getMessage(), Optional.empty()));
        }

        return ResponseEntity.ok().body(new ResponseDTO<>("제품의 상세 정보를 조회했습니다.", response));
    }
}
