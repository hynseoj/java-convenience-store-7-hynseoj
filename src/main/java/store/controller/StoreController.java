package store.controller;

import store.application.facade.StoreFacade;
import store.common.dto.PurchaseRequest;
import store.model.Products;
import store.model.Promotions;
import store.view.OutputView;

public class StoreController {

    private final InputHandler inputHandler;
    private final OutputView outputView;

    public StoreController(InputHandler inputHandler, OutputView outputView) {
        this.inputHandler = inputHandler;
        this.outputView = outputView;
    }

    public void run() {
        Promotions promotions = inputHandler.getPromotions();
        Products products = inputHandler.getProducts(promotions);
        StoreFacade storeFacade = new StoreFacade(products, promotions);
        outputView.printStoreInventory(products);

        PurchaseRequest purchaseItems = inputHandler.getPurchaseItems();
        storeFacade.purchase(purchaseItems);
    }
}
