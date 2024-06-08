package wanted.jun.pre_subject.member;

import org.apache.coyote.Response;
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


    @Test
    public void login() throws Exception {
        //given
        String id = "id";
        String password = "password";
        String name = "name";
        SignUpReqDTO signUpReqDTO = new SignUpReqDTO(id, password, name);
        memberController.signUp(signUpReqDTO);

        LoginReqDTO request = new LoginReqDTO(id, password);

        //when
        ResponseEntity<ResponseDTO<?>> response =  memberController.login(request);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        LoginResDTO data = (LoginResDTO) Objects.requireNonNull(response.getBody()).data();
        assertThat(data.id()).isEqualTo(id);
        assertThat(data.name()).isEqualTo(name);
    }


    @Test
    public void loginFail() throws Exception {
        //given
        String id = "id";
        String password = "password";
        String name = "name";
        SignUpReqDTO signUpReqDTO = new SignUpReqDTO(id, password, name);
        memberController.signUp(signUpReqDTO);

        String wrongPassword = "wrong password";
        LoginReqDTO request = new LoginReqDTO(id, wrongPassword);

        //when
        ResponseEntity<ResponseDTO<?>> response = memberController.login(request);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Object data = Objects.requireNonNull(response.getBody()).data();
        assertThat(data).isEqualTo(null);
    }


}
