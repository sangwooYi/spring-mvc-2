package hello.upload.file;

import hello.upload.domain.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    private String extractExt(String originalFileName) {

        if (!StringUtils.hasText(originalFileName)) {
            return "";
        }
        int pos = originalFileName.lastIndexOf(".");
        // endIndex 없으면 beginIndex 부터 끝까지. 있으면 인덱스 기준 a ~ b-1 까지
        return originalFileName.substring(pos+1);
    }

    private String createStoreFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String ext = this.extractExt(originalFileName);

        return uuid + "." + ext;
    }

    public List<UploadFile> storeFileBulk(List<MultipartFile> multipartFileList) throws IOException{

        List<UploadFile> uploadFileList = new ArrayList<>();

        for (MultipartFile file : multipartFileList) {
            UploadFile uploadFile = this.storeFile(file);
            uploadFileList.add(uploadFile);
        }

        return  uploadFileList;
    }

    public UploadFile storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        String originalFileName = file.getOriginalFilename();
        String storeFileName = this.createStoreFileName(originalFileName);

        // file.transferTo() 이 안엔 File 객체가 들어가야하며, File 안에는 Full Path 경로가 들어가야한다.
        // transferTo 는 임시메모리에 데이터를 실제 경로에 저장해 주는 역할 (말그대로 transfer to !)
        file.transferTo(new File(getFullPath(storeFileName)));

        UploadFile uploadFile = new UploadFile();
        uploadFile.setOriginalFileName(originalFileName);
        uploadFile.setStoreFileName(storeFileName);

        return uploadFile;
    }

}
