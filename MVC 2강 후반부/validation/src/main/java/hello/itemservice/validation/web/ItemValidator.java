package hello.itemservice.validation.web;

import hello.itemservice.validation.domain.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

    // 지원 여부 체크
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors bindingResult) {

        Item item = (Item) target;

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
                // 주의! ObjectError 에 objectName 은 사실 프론트에서는 크게 의미 X , 백엔드 유지보수 과정에 필요한 친구 따라서 의미있게 부여할 것
                //bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, totalCost} , "총 금액은 1만원이 넘어야 합니다. 현재 총 금액 = " + totalCost));
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalCost} , "총 금액은 1만원이 넘어야 합니다. 현재 총 금액 = " + totalCost);
            }
        }

    }
}
