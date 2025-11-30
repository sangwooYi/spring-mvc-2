package hello.itemservice.validation.web.controller;

import hello.itemservice.validation.domain.Item;
import hello.itemservice.validation.domain.ItemRepository;
import hello.itemservice.validation.web.ItemValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationControllerV3 {

    private final ItemRepository itemRepository;

    @GetMapping("")
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }


    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

    // 체크할 DTO 앞에 @Validated 붙여주면
    // Bean Validation 설정이 되었으면 별도의 Validator 설정 없이도
    // 스프링이 알아서 내가 설정한 validate 조건에 맞춰 체크해준다!
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);

            // 굳이 모델에 담을 필요 X
            return "validation/v3/addForm";
        }

        Item savedItem = itemRepository.save(item);

        // 리다이렉트 때 attribute 할당 하는 방법, Path Variable 과 매핑되는 값은 자동으로 매핑,
        // 나머지는 쿼리스트링 형태로 자동 세팅
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }
}
