package store.controller;

import store.application.facade.StoreFacade;
import store.common.dto.PurchaseRequest;
import store.model.ProductCatalog;
import store.model.PromotionCatalog;
import store.view.OutputView;

public class StoreController {

    private final InputHandler inputHandler;
    private final OutputView outputView;

    public StoreController(InputHandler inputHandler, OutputView outputView) {
        this.inputHandler = inputHandler;
        this.outputView = outputView;
    }

    public void run() {
        PromotionCatalog promotionCatalog = inputHandler.getPromotions();
        ProductCatalog productCatalog = inputHandler.getProducts(promotionCatalog);
        StoreFacade storeFacade = new StoreFacade(productCatalog, promotionCatalog);
        outputView.printStoreInventory(productCatalog);

        PurchaseRequest purchaseItems = inputHandler.getPurchaseItems();
        storeFacade.purchase(purchaseItems);
    }
}
