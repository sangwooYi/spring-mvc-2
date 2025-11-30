package hello.itemservice.validation.validation;

import hello.itemservice.validation.domain.Item;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import java.util.Set;

/**
 * https://docs.hibernate.org/validator/6.2/reference/en-US/html_single/ 
 * 여기서 확인해볼것, 근데 실제로는 스프링에 이미 통합되어있어 직접 이렇게 factory 까지 쓸 일은 거의 없음!
 */
public class BeanValidationTest {

    @Test
    @DisplayName("Bean Validation 테스트")
    void beanValidation() {
        // buildDefaultValidatorFactory 말그대로 디폴트 Validator 생성해주는 팩토리
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();
        item.setItemName("");
        item.setPrice(0);
        item.setQuantity(100000);

        // 검증하는 방법
        Set<ConstraintViolation<Item>> violationSet = validator.validate(item);

        for (ConstraintViolation<Item> violation : violationSet) {
            System.out.println("violation = " + violation);

            // .getMessage() 를 쓰며 ㄴ interpolatedMessage 키값에 저장된 메시지 value 값을 알아서 꺼내준다.
            System.out.println("violation Message = " + violation.getMessage());
        }
    }

}
