package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {

    @Override
    public String convert(IpPort source) {

        // "127.0.0.1:8080" 이런 형태 즉 ip주소:포트주소 형태로 받음
        log.info("source = {}", source);

        return source.getIp() + ":" + source.getPort();
    }
}
