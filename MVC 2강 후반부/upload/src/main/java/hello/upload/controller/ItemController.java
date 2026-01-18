package hello.upload.controller;

import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.UriUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    // 생성자 DI 방식과 동일
    private final ItemRepository itemRepository;
    private final FileStore myFileStore;

    @GetMapping("/new")
    public String newItem(@ModelAttribute ItemForm itemForm) {
        return "item-form";
    }

    @PostMapping("/new")
    public String saveItem(@ModelAttribute ItemForm itemForm,
                           RedirectAttributes redirectAttributes) throws IOException {
        log.info("itemForm = {}", itemForm);
        MultipartFile attachFile = itemForm.getAttachFile();
        UploadFile uploadFile = myFileStore.storeFile(attachFile);

        List<MultipartFile> imageFiles = itemForm.getImageFiles();
        List<UploadFile> uploadImageList = myFileStore.storeFileBulk(imageFiles);

        // 저장 보통 DB 에는 파일 경로만 저장한다.
        Item item = new Item();
        item.setItemName(itemForm.getItemName());
        item.setUploadFile(uploadFile);
        item.setUploadFileList(uploadImageList);
        itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", item.getId());

        log.info("uploadImageList = {}", uploadImageList);

        return "redirect:/items/{itemId}";
    }

    @GetMapping("/{itemId}")
    public String itemPage(@PathVariable Long itemId, Model model) {

        Item item = itemRepository.findById(itemId);
        log.info("item = {}", item);

        model.addAttribute("item", item);

        return "item-view";
    }

    @ResponseBody
    @GetMapping("/images/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {

        log.info("fileName = {}", fileName);
        // Resource 인터페이스의 자식중 UrlResource 가 존재함!
        return new UrlResource("file:" + myFileStore.getFullPath(fileName));
    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {

        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getUploadFile().getStoreFileName();
        String uploadFileName = item.getUploadFile().getOriginalFileName();

        UrlResource resource = new UrlResource("file:" + myFileStore.getFullPath(storeFileName));

        // 헤더에 넣으려면 인코딩한 값을 넣어주어야 한다 주의!!
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        // 이건 그냥 규약이다 ( 첨부한 파일명에 대한 정보를 헤더에 추가하는 작업"
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        // 헤더에 넣어주어야 함.
        return ResponseEntity.ok()  // 200 Status 코드 반환
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
