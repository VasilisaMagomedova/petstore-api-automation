package payload;

public class PetTag {
    private int id;
    private String name;

    public PetTag(int id, String name) {
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
