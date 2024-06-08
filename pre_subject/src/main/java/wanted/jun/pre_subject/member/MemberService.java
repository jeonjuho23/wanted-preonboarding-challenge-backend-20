package wanted.jun.pre_subject.member;

import javax.security.auth.login.FailedLoginException;

interface MemberService {
    void signUp(SignUpReqDTO request) throws Exception;

    LoginResDTO login(LoginReqDTO request) throws FailedLoginException;
}
