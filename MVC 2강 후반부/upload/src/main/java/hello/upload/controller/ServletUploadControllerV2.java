package hello.upload.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    // application.properties 속성 가져오기가 가능
    @Value("${file.dir}")
    private String fileDir;


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
        log.info("parts = {}", parts);

        for (Part part : parts) {
            log.info("========== Part Info ==========");
            log.info("part = {}", part);
            log.info("part name = {}", part.getName());

            log.info("========== Header Info ==========");
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("headerName : {}", headerName);
            }
            log.info("submittedFileName={}", part.getSubmittedFileName());
            log.info("size={}", part.getSize());

            // 데이터 읽기
            InputStream inputStream = part.getInputStream();
            String bodyData = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            log.info("bodyData = {}", bodyData);

            // 파일 저장하기 ( submittedFileName 는 실제 첨부파일 있을때만 해당 값이 존재 )
            if (StringUtils.hasText(part.getSubmittedFileName())) {
                String fullPath = fileDir + part.getSubmittedFileName();

                log.info("fullPath = {}", fullPath);

                // part는 write 를 지원해 줌
                part.write(fullPath);
            }
        }

        return "upload-form";
    }
}
