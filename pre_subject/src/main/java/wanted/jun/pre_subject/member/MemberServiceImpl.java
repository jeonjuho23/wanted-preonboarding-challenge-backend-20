package wanted.jun.pre_subject.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


    @Override
    @Transactional
    public void signUp(SignUpReqDTO request) throws Exception {
        if (isDuplicateMemberId(request.id())) throw new Exception("입력하신 ID와 중복된 ID가 존재합니다.");

        Member newMember = Member.signUp(request.id(), request.password(), request.name());
        memberRepository.save(newMember);
    }

    private boolean isDuplicateMemberId(String id) {
        Optional<Member> optionalMember = memberRepository.findByUserId(id);
        return optionalMember.isPresent();
    }

    @Override
    public LoginResDTO login(LoginReqDTO request) throws FailedLoginException {
        Optional<Member> optionalMember
                = memberRepository.findByUserIdAndUserPassword(request.id(), request.password());

        Member member = optionalMember.orElseThrow(() -> new FailedLoginException("로그인에 실패했습니다."));

        return new LoginResDTO(member.getUserId(), member.getUserName(), member.getId());
    }
}
