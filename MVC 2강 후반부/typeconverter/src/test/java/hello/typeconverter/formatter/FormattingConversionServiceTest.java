package hello.typeconverter.formatter;

import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

public class FormattingConversionServiceTest {

    @Test
    void formattingConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        // 컨버터 등록   ( FormattingConversionService 가 애초에 ConversionService를 상속받아 구현된 클래스임 )
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        // 포매터 등록
        conversionService.addFormatter(new MyNumberFormatter());

        // 컨버터 사용
        IpPort ipPort = conversionService.convert("127.1.1.1:8080", IpPort.class);
        Assertions.assertThat(ipPort).isEqualTo(new IpPort("127.1.1.1", 8080));

        // 포매터 사용 ( 이렇게 사용할 때는 그냥 .convert 써주면 알아서 적절한 포매터 매서드를 호출해 준다.
        // 원래는 .parse ( Str -> T ) , .print ( T -> Str ) 로 구분되어있다!
        Integer resInt = conversionService.convert("1,000,000", Integer.class);
        String resStr = conversionService.convert(100000, String.class);

        Assertions.assertThat(resInt).isEqualTo(1000000);
        Assertions.assertThat(resStr).isEqualTo("100,000");
    }

}
