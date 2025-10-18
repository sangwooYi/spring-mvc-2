package hello.thymeleafbasic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @ComponentScan 은 이 어노테이션이 거려있는 하위 패키지 전체를 스캔한다.
@SpringBootApplication
public class ThymeleafbasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThymeleafbasicApplication.class, args);
	}

}
