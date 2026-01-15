package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

public class ConversionServiceTest {

    @Test
    void conversionService() {
        
        // Converter 등록
        // ConverterRegistry : 등록
        // ConversionService : 사용
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());
        
        // 사용 source , TargetType에 맞게 ConversionService에 등록된 converter 를 체크해서
        // 알아서 바인딩해준다!
        Integer result = conversionService.convert("10", Integer.class);
        Assertions.assertThat(result).isEqualTo(10);

        // 클래스명.class 는 그냥 해당 클래스 전체정보를 넘기는 것
        IpPort result2 = conversionService.convert("121.1.1.1:8080", IpPort.class);
        IpPort test = new IpPort("121.1.1.1", 8080);

        Assertions.assertThat(result2).isEqualTo(test);
    }
}
