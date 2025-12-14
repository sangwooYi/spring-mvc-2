package com.example.login.web.member;

import com.example.login.domain.member.Member;
import com.example.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;


    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member){

        // 위처럼 @ModelAttribute 에 이름을 지어주면
        // model.addAttribute("member", new Member()); 이 과정을 자동으로 해준다.

        return "member/addMemberForm";

    }

    @PostMapping("/add")
    public String saveMember(@Validated @ModelAttribute Member member, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("validation error :  {}", bindingResult.getAllErrors());

            return "member/addMemberForm";
        }

        memberRepository.save(member);
        return "redirect:/";
    }
}
