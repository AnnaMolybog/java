package otus;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import otus.entities.Product;
import java.util.Map;

public class HelloOtus {
    public static void main(String[] args) {
        Product bread = new Product(1, "Bread");
        Product vegetables = new Product(2, "Vegetables");
        Product fruits = new Product(3, "Fruits");
        Product wine = new Product(4, "Wine");
        Map<Integer, Product> shoppingList = Maps.uniqueIndex(
            Lists.newArrayList(bread, vegetables, fruits, wine),
            new Function<Product, Integer>() {
                public Integer apply(Product product) {
                    return product.id;
                }
            }
        );

        Map<Integer, Product> shoppingCardAnna = Maps.uniqueIndex(
            Lists.newArrayList(wine),
            new Function<Product, Integer>() {
                public Integer apply(Product product) {
                    return product.id;
                }
            }
        );

        Map<Integer, Product> shoppingCardSlava = Maps.uniqueIndex(
            Lists.newArrayList(vegetables),
            new Function<Product, Integer>() {
                public Integer apply(Product product) {
                    return product.id;
                }
            }
        );

        Map<Integer, Product> shoppingCardDifference = Maps.difference(
            Maps.difference(
                shoppingList,
                shoppingCardAnna
            ).entriesOnlyOnLeft(),
            shoppingCardSlava
        ).entriesOnlyOnLeft();


        System.out.println("Remain to buy: " + shoppingCardDifference.values());
    }
}
