package com.example.login.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // Key, Value 형태로 저장
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    public Member save(Member member) {

        member.setId(sequence++);   // 저장후 1 증가

        log.info("member Save = {}", member);
        store.put(member.getId(), member);

        return member;
    }

    public Member findById(Long id) {

        Member member = store.get(id);

        return member;

    }

    public Optional<Member> findByLoginId(String loginId) {

        // Optional 객체 사용할떄 초기화는 .empty() 사용
        Optional<Member> member = Optional.empty();

        List<Member> memberList = this.findAll();

        for (Member mem : memberList) {

            if (mem.getLoginId().equals(loginId)) {

                // Optional<> 객체에 값 넣어주는 방법
                member = Optional.of(mem);
                break;
            }
        }

        return member;
    }

    public List<Member> findAll() {

        List<Member> memberList = new ArrayList<>(store.values());

        return memberList;
    }

}
