package hello.itemservice.validation.web.controller;

import hello.itemservice.validation.domain.Item;
import hello.itemservice.validation.domain.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping("")
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {

        // 검증 오류 결과를 보관
        Map<String ,String> errors = new HashMap<>();
        
        // 검증 로직 
        // StringUtils.hasText(텍스트) 해당 텍스트가 빈값인지 체크
        // return (str != null && !str.isBlank()); 가 로직
        if (!StringUtils.hasText(item.getItemName())) {
            errors.put("itemName", "상품 이름은 필수입니다.");
        }
        
        // null 값 체크 및 최소 최대값 체크
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.put("price", "값은 1000원 이상 100만원까지 범위입니다.");
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            errors.put("quantity", "수량은 최대 9,999 까지입니다.");
        }

        // 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalCost = item.getPrice()*item.getQuantity();
            if (totalCost < 10000) {
                errors.put("totalCostError", "총 금액은 1만원이 넘어야 합니다. 현재 총 금액 = " + totalCost);
            }
        }

        log.info("errors = {}", errors);
        // 검증 실패시 다시 그냥 입력폼으로
        if (!errors.isEmpty()) {
            // 위에서 @ModelAttribute 때문에 이미 model 에 item 이 들어가있다!
            // model.addAttribute("item", item); 이 자동으로 선언되는 셈!
            model.addAttribute("errors", errors);
            return "validation/v1/addForm";
        }

        Item savedItem = itemRepository.save(item);

        // 리다이렉트 때 attribute 할당 하는 방법, Path Variable 과 매핑되는 값은 자동으로 매핑,
        // 나머지는 쿼리스트링 형태로 자동 세팅
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }
}
