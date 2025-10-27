package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    // @ModelAttribute 어노테이션 통해서 해당 컨트롤러 공통으로 특정 Attribute 를 보내줄 수 있다!
    // @ModelAttribute 의 역할 1) RequestParam, Path Variable 값을 DTO 형태로 매핑
    //                         2) Response 에 Model에 특정 Attribute 를 추가해 줌 @ModelAttribute({변수명}) 형태
    // 물론 메서드마다 일일히 .model 에 넣어줘도 되나, 공통적으로 쓸 값이 있다면 @ModelAttribute 활용하기
    @ModelAttribute("regions")
    public Map<String, String> regions() {
        // LinkedHashMap 을 쓴 이유는 순서를 보장하기 위함
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("GYONGI", "경기");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        // 이렇게 객체값을 return 해주면 @ModelAttribute("{변수명}") 에 지정해둔 변수명으로 알아서 이 객체가 
        // Model 에 들어간다.
        return regions;
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("ROCKET", "로켓 배송"));
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }

    @GetMapping
    public String items(Model model) {

        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        // 빈 껍데기를 넘겨줘야 한다.
        model.addAttribute("item", new Item());
        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {

        // Item 에서 form 에서 선택한 값들이 알아서 조립되어서 들어가 있다
        // @ModelAttribute 덕분에!  ( 참고로 HTTP API 통한건 @RequestBody 이다! )
        log.info("item.open={}", item.getOpen());
        // 화면에서 regions 쪽에서 선택한 태그들이 각각 전달되는데 스프링이 알아서 array 형태로 조합해준다!
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());
        log.info("item.deliveryCode={}", item.getDeliveryCode());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}

