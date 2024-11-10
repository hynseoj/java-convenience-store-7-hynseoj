package store.application.facade;

import java.util.Set;
import store.application.service.InventoryService;
import store.application.service.PromotionService;
import store.common.dto.PurchaseRequest;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.model.Product;
import store.model.Products;
import store.model.Promotions;

public class StoreFacade {

    private final Products products;
    private final Promotions promotions;
    private final InventoryService inventoryService;
    private final PromotionService promotionService;

    public StoreFacade(Products products, Promotions promotions) {
        this.products = products;
        this.promotions = promotions;
        inventoryService = new InventoryService(products);
        promotionService = new PromotionService(products);
    }

    public void purchase(PurchaseRequest purchaseItems) {
        PurchaseProductNames productNames = purchaseItems.getProductNames();

        inventoryService.validateItemsExist(productNames);
        inventoryService.checkItemsStock(purchaseItems);

        Set<Product> promotionalProducts = promotionService.getPromotionalProducts(productNames);
    }
}
