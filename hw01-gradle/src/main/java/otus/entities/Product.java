package otus.entities;

public class Product {
    public Integer id;
    public String name;

    public Product(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
