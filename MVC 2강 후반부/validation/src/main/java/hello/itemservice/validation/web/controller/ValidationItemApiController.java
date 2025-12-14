package hello.itemservice.validation.web.controller;

import hello.itemservice.validation.web.controller.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
// 던지는 태그를 알아서 화면에 뿌려질수 있도록 세팅해주는게 @RestController
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {


    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm itemSaveForm, BindingResult bindingResult) {

        log.info("itemSaveForm = {}", itemSaveForm);

        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);

            return bindingResult.getAllErrors();
        }

        return itemSaveForm;
    }
}
