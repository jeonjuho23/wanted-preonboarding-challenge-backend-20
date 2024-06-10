package wanted.jun.pre_subject.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.FailedLoginException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpReqDTO request) {

        try {
            memberService.signUp(request);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(e.getMessage(), Optional.empty()));
        }

        return ResponseEntity.ok().body(new ResponseDTO<>("회원가입이 완료되었습니다.", Optional.empty()));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<?>> login(@RequestBody LoginReqDTO request) {

        LoginResDTO response;
        try {
            response = memberService.login(request);
        } catch (FailedLoginException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(e.getMessage(), Optional.empty()));
        }

        return ResponseEntity.ok().body(new ResponseDTO<>("로그인에 성공했습니다.", response));
    }
}
