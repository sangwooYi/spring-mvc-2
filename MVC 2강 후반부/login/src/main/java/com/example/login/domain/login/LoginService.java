package com.example.login.domain.login;


import com.example.login.domain.member.Member;
import com.example.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @param loginId
     * @param password
     * @return Null 이면 로그인 실패 로직
     */
    public Member login(String loginId, String password) {

        // Optional 이렇게 filter 기능도 사용 가능! ( map 도 사용 가능! )
        // filter 로 한번 체크하고 -> orElse / orElseThrow 통해서 처리하기
        Member member = memberRepository.findByLoginId(loginId).filter(m -> m.getPassword().equals(password)).orElse(null);

        return member;
    }
}
