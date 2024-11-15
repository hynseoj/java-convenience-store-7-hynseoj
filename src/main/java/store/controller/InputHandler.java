package store.controller;

import static store.common.constant.ErrorMessage.INPUT_INVALID_FORMAT;
import static store.common.constant.ErrorMessage.PRODUCT_NOT_FOUND_ERROR;
import static store.common.constant.ErrorMessage.PRODUCT_OUT_OF_STOCK;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import store.common.dto.PurchaseRequest;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.common.util.FileReader;
import store.common.util.StringUtils;
import store.model.Product;
import store.model.ProductCatalog;
import store.model.Products;
import store.model.Promotion;
import store.model.PromotionCatalog;
import store.model.promotion.BuyNGetMFreePromotion;
import store.model.promotion.PromotionStrategy;
import store.view.InputView;

public class InputHandler {

    private static final Pattern PURCHASE_ITEM_REGEX = Pattern.compile("^\\[(.+)\\-(.+)\\]$");

    private final InputView inputView;

    public InputHandler(InputView inputView) {
        this.inputView = inputView;
    }

    public PromotionCatalog getPromotions() {
        try {
            List<String> fileLines = FileReader.readFile("src/main/resources/promotions.md");
            return PromotionCatalog.from(fileLines.stream()
                    .skip(1)
                    .map(fileLine -> makePromotion(StringUtils.splitWithDelimiter(fileLine, ",")))
                    .collect(Collectors.toMap(Promotion::name, promotion -> promotion))
            );
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    public ProductCatalog getProducts(PromotionCatalog promotionCatalog) {
        try {
            List<String> fileLines = FileReader.readFile("src/main/resources/products.md");
            return ProductCatalog.from(fileLines.stream()
                    .skip(1)
                    .map(fileLine -> makeProduct(StringUtils.splitWithDelimiter(fileLine, ","), promotionCatalog))
                    .collect(Collectors.toSet())
            );
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    public PurchaseRequest getPurchaseItems(ProductCatalog productCatalog) {
        return validate(() -> {
            List<String> purchaseItems = StringUtils.splitWithDelimiter(inputView.getPurchaseItems(), ",");
            PurchaseRequest request = getPurchaseRequest(purchaseItems);
            validateItemsExist(request.getProductNames(), productCatalog);
            checkItemsStock(request, productCatalog);
            return request;
        });
    }

    private PurchaseRequest getPurchaseRequest(List<String> purchaseItems) {
        try {
            return PurchaseRequest.from(
                    purchaseItems.stream()
                            .map(purchaseItem -> StringUtils.extractFromRegex(purchaseItem, PURCHASE_ITEM_REGEX))
                            .collect(Collectors.toMap(item -> item.get(0).strip(),
                                    item -> StringUtils.parseInt(item.get(1))))
            );
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(INPUT_INVALID_FORMAT.message());
        }
    }

    public boolean isAffirmative() {
        return validate(() -> {
            String yesOrNo = inputView.getYesOrNo().toUpperCase(Locale.ROOT);
            if (!yesOrNo.equals("Y") && !yesOrNo.equals("N")) {
                throw new IllegalArgumentException(INPUT_INVALID_FORMAT.message());
            }
            return yesOrNo.equals("Y");
        });
    }

    private Promotion makePromotion(List<String> promotionValues) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        String name = promotionValues.get(0);
        int purchaseQuantity = StringUtils.parseInt(promotionValues.get(1));
        int freeQuantity = StringUtils.parseInt(promotionValues.get(2));
        PromotionStrategy promotionStrategy = new BuyNGetMFreePromotion(purchaseQuantity, freeQuantity);
        LocalDate startDate = LocalDate.parse(promotionValues.get(3), formatter);
        LocalDate endDate = LocalDate.parse(promotionValues.get(4), formatter);
        return Promotion.of(name, promotionStrategy, startDate, endDate);
    }

    private Product makeProduct(List<String> productValues, PromotionCatalog promotionCatalog) {
        String name = productValues.get(0);
        int price = StringUtils.parseInt(productValues.get(1));
        int stock = StringUtils.parseInt(productValues.get(2));
        Promotion promotion = promotionCatalog.getPromotionByName(productValues.get(3));
        return Product.of(name, price, stock, promotion);
    }

    private void validateItemsExist(PurchaseProductNames productNames, ProductCatalog productCatalog) {
        boolean doesNotContainsAllProduct = !productCatalog.doesContainsAllProduct(productNames.productNames());
        if (doesNotContainsAllProduct) {
            throw new IllegalArgumentException(PRODUCT_NOT_FOUND_ERROR.message());
        }
    }

    private void checkItemsStock(PurchaseRequest purchaseItems, ProductCatalog productCatalog) {
        purchaseItems.items().forEach((productName, quantity) -> {
            Products product = productCatalog.getProductByName(productName);
            int totalStock = product.products().stream().mapToInt(Product::stock).sum();
            boolean isOutOfStock = quantity > totalStock;
            if (isOutOfStock) {
                throw new IllegalArgumentException(PRODUCT_OUT_OF_STOCK.message());
            }
        });
    }

    private <T> T validate(Supplier<T> inputSupplier) {
        while (true) {
            try {
                return inputSupplier.get();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
