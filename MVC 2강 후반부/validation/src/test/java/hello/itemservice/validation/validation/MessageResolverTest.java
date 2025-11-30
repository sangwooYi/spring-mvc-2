package hello.itemservice.validation.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageResolverTest {

    MessageCodesResolver messageCodesResolver = new DefaultMessageCodesResolver();

    /**
     * errors.properties 에는 아래 규칙에 맞게 선언해 두어야 한다.
     */

    @Test
    void messageCodesResolverObject() {
        String[] messageCodes = messageCodesResolver.resolveMessageCodes("required", "item");

        // required.item   에러코드/오브젝트명
        // required        에러코드
        for (String messageCode : messageCodes) {
            System.out.printf("messageCode = %s \n", messageCode);
        }

        Assertions.assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField() {
        // 세번째에 필드값 , 네번째에 필드 타입 클래스 전달해주면 FieldError!
        String[] messageCodes = messageCodesResolver.resolveMessageCodes("required", "item",  "itemName", String.class);

        // required.item.itemName       에러코드/오브젝트/필드명
        // required.itemName            에러코드/필드명
        // required.java.lang.String    에러코드/필드 Type
        // required                     에러코드
        for (String messageCode : messageCodes) {
            System.out.printf("messageCode = %s \n", messageCode);
        }

        Assertions.assertThat(messageCodes).containsExactly("required.item.itemName", "required.itemName", "required.java.lang.String", "required");
    }
}
