package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConverterTest {

    @Test
    void stringToInteger() {
        StringToIntegerConverter converter = new StringToIntegerConverter();

        Integer res = converter.convert("10");

        Assertions.assertThat(res).isEqualTo(10);
    }

    @Test
    void intToString() {
        IntegerToStringConverter converter = new IntegerToStringConverter();

        String res = converter.convert(10);

        Assertions.assertThat(res).isEqualTo("10");
    }

    @Test
    void stringToIpPort() {
        StringToIpPortConverter converter = new StringToIpPortConverter();

        IpPort res = converter.convert("127.0.0.1:8080");

        IpPort test1 = new IpPort("127.0.0.1", 8080);

        // isEqualTo 는 값 자체만 비교 ( 따라서 equals 오버라이딩 되어있으면 값 비교로 같은지 여부 체크)
        // isSameAs 는 주소까지비교, 즉 진짜로 완전히 같은 객체인지 체크하는 것
        Assertions.assertThat(res).isEqualTo(test1);
    }

    @Test
    void IpPortToString() {
        IpPortToStringConverter converter = new IpPortToStringConverter();

        IpPort test1 = new IpPort("127.0.0.1", 8080);
        String res = converter.convert(test1);

        Assertions.assertThat(res).isEqualTo("127.0.0.1:8080");
    }
}
