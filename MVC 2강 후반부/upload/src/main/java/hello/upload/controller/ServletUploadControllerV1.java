package hello.upload.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v1")
public class ServletUploadControllerV1 {

    @GetMapping("/upload")
    public String uploadForm() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(HttpServletRequest request) throws ServletException, IOException {
        log.info("request = {}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName = {}", itemName);
        
        // 여기서 Part 란 Multipart 에서 각 Part 들을 말함 ( boundary 로 구별되는 정보들 )
        Collection<Part> parts = request.getParts();
        log.debug("parts = {}", parts);

        return "upload-form";
    }
}
