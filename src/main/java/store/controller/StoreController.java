package store.controller;

import static store.common.constant.PromotionNotice.GET_FREE_M_NOTICE;
import static store.common.constant.PromotionNotice.OUT_OF_STOCK_NOTICE;

import java.util.HashMap;
import store.application.service.InventoryService;
import store.application.service.PromotionService;
import store.common.dto.PromotionConditionResult;
import store.common.dto.PurchaseRequest;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.model.Cart;
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

        Cart cart = Cart.from(new HashMap<>());
        promotionalProducts.products().forEach(product -> {
            int purchaseQuantity = purchaseItems.cart().get(product.name());
            PromotionConditionResult conditionResult = promotionService.checkPromotionCondition(
                    product, purchaseQuantity
            );

            if (conditionResult.message().equals(GET_FREE_M_NOTICE.message())) {
                outputView.printGetFreeNotice(conditionResult.message());
                boolean wantToAdd = inputHandler.getYesOrNo();
                if (wantToAdd) {
                    cart.addProduct(product, purchaseQuantity + 1);
                } else {
                    cart.addProduct(product, purchaseQuantity);
                }
                purchaseItems.cart().remove(product.name());
            }
            if (conditionResult.message().equals(OUT_OF_STOCK_NOTICE.message())) {
                outputView.printPromotionOutOfStockNotice(conditionResult.message());
                boolean wantToBuy = inputHandler.getYesOrNo();
                cart.addProduct(product, conditionResult.stockUsed());
                if (wantToBuy) {
                    purchaseItems.cart().replace(product.name(), purchaseQuantity - conditionResult.stockUsed());
                } else {
                    purchaseItems.cart().remove(product.name());
                }
            }
        });
    }
}
