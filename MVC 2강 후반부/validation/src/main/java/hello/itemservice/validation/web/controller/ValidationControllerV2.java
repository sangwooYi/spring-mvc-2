package hello.itemservice.validation.web.controller;

import hello.itemservice.validation.domain.Item;
import hello.itemservice.validation.domain.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping("")
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    // codes 랑 arguments 활용 Version
    // BindingResult 는 검증해야할 객체 바로 다음에 선언해줘야함
    @PostMapping("/add/temp")
    public String addItem(@ModelAttribute Item item, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {


        // 검증 로직
        // StringUtils.hasText(텍스트) 해당 텍스트가 빈값인지 체크
        // return (str != null && !str.isBlank()); 가 로직
        if (!StringUtils.hasText(item.getItemName())) {
            // 인자로 객체 / 필드명 / 에러명 순으로 넣어주면 된다.
            // DTO의 필드면 FieldError 쓰면 된다.
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
        }

        // null 값 체크 및 최소 최대값 체크
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "값은 1000원 이상 100만원까지 범위입니다."));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지입니다."));
        }

        // 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalCost = item.getPrice()*item.getQuantity();
            if (totalCost < 10000) {
                // 필드가 아닌 값으로 에러를 넘기려면 ObjectError 를 사용 ( FieldError 가 애초에 ObjectError 상속받은 친구임 )
//              // 주의! ObjectError 에 objectName 은 사실 프론트에서는 크게 의미 X , 백엔드 유지보수 과정에 필요한 친구 따라서 의미있게 부여할 것
                bindingResult.addError(new ObjectError("item",  new String[]{"totalPriceMin"}, new Object[]{10000, totalCost} ,"총 금액은 1만원이 넘어야 합니다. 현재 총 금액 = " + totalCost));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);

            // 굳이 모델에 담을 필요 X
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);

        // 리다이렉트 때 attribute 할당 하는 방법, Path Variable 과 매핑되는 값은 자동으로 매핑,
        // 나머지는 쿼리스트링 형태로 자동 세팅
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // 위에 초기버전은 FieldError 발생시에, 기존 값이 유지가 안됨 이에 대한 보완책
    // V1 objectName, field, defaultMessage 까지만 입력이었음 ( 나마진 null / false 로 , 직접 확인해볼 것)
    /**
     매개변수는 아래와 같음
     objectName –> 오브젝트명
     field –> 체크할 필드
     rejectedValue -> 거부당한 값. (즉 에러가 발생할 때 클라이언트에서 보낸 값)
     bindingFailure – 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값 ( 바인딩 실패면 true / 아니면 false )
     -> 참고로 바인딩 실패라면 자동으로 스프링이 FieldError 를 만들어주는데 그 때는 이 값이 자동으로 true 로 생성되는것!
     codes –> 메시지 코드  <= message.properties 처럼 에러코드를 일관되게 설정해서 가져올 수 있음
     arguments –> codes 메시지에 전달할 인자  <= codes에서 사용할 인자를 여기에 담아 줌
     defaultMessage -> 디폴트 메시지
     FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure,
                    @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
     */
//    @PostMapping("/add")
//    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult,
//                          RedirectAttributes redirectAttributes, Model model) {
//
//
//        // 검증 로직
//        // StringUtils.hasText(텍스트) 해당 텍스트가 빈값인지 체크
//        // return (str != null && !str.isBlank()); 가 로직
//        if (!StringUtils.hasText(item.getItemName())) {
//            // 인자로 객체 / 필드명 / 에러명 순으로 넣어주면 된다.
//            // DTO의 필드면 FieldError 쓰면 된다.
//            //bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
//            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false,  null, null, "상품 이름은 필수입니다."));
//        }
//
//        // null 값 체크 및 최소 최대값 체크
//        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null,  "값은 1000원 이상 100만원까지 범위입니다."));
//            //bindingResult.addError(new FieldError("item", "price", "값은 1000원 이상 100만원까지 범위입니다."));
//        }
//
//        if (item.getQuantity() == null || item.getQuantity() > 9999) {
//            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null,  "수량은 최대 9,999 까지입니다."));
//            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지입니다."));
//        }
//
//        // 복합 룰 검증
//        if (item.getPrice() != null && item.getQuantity() != null) {
//            int totalCost = item.getPrice()*item.getQuantity();
//            if (totalCost < 10000) {
//                // 필드가 아닌 값으로 에러를 넘기려면 ObjectError 를 사용 ( FieldError 가 애초에 ObjectError 상속받은 친구임 )
//              // 주의! ObjectError 에 objectName 은 사실 프론트에서는 크게 의미 X , 백엔드 유지보수 과정에 필요한 친구 따라서 의미있게 부여할 것
//                bindingResult.addError(new ObjectError("item",  "총 금액은 1만원이 넘어야 합니다. 현재 총 금액 = " + totalCost));
//                bindingResult.addError(new ObjectError("test",  "테스트테스트 = " + totalCost));
//            }
//        }
//
//        if (bindingResult.hasErrors()) {
//            log.info("bindingResult = {}", bindingResult);
//
//            // 굳이 모델에 담을 필요 X
//            return "validation/v2/addForm";
//        }
//
//        Item savedItem = itemRepository.save(item);
//
//        // 리다이렉트 때 attribute 할당 하는 방법, Path Variable 과 매핑되는 값은 자동으로 매핑,
//        // 나머지는 쿼리스트링 형태로 자동 세팅
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v2/items/{itemId}";
//    }

    @PostMapping("/add/V3")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes, Model model) {

        // Item 에 대해 이미 매핑이 다 되어있다.
        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        // 검증 로직
        // StringUtils.hasText(텍스트) 해당 텍스트가 빈값인지 체크
        // return (str != null && !str.isBlank()); 가 로직
        if (!StringUtils.hasText(item.getItemName())) {
            // 인자로 객체 / 필드명 / 에러명 순으로 넣어주면 된다.
            // DTO의 필드면 FieldError 쓰면 된다.
            //bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false,  new String[]{"required.item.itemName"}, null, "상품 이름은 필수입니다."));
        }

        // null 값 체크 및 최소 최대값 체크
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000},  "값은 1000원 이상 100만원까지 범위입니다."));
            //bindingResult.addError(new FieldError("item", "price", "값은 1000원 이상 100만원까지 범위입니다."));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999},  "수량은 최대 9,999 까지입니다."));
//            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지입니다."));
        }

        // 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalCost = item.getPrice()*item.getQuantity();
            if (totalCost < 10000) {
                // 필드가 아닌 값으로 에러를 넘기려면 ObjectError 를 사용 ( FieldError 가 애초에 ObjectError 상속받은 친구임 )
//              // 주의! ObjectError 에 objectName 은 사실 프론트에서는 크게 의미 X , 백엔드 유지보수 과정에 필요한 친구 따라서 의미있게 부여할 것
                bindingResult.addError(new ObjectError("item",  "총 금액은 1만원이 넘어야 합니다. 현재 총 금액 = " + totalCost));
                bindingResult.addError(new ObjectError("test",  "테스트테스트 = " + totalCost));
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);

            // 굳이 모델에 담을 필요 X
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);

        // 리다이렉트 때 attribute 할당 하는 방법, Path Variable 과 매핑되는 값은 자동으로 매핑,
        // 나머지는 쿼리스트링 형태로 자동 세팅
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    // rejectValue() => FieldError 대체 
    // reject() 활용 => ObjectError 대체
    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes, Model model) {

        // Item 에 대해 이미 매핑이 다 되어있다.
        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        // 검증 로직
        // StringUtils.hasText(텍스트) 해당 텍스트가 빈값인지 체크
        // return (str != null && !str.isBlank()); 가 로직
        if (!StringUtils.hasText(item.getItemName())) {
            // 인자로 객체 / 필드명 / 에러명 순으로 넣어주면 된다.
            // DTO의 필드면 FieldError 쓰면 된다.
            //bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
            // 아래처럼 하는것과 rejectValue 와 현재 properties 상태는 동일하게 작동함
            //bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false,  new String[]{"required.item.itemName", "required"}, null, "상품 이름은 필수입니다."));
            // 아래에 경우에 properties 에 required.item.itemName 를 만약 주석처리하면 required 값을 자동으로 찾아준다.
            bindingResult.rejectValue("itemName", "required");  // required.item.itemName 을 찾아간다.
        }

        // null 값 체크 및 최소 최대값 체크
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            //bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000},  "값은 1000원 이상 100만원까지 범위입니다."));
            //bindingResult.addError(new FieldError("item", "price", "값은 1000원 이상 100만원까지 범위입니다."));
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, "값은 1000원 이상 100만원까지 범위입니다."); // range.item.price 를 알아서 찾아감
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            //bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999},  "수량은 최대 9,999 까지입니다."));
            //bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지입니다."));
            // 세번째에 argument 들, 네번째에는 default 메시지를 전달
            bindingResult.rejectValue("quantity", "max", new Object[]{9999},  "수량은 최대 9,999 까지입니다.");
        }

        // 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int totalCost = item.getPrice()*item.getQuantity();
            if (totalCost < 10000) {
                // 필드가 아닌 값으로 에러를 넘기려면 ObjectError 를 사용 ( FieldError 가 애초에 ObjectError 상속받은 친구임 )
//              // 주의! ObjectError 에 objectName 은 사실 프론트에서는 크게 의미 X , 백엔드 유지보수 과정에 필요한 친구 따라서 의미있게 부여할 것
                //bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, totalCost} , "총 금액은 1만원이 넘어야 합니다. 현재 총 금액 = " + totalCost));
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalCost} , "총 금액은 1만원이 넘어야 합니다. 현재 총 금액 = " + totalCost);
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);

            // 굳이 모델에 담을 필요 X
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);

        // 리다이렉트 때 attribute 할당 하는 방법, Path Variable 과 매핑되는 값은 자동으로 매핑,
        // 나머지는 쿼리스트링 형태로 자동 세팅
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }
}
