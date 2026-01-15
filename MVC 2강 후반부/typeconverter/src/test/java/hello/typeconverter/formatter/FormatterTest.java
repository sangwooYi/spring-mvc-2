package hello.typeconverter.formatter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Locale;

public class FormatterTest {

    MyNumberFormatter myNumberFormatter = new MyNumberFormatter();

    @Test
    void parse() {
        // Number 는 byte, short, int, long, float, double 등 숫자 관련 primitive 타입으로
        // 컨버팅 가능한 객체 .initValue() 등으로 필요한 타입으로 변환하여 받아갈 수 있다.
        Number number = null;
        try {
            number = myNumberFormatter.parse("1,000,000", Locale.KOREA);
        } catch (ParseException e) {

        }
        if (number != null) {
            Assertions.assertThat(number.intValue()).isEqualTo(1000000);
        }
    }

    @Test
    void print() {
        String print = myNumberFormatter.print(1000000, Locale.KOREA);

        Assertions.assertThat(print).isEqualTo("1,000,000");
    }
}
