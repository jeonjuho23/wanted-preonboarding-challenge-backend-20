package wanted.jun.pre_subject.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberControllerTest {

    @Autowired
    private MemberController memberController;

    @Test
    public void signUp() throws Exception {
        //given
        String id = "id";
        String password = "password";
        String name = "name";
        SignUpReqDTO request = new SignUpReqDTO(id, password, name);

        //when
        ResponseEntity<?> response = memberController.signUp(request);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void signUpDuplicateUserIdException() throws Exception {
        //given
        String id = "id";
        String password = "password";
        String name = "name";
        SignUpReqDTO request = new SignUpReqDTO(id, password, name);

        //when
        memberController.signUp(request);
        ResponseEntity<?> response = memberController.signUp(request);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }



}
