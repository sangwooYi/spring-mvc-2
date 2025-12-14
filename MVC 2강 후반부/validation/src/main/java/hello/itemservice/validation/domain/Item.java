package hello.itemservice.validation.domain;

import lombok.*;


@Data
@AllArgsConstructor
public class Item {

    //@NotNull(groups = UpdateCheck.class)    // 수정때만
    private Long id;

    //@NotNull(groups = {SaveCheck.class, UpdateCheck.class})    // 이건 null
    //@NotBlank(groups = {SaveCheck.class, UpdateCheck.class})    // 이건 공백 주의 NotNull 은 Blank 체크는 못해준다.
    private String itemName;

    //@NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    //@Range(min=1000, max=1000000,groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    //@NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    //@Max(value = 9999, groups = SaveCheck.class)    // 저장때만
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }


}
