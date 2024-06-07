package wanted.jun.pre_subject.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void signUp() throws Exception {
        //given
        String id = "id";
        String password = "password";
        String name = "name";
        SignUpReqDTO request = new SignUpReqDTO(id, password, name);

        //when
        memberService.signUp(request);

        //then
    }


    @Test
    public void signUpDuplicateUserIdException() throws Exception {
        //given
        String id = "id";
        String password = "password";
        String name = "name";
        SignUpReqDTO request = new SignUpReqDTO(id, password, name);

        //when
        memberService.signUp(request);
        // 중복된 값으로 한번 더
        String message = null;
        try {
            memberService.signUp(request);
        } catch (Exception e) {
            message = e.getMessage();
        }

        //then
        Assert.hasText(message, "예외에대한 메세지가 존재해야합니다.");
    }


}
