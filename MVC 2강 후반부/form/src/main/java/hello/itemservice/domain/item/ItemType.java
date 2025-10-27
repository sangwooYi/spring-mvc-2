package hello.itemservice.domain.item;

public enum ItemType {

    BOOK("도서"), FOOD("음식"), ETC("기타");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    
    // Enum 테스트용
    public static void main(String[] args) {
        ItemType[] itemTypes = ItemType.values();

        for (ItemType itemType : itemTypes) {
            // .name() 은 그냥 애초에 Enum 명칭
            System.out.println(itemType.name());
            System.out.println(itemType.getDescription());
        }

    }
}
