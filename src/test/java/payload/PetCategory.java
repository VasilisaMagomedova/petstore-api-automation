package payload;

public class PetCategory {
    private int id;
    private String name;

    public PetCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
