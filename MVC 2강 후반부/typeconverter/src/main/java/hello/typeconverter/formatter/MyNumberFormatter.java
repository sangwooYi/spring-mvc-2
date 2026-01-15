package hello.typeconverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 *  Formatter<T>
 *  print -> T 를 String 으로 변환
 *  parse -> String 을 T로 변환
 */

@Slf4j
public class MyNumberFormatter implements Formatter<Number> {

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("text = {}", text);
        log.info("locale = {}", locale);
        NumberFormat format = NumberFormat.getInstance(locale);
        return format.parse(text);
    }

    @Override
    public String print(Number object, Locale locale) {
        log.info("object = {}", object);
        log.info("locale = {}", locale);

        NumberFormat format = NumberFormat.getInstance(locale);

        return format.format(object);
    }
}
