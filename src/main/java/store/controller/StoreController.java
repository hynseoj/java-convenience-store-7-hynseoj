package store.controller;

import static store.common.constant.PromotionNotice.GET_FREE_M_NOTICE;
import static store.common.constant.PromotionNotice.OUT_OF_STOCK_NOTICE;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import store.application.service.InventoryService;
import store.application.service.PromotionService;
import store.common.dto.PromotionConditionResult;
import store.common.dto.PromotionResult;
import store.common.dto.PurchaseRequest;
import store.common.dto.PurchaseRequest.PurchaseProductNames;
import store.model.Cart;
import store.model.Product;
import store.model.ProductCatalog;
import store.model.Products;
import store.model.PromotionCatalog;
import store.model.Receipt;
import store.view.OutputView;

public class StoreController {

    private final InputHandler inputHandler;
    private final OutputView outputView;
    private InventoryService inventoryService;
    private PromotionService promotionService;
    private ProductCatalog productCatalog;
    private PromotionCatalog promotionCatalog;

    public StoreController(InputHandler inputHandler, OutputView outputView) {
        this.inputHandler = inputHandler;
        this.outputView = outputView;
    }

    public void run() {
        if (promotionCatalog == null || productCatalog == null) {
            promotionCatalog = inputHandler.getPromotions();
            productCatalog = inputHandler.getProducts(promotionCatalog);
            inventoryService = new InventoryService(productCatalog);
            promotionService = new PromotionService(productCatalog);
        }
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

            if (GET_FREE_M_NOTICE.matches(conditionResult.message())) {
                outputView.printGetFreeNotice(conditionResult.message());
                boolean wantToAdd = inputHandler.getYesOrNo();
                if (wantToAdd) {
                    cart.addProduct(product, purchaseQuantity + 1);
                } else {
                    cart.addProduct(product, purchaseQuantity);
                }
                purchaseItems.cart().remove(product.name());
            } else if (OUT_OF_STOCK_NOTICE.matches(conditionResult.message())) {
                outputView.printPromotionOutOfStockNotice(conditionResult.message());
                boolean wantToBuy = inputHandler.getYesOrNo();
                cart.addProduct(product, conditionResult.stockUsed());
                if (wantToBuy) {
                    purchaseItems.cart().replace(product.name(), purchaseQuantity - conditionResult.stockUsed());
                } else {
                    purchaseItems.cart().remove(product.name());
                }
            } else {
                cart.addProduct(product, conditionResult.stockUsed());
                purchaseItems.cart().replace(product.name(), purchaseQuantity - conditionResult.stockUsed());
            }
        });

        purchaseItems.cart().keySet().forEach(productName -> {
            Product product = productCatalog.getProductByName(productName).products().stream()
                    .findFirst()
                    .orElseThrow();
            if (product.promotion() != null) {
                System.out.println("이상함");
            }
            cart.addProduct(product, purchaseItems.cart().get(productName));
        });

        outputView.printMembershipNotice();
        boolean hasMembership = inputHandler.getYesOrNo();
        AtomicInteger membershipDiscount = new AtomicInteger();
        if (hasMembership) {
            cart.cart().forEach((product, quantity) -> {
                if (promotionalProducts.products().contains(product)) {
                    return;
                }
                membershipDiscount.addAndGet(product.price() * quantity);
            });
        }
        membershipDiscount.updateAndGet(value -> value * 30 / 100);
        if (membershipDiscount.get() > 8000) {
            membershipDiscount.updateAndGet(value -> 8000);
        }

        List<PromotionResult> promotionResults = cart.cart().keySet().stream()
                .filter(Product::isPromotionApplicable)
                .map(product -> product.applyPromotion(cart.cart().get(product)))
                .toList();

        Receipt receipt = Receipt.of(cart, promotionResults, membershipDiscount.get());
        outputView.printReceipt(receipt);

        cart.cart().forEach((product, quantity) -> inventoryService.reduceStock(product, quantity));

        outputView.printRepurchaseNotice();
        boolean wantToRepurchase = inputHandler.getYesOrNo();
        if (wantToRepurchase) {
            run();
        }
    }
}
