package hello.itemservice.validation.web.controller;

import hello.itemservice.validation.domain.Item;
import hello.itemservice.validation.domain.ItemRepository;
import hello.itemservice.validation.domain.SaveCheck;
import hello.itemservice.validation.domain.UpdateCheck;
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
    @PostMapping("/add/v1")
    public String addItemV1(@Validated @ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes, Model model) {


        // 복합 룰 검증
        // Obje `ctError 는 이렇게 그냥 직접 로직짜서 추가하는게 더 좋다!
        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalCost = item.getPrice()*item.getQuantity();
            if (totalCost < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalCost} , "총 금액은 1만원이 넘어야 합니다. 현재 총 금액 = " + totalCost);
            }
        }

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

    // groups 원리 -> 내가 groups 에 세팅해준 클래스가 @Validated 에 포함되어있을때만 유효성 검사 해주는 원리!
    // 근데 애초에 value 값 안넣어주면 그냥 무시하고 Validate 체크한다!
    @PostMapping("/add")
    public String addItemV2(@Validated(value = SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes, Model model) {


        // 복합 룰 검증
        // Obje `ctError 는 이렇게 그냥 직접 로직짜서 추가하는게 더 좋다!
        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalCost = item.getPrice()*item.getQuantity();
            if (totalCost < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalCost} , "총 금액은 1만원이 넘어야 합니다. 현재 총 금액 = " + totalCost);
            }
        }

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

    @PostMapping("/{itemId}/edit/v1")
    public String editV1(@PathVariable Long itemId, @Validated @ModelAttribute Item item,
                       BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalCost = item.getPrice()*item.getQuantity();

            if (totalCost < 10000) {
                // 인자를 전달해도 되고 안해도 되고 선택사항 ( 오버로딩 되어있는 인자들 직접 체크해보면 알 수 있음 )

                Object[] messageArgs = {10000, totalCost};
                bindingResult.reject("totalPriceMin", messageArgs,"");
            }
        }

        // 굳이 다시 model 에 넣어줄 필요 X
        if (bindingResult.hasErrors()) {
            log.info("bindingReuslt = {}" , bindingResult);
            return "validation/v3/editForm";
        }


        itemRepository.update(itemId, item);

        redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String editV2(@PathVariable Long itemId, @Validated(value = UpdateCheck.class) @ModelAttribute Item item,
                       BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalCost = item.getPrice()*item.getQuantity();

            if (totalCost < 10000) {
                // 인자를 전달해도 되고 안해도 되고 선택사항 ( 오버로딩 되어있는 인자들 직접 체크해보면 알 수 있음 )

                Object[] messageArgs = {10000, totalCost};
                bindingResult.reject("totalPriceMin", messageArgs,"");
            }
        }

        // 굳이 다시 model 에 넣어줄 필요 X
        if (bindingResult.hasErrors()) {
            log.info("bindingReuslt = {}" , bindingResult);
            return "validation/v3/editForm";
        }


        itemRepository.update(itemId, item);

        redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/validation/v3/items/{itemId}";
    }
}
