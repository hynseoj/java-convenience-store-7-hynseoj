package store.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import store.common.util.FileReader;
import store.common.util.StringUtils;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;
import store.model.Promotions;

public class InputHandler {

    public Promotions getPromotions() {
        try {
            List<String> fileLines = FileReader.readFile("src/main/resources/promotions.md");
            return Promotions.from(fileLines.stream()
                    .skip(1)
                    .map(fileLine -> makePromotion(StringUtils.splitWithDelimiter(fileLine, ",")))
                    .collect(Collectors.toMap(Promotion::name, promotion -> promotion))
            );
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    private Promotion makePromotion(List<String> promotionValues) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String name = promotionValues.get(0);
        int purchaseQuantity = StringUtils.parseInt(promotionValues.get(1));
        int giftQuantity = StringUtils.parseInt(promotionValues.get(2));
        LocalDateTime startDate = LocalDateTime.parse(promotionValues.get(3), formatter);
        LocalDateTime endDate = LocalDateTime.parse(promotionValues.get(4), formatter);
        return Promotion.of(name, purchaseQuantity, giftQuantity, startDate, endDate);
    }

    public Products getProducts(Promotions promotions) {
        try {
            List<String> fileLines = FileReader.readFile("src/main/resources/product.md");
            return Products.from(fileLines.stream()
                    .skip(1)
                    .map(fileLine -> makeProduct(StringUtils.splitWithDelimiter(fileLine, ","), promotions))
                    .collect(Collectors.toSet())
            );
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    private Product makeProduct(List<String> productValues, Promotions promotions) {
        String name = productValues.get(0);
        int price = StringUtils.parseInt(productValues.get(1));
        int quantity = StringUtils.parseInt(productValues.get(2));
        Promotion promotion = promotions.getPromotionByName(productValues.get(3));
        return Product.of(name, price, quantity, promotion);
    }
}
