package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {

    @Override
    public IpPort convert(String source) {

        // "127.0.0.1:8080" 이런 형태 즉 ip주소:포트주소 형태로 받음
        log.info("source = {}", source);

        String[] split = source.split(":");
        String ip = split[0];
        String port = split[1];
        // 그냥 int로 바꿀거면 .parseInt, Integer로 반환할거면 .valueOf 사용!
        int portToInt = Integer.parseInt(port);

        return new IpPort(ip, portToInt);
    }
}
