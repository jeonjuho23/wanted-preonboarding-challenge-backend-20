package wanted.jun.pre_subject.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.jun.pre_subject.member.ResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trades")
public class TradeController {

    private final TradeService tradeService;


    @PostMapping("{productId}")
    public ResponseEntity<ResponseDTO<?>> reserveProduct(@PathVariable("productId") Long productId, @RequestBody Long buyerId) {

        if(buyerId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>("제품 예약은 회원만 가능합니다.", null));

        ReserveTradeReqDTO request = new ReserveTradeReqDTO(productId, buyerId);
        try {
            tradeService.reserveProduct(request);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(ie.getMessage(), null));
        }

        return ResponseEntity.ok().body(new ResponseDTO<>("제품이 예약되었습니다.", null));
    }

    @PatchMapping("{tradeId}")
    public ResponseEntity<ResponseDTO<?>> approveTrade(@PathVariable("tradeId") Long tradeId, @RequestBody Long sellerId) {

        if(sellerId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>("제품 예약은 회원만 가능합니다.", null));


        ApproveTradeReqDTO request = new ApproveTradeReqDTO(tradeId, sellerId);
        ApproveTradeResDTO response;

        try {
            Trade trade = tradeService.approveTrade(request);
            response = new ApproveTradeResDTO(trade.getId());
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(ie.getMessage(), null));
        }

        return ResponseEntity.ok().body(new ResponseDTO<>("제품 판매가 승인되었습니다.", response));
    }
}
