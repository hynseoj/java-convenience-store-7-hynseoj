package store.controller;

import java.io.IOException;
import java.util.List;
import store.common.util.FileReader;
import store.model.Promotions;

public class InputHandler {

    public List<String> getPromotions() {
        try {
            return FileReader.readFile("src/main/resources/promotions.md");
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    public List<String> getProducts(Promotions promotions) {
        try {
            return FileReader.readFile("src/main/resources/product.md");
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }
}
