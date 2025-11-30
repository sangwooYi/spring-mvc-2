package hello.itemservice.validation.domain;

// jakarta 는 어디서나 사용가능 ( 예전 javax 가 jakarta 로 상표명 바뀐거 )
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// 이건 hibernate validator 깔려있는 경우에만 사용 가능
import org.hibernate.validator.constraints.Range;

@Data
public class Item {

    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min=1000, max=1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
