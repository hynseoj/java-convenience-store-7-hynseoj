package store.controller;

import static store.common.constant.PromotionNotice.DEFAULT_NOTICE;

import java.util.HashSet;
import java.util.Set;
import store.application.service.InventoryService;
import store.application.service.PromotionService;
import store.common.dto.PromotionConditionResult;
import store.common.dto.PromotionResult;
import store.common.dto.PurchaseRequest;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.model.ProductCatalog;
import store.model.Products;
import store.model.PromotionCatalog;
import store.view.OutputView;

public class StoreController {

    private final InputHandler inputHandler;
    private final OutputView outputView;
    private InventoryService inventoryService;
    private PromotionService promotionService;

    public StoreController(InputHandler inputHandler, OutputView outputView) {
        this.inputHandler = inputHandler;
        this.outputView = outputView;
    }

    public void run() {
        PromotionCatalog promotionCatalog = inputHandler.getPromotions();
        ProductCatalog productCatalog = inputHandler.getProducts(promotionCatalog);
        outputView.printStoreInventory(productCatalog);

        inventoryService = new InventoryService(productCatalog);
        promotionService = new PromotionService(productCatalog);

        PurchaseRequest purchaseItems = inputHandler.getPurchaseItems();
        PurchaseProductNames productNames = purchaseItems.getProductNames();

        inventoryService.validateItemsExist(productNames);
        inventoryService.checkItemsStock(purchaseItems);

        Products promotionalProducts = promotionService.getPromotionalProducts(productNames);

        Set<PromotionResult> promotionResults = new HashSet<>();
        promotionalProducts.products().forEach(product -> {
            int purchaseQuantity = purchaseItems.cart().get(product.name());
            PromotionConditionResult conditionResult = promotionService.checkPromotionCondition(
                    product, purchaseQuantity
            );

            if (!conditionResult.message().equals(DEFAULT_NOTICE.message())) {
                outputView.printPromotionNotice(conditionResult.message());
            }
        });
    }
}
