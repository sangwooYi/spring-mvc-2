package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {

    // 클라이언트가 올린 파일명
    private String originalFileName;
    
    // 서버에 저장할 파일 명
    private String storeFileName;

}
