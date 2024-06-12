package wanted.jun.pre_subject.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.jun.pre_subject.member.ResponseDTO;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trades")
public class TradeController {

    private final TradeService tradeService;


    @PostMapping("{productId}")
    public ResponseEntity<ResponseDTO<?>> reserveProduct(@PathVariable("productId") Long productId, @RequestBody Map<String, Long> data) {

        Long buyerId = data.get("buyerId");
        if(buyerId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>("제품 예약은 회원만 가능합니다.", null));
        }

        ReserveTradeReqDTO request = new ReserveTradeReqDTO(productId, buyerId);
        try {
            tradeService.reserveProduct(request);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(ie.getMessage(), null));
        }

        return ResponseEntity.ok().body(new ResponseDTO<>("제품이 예약되었습니다.", null));
    }

    @PatchMapping("{tradeId}")
    public ResponseEntity<ResponseDTO<?>> approveTrade(@PathVariable("tradeId") Long tradeId, @RequestBody Map<String, Long> data) {

        Long sellerId = data.get("sellerId");
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

    @GetMapping("/purchased/{memberId}")
    public ResponseEntity<ResponseDTO<?>> fetchPurchasedTradeHistory
            (@PathVariable("memberId") Long memberId,
             @PageableDefault(size = 10, page = 0, sort = "product.stateUpdateTime") Pageable pageable) {

        FetchPurchasedTradeHistoryReqDTO request = new FetchPurchasedTradeHistoryReqDTO(memberId, pageable);
        Optional<FetchPurchasedTradeHistoryResDTO> response;
        try {
            response = tradeService.fetchPurchasedTradeHistory(request);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(ie.getMessage(), Optional.empty()));
        }

        return ResponseEntity.ok().body(new ResponseDTO<>("구매한 제품 목록을 조회했습니다.", response));
    }

    @GetMapping("/reserved/{memberId}")
    public ResponseEntity<ResponseDTO<?>> fetchReservedTradeHistoryForBuying
            (@PathVariable("memberId") Long memberId,
             @PageableDefault(size = 10, page = 0, sort = "product.stateUpdateTime") Pageable pageable) {

        FetchReservedTradeHistoryForBuyingReqDTO request = new FetchReservedTradeHistoryForBuyingReqDTO(memberId, pageable);
        Optional<FetchReservedTradeHistoryForBuyingResDTO> response;
        try {
            response = tradeService.fetchReservedTradeHistoryForBuying(request);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(ie.getMessage(), Optional.empty()));
        }

        return ResponseEntity.ok().body(new ResponseDTO<>("예약한 제품 목록을 조회했습니다.", response));
    }
}
