package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource messageSource;

    @Test
    void helloMessage() {
        String res1 = messageSource.getMessage("hello", null, null);

        String res2 = messageSource.getMessage("hello", null, Locale.ENGLISH);
        String res3 = messageSource.getMessage("hello", null, Locale.KOREA);

        Object[] test = new Object[1];
        test[0] = "이상우";
    
        // 없으면 NoSuchMessageException 이 예외 발생
        String res4 = messageSource.getMessage("hello.name", test, Locale.KOREA);

        // 실제 동일여부를 비교할떄는 isEqualTo 를사용
        // isSameAs 의 경우는 int 같은 primitive 값을 비교할때만 사용하자
        Assertions.assertThat(res1).isEqualTo("안녕");
        Assertions.assertThat(res2).isEqualTo("hello");
        // 예외 체크
        Assertions.assertThatThrownBy(() -> messageSource.getMessage("tttt", null, null))
                .isInstanceOf(NoSuchMessageException.class);

    }
}
