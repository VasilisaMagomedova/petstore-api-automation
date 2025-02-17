package payload;

import java.util.List;

public class Pet {
    private int id;
    private PetCategory category;
    private String name;
    private List<String> photoUrls;
    private List<PetTag> tags;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PetCategory getCategory() {
        return category;
    }

    public void setCategory(int categoryId, String categoryName) {
        this.category = new PetCategory(categoryId, categoryName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public List<PetTag> getTags() {
        return tags;
    }

    public void setTags(List<PetTag> tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
