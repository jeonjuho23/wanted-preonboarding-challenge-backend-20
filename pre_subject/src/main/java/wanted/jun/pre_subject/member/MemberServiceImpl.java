package wanted.jun.pre_subject.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
