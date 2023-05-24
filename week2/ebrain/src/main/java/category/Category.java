package category;

public class Category {
    /**
     * 카테고리 ID
     */
    private int categoryId;
    /**
     * 카테고리 이름
     */
    private String categoryName;

    public Category() {
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
