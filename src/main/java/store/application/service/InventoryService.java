package store.application.service;

import static store.common.constant.ErrorMessage.PRODUCT_NOT_FOUND_ERROR;
import static store.common.constant.ErrorMessage.PRODUCT_OUT_OF_STOCK;

import store.common.dto.PurchaseRequest;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.model.Product;
import store.model.ProductCatalog;
import store.model.Products;

public class InventoryService {

    private final ProductCatalog productCatalog;

    public InventoryService(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    public void validateItemsExist(PurchaseProductNames productNames) {
        boolean doesNotContainsAllProduct = !this.productCatalog.doesContainsAllProduct(productNames.productNames());
        if (doesNotContainsAllProduct) {
            throw new IllegalArgumentException(PRODUCT_NOT_FOUND_ERROR.message());
        }
    }

    public void checkItemsStock(PurchaseRequest purchaseItems) {
        purchaseItems.cart().forEach((productName, quantity) -> {
            Products product = productCatalog.getProductByName(productName);
            int totalStock = product.products().stream().mapToInt(Product::stock).sum();
            boolean isOutOfStock = quantity > totalStock;
            if (isOutOfStock) {
                throw new IllegalArgumentException(PRODUCT_OUT_OF_STOCK.message());
            }
        });
    }

    public void reduceStock(Product product, int quantity) {
        product.reduceStock(quantity);
    }
}
