package wanted.jun.pre_subject.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wanted.jun.pre_subject.member.ResponseDTO;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ResponseDTO<?>> registProduct(@RequestBody ProductRegistReqDTO request) {

        if (request.memberId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>("회원만 제품을 등록할 수 있습니다.", null));

        try {
            productService.registProduct(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>("회원을 찾을 수 없습니다.", null));
        }

        return ResponseEntity.ok().body(new ResponseDTO<>("제품이 등록되었습니다.", null));
    }
}
