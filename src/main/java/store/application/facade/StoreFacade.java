package store.application.facade;

import store.application.service.InventoryService;
import store.application.service.PromotionService;
import store.common.dto.PurchaseRequest;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.model.ProductCatalog;
import store.model.Products;
import store.model.PromotionCatalog;

public class StoreFacade {

    private final ProductCatalog productCatalog;
    private final PromotionCatalog promotionCatalog;
    private final InventoryService inventoryService;
    private final PromotionService promotionService;

    public StoreFacade(ProductCatalog productCatalog, PromotionCatalog promotionCatalog) {
        this.productCatalog = productCatalog;
        this.promotionCatalog = promotionCatalog;
        inventoryService = new InventoryService(productCatalog);
        promotionService = new PromotionService(productCatalog);
    }

    public void purchase(PurchaseRequest purchaseItems) {
        PurchaseProductNames productNames = purchaseItems.getProductNames();

        inventoryService.validateItemsExist(productNames);
        inventoryService.checkItemsStock(purchaseItems);

        Products promotionalProducts = promotionService.getPromotionalProducts(productNames);

        promotionalProducts.products().forEach(product -> {
            int purchaseQuantity = purchaseItems.cart().get(product.name());
            if (product.isInStock(purchaseQuantity)) {
                product.reduceStock(purchaseQuantity);
            }
        });
    }
}
