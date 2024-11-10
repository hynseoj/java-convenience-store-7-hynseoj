package store.application.facade;

import java.util.Map;
import store.application.service.InventoryService;
import store.model.Products;
import store.model.Promotions;

public class StoreFacade {

    private final Products products;
    private final Promotions promotions;
    private final InventoryService inventoryService;

    public StoreFacade(Products products, Promotions promotions) {
        this.products = products;
        this.promotions = promotions;
        inventoryService = new InventoryService(products);
    }

    public void purchase(Map<String, Integer> purchaseItems) {
        inventoryService.validateItemsExist(purchaseItems.keySet());
    }
}
