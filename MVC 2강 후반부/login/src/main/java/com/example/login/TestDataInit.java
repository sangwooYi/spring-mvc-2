package com.example.login;


import com.example.login.domain.item.Item;
import com.example.login.domain.item.ItemRepository;
import com.example.login.domain.member.Member;
import com.example.login.domain.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    /**
     * 테스트용 데이터 추가
     * @PostConstruct는 해당 클래스의 생성자가 생성 된 이후 시점에 자동으로 호출되도록 해주는 어노테이션!
     * 따라서 TestDataInit 생성자가 추가 된 이후에 아래 init 메서드가 자동으로 호출 된다!
     * ( init 테스트 데이터 만들때 유용하게 사용하면 됨 ! )
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
        itemRepository.save(new Item("itemC", 8000, 50));

        memberRepository.save(new Member("memberA", "nameA", "123123"));
        memberRepository.save(new Member("memberB", "nameB", "123123"));
        memberRepository.save(new Member("okqwaszx", "이상우", "123123"));
    }

}