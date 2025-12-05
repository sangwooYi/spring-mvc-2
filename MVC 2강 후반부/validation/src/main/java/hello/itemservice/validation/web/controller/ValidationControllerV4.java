package hello.itemservice.validation.web.controller;

import hello.itemservice.validation.domain.Item;
import hello.itemservice.validation.domain.ItemRepository;
import hello.itemservice.validation.domain.SaveCheck;
import hello.itemservice.validation.domain.UpdateCheck;
import hello.itemservice.validation.web.controller.form.ItemSaveForm;
import hello.itemservice.validation.web.controller.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationControllerV4 {

    private final ItemRepository itemRepository;

    @GetMapping("")
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }


    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new ItemSaveForm());
        return "validation/v4/addForm";
    }

    // 체크할 DTO 앞에 @Validated 붙여주면
    // Bean Validation 설정이 되었으면 별도의 Validator 설정 없이도
    // 스프링이 알아서 내가 설정한 validate 조건에 맞춰 체크해준다!
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {

        log.info("bindingResult = {}", bindingResult);
        // 복합 룰 검증
        // Obje `ctError 는 이렇게 그냥 직접 로직짜서 추가하는게 더 좋다!
        if (form.getPrice() != null && form.getQuantity() != null) {
            int totalCost = form.getPrice()*form.getQuantity();
            if (totalCost < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalCost} , "총 금액은 1만원이 넘어야 합니다. 현재 총 금액 = " + totalCost);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);

            // 굳이 모델에 담을 필요 X
            return "validation/v4/addForm";
        }
        
        // 변환과정이 추가로 필요하긴 함
        Item itemDto = new Item(form.getItemName(), form.getPrice(), form.getQuantity());

        Item savedItem = itemRepository.save(itemDto);

        // 리다이렉트 때 attribute 할당 하는 방법, Path Variable 과 매핑되는 값은 자동으로 매핑,
        // 나머지는 쿼리스트링 형태로 자동 세팅
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        ItemUpdateForm form = new ItemUpdateForm();
        form.setId(item.getId());
        form.setItemName(item.getItemName());
        form.setPrice(item.getPrice());
        form.setQuantity(item.getQuantity());

        model.addAttribute("item", form);
        return "validation/v4/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form,
                       BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (form.getPrice() != null && form.getQuantity() != null) {
            int totalCost = form.getPrice()*form.getQuantity();

            if (totalCost < 10000) {
                // 인자를 전달해도 되고 안해도 되고 선택사항 ( 오버로딩 되어있는 인자들 직접 체크해보면 알 수 있음 )

                Object[] messageArgs = {10000, totalCost};
                bindingResult.reject("totalPriceMin", messageArgs,"");
            }
        }

        // 굳이 다시 model 에 넣어줄 필요 X
        if (bindingResult.hasErrors()) {
            log.info("bindingReuslt = {}" , bindingResult);
            return "validation/v4/editForm";
        }
        // 변환과정이 추가로 필요하긴 함
        Item itemDto = new Item(form.getId(), form.getItemName(), form.getPrice(), form.getQuantity());

        itemRepository.update(itemId, itemDto);

        redirectAttributes.addAttribute("itemId", itemId);
        return "redirect:/validation/v4/items/{itemId}";
    }
}
