package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class ConverterController {

    @GetMapping("/converter-view")
    public String converterView(Model model) {
        model.addAttribute("number", 10000000);
        model.addAttribute("ipPort", new IpPort("127.0.0.1", 8080));

        return "converterView";
    }

    @GetMapping("/converter-form")
    public String converterFormPage(Model model) {

        IpPort ipPort = new IpPort("127.0.0.1", 8080);
        MyData myData = new MyData(ipPort);

        model.addAttribute("myData", myData);

        return "converterForm";
    }

    @PostMapping("/converter-form")
    public String converterFormTest(@ModelAttribute IpPort ipPort,  // 스프링이 String -> IpPort 컨버터 적용 해 줌
                                    Model model) {
        log.info("ipPort = {}", ipPort);

        model.addAttribute("number", 11);
        model.addAttribute("ipPort", ipPort);
        return "converterView";
    }

    @Data
    static class MyData {
        private IpPort ipPort;

        public MyData(IpPort ipPort) {
            this.ipPort = ipPort;
        }

        public IpPort getIpPort() {
            return this.ipPort;
        }

        public void setIpPort(IpPort ipPort) {
            this.ipPort = ipPort;
        }
    }
}

