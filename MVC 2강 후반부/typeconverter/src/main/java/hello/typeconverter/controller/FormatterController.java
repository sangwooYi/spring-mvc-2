package hello.typeconverter.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Slf4j
@Controller
public class FormatterController {

    @GetMapping("/formatter/edit")
    public String formatterForm(Model model) {

        Form form = new Form();
        form.setNumber(1000000);
        form.setLocalDateTime(LocalDateTime.now());

        model.addAttribute("formData", form);

        return "formatterForm";
    }

    @PostMapping("/formatter/edit")
    public String formatterEdit(@ModelAttribute(name="form") Form form) {

        log.info("form = {}", form);

        return "redirect:/";
    }

    // getter, setter, requiredArgsConstructor 등 DTO에 필요한 어노테이션 엥간한거 다 걸려있음
    @Data
    static class Form {

        // 당연히 아래 포매터 pattern 에 맞지 않는 값이 전달되면 에러난다.typeMismatch !

        // 콤마로 구분, 3자리마다.
        @NumberFormat(pattern = "###,###")
        private Integer number;

        // 2026-01-15 23:10:11 형태
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime localDateTime;
    }
}
